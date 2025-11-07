package com.unimagdalena.order_service.services.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

import com.unimagdalena.order_service.comandos.ProcessPaymentCommand;
import com.unimagdalena.order_service.comandos.ReleaseInventoryCommand;
import com.unimagdalena.order_service.entities.*;
import com.unimagdalena.order_service.eventos.*;
import com.unimagdalena.order_service.repositories.OrderRepository;
import static com.unimagdalena.order_service.services.config.RabbitKeys.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = INVENTORY_RESERVE_QUEUE)
    public void handleInventoryReserved(InventoryReservedEvent event) {
        log.info("Recibido InventoryReservedEvent: {}", event);
        Order order = orderRepository.findById(UUID.fromString(event.orderId()).toString())
                .orElseThrow();
        order.setStatus(Status.PENDING_PAYMENT);
        orderRepository.save(order);

        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, PAYMENT_PROCESS,
                new ProcessPaymentCommand(order.getId().toString(), event.totalAmount()));
    }

    @RabbitListener(queues = PAYMENT_EVENTS_FAILED_QUEUE)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Recibido PaymentCompletedEvent: {}", event);
        Order order = orderRepository.findById(UUID.fromString(event.orderId()).toString())
                .orElseThrow();
        order.setStatus(Status.COMPLETED);
        orderRepository.save(order);

        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, PAYMENT_COMPLETED,
                new OrderCompletedEvent(order.getId().toString()));
    }

    @RabbitListener(queues = PAYMENT_EVENTS_FAILED_QUEUE)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Recibido PaymentFailedEvent: {}", event);
        Order order = orderRepository.findById(UUID.fromString(event.orderId()).toString())
                .orElseThrow();
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);

        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, INVENTORY_RELEASE,
                new ReleaseInventoryCommand(order.getId().toString(),
                        order.getProductId(), order.getQuantity()));
    }
}
