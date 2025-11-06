package com.unimagdalena.order_service.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.unimagdalena.order_service.Dto.OrderServiceDto;
import com.unimagdalena.order_service.comandos.ReserveInventoryCommand;
import com.unimagdalena.order_service.entities.Order;
import com.unimagdalena.order_service.entities.Status;
import com.unimagdalena.order_service.repositories.OrderRepository;
import com.unimagdalena.order_service.services.config.RabbitConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderServiceDto createOrder(String productId, int quantity) {
        Order order = Order.builder()
                .productId(productId)
                .quantity(quantity)
                .status(Status.CREATED)
                .createAt(new java.sql.Timestamp(System.currentTimeMillis()))
                .build();

        orderRepository.save(order);

        // Enviar comando para reservar inventario
        var command = new ReserveInventoryCommand(order.getId().toString(), productId, quantity);
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE, RabbitConfig.RESERVE_INVENTORY, command);

        return OrderServiceDto.builder()
                .id(order.getId().toString())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .createAt(order.getCreateAt())
                .build();
    }
}