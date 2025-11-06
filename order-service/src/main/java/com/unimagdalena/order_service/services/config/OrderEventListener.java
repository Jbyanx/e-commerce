package com.unimagdalena.order_service.services.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.unimagdalena.order_service.comandos.ProcessPaymentCommand;
import com.unimagdalena.order_service.comandos.ReleaseInventoryCommand;
import com.unimagdalena.order_service.entities.*;
import com.unimagdalena.order_service.eventos.InventoryReservedEvent;
import com.unimagdalena.order_service.eventos.OrderCompletedEvent;
import com.unimagdalena.order_service.eventos.PaymentCompletedEvent;
import com.unimagdalena.order_service.eventos.PaymentFailedEvent;
import com.unimagdalena.order_service.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "inventory.events")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        Order order = orderRepository.findById(UUID.fromString(event.orderId()).toString()).orElseThrow();
        order.setStatus(Status.PENDING_PAYMENT);
        orderRepository.save(order);

        rabbitTemplate.convertAndSend("order.exchange", "payment.process",
                new ProcessPaymentCommand(order.getId().toString(), event.totalAmount()));
    }

    @RabbitListener(queues = "payment.events")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        Order order = orderRepository.findById(UUID.fromString(event.orderId()).toString()).orElseThrow();
        order.setStatus(Status.COMPLETED);
        orderRepository.save(order);
        rabbitTemplate.convertAndSend("order.exchange", "order.completed",
                new OrderCompletedEvent(order.getId().toString()));
    }

    @RabbitListener(queues = "payment.events.failed")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        Order order = orderRepository.findById(UUID.fromString(event.orderId()).toString()).orElseThrow();
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
        rabbitTemplate.convertAndSend("order.exchange", "inventory.release",
                new ReleaseInventoryCommand(order.getId().toString(), order.getProductId(), order.getQuantity()));
    }
}
