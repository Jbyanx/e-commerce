package com.unimagdalena.order_service.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.unimagdalena.order_service.Dto.OrderServiceDto;
import com.unimagdalena.order_service.comandos.ReserveInventoryCommand;
import com.unimagdalena.order_service.entities.Order;
import com.unimagdalena.order_service.entities.Status;
import com.unimagdalena.order_service.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

import static com.unimagdalena.order_service.services.config.RabbitKeys.*;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public OrderServiceDto createOrder(String productId, int quantity) {
        // 🧱 Crear la orden localmente
        Order order = Order.builder()
                .productId(productId)
                .quantity(quantity)
                .status(Status.CREATED)
                .createAt(new java.sql.Timestamp(System.currentTimeMillis()))
                .build();

        orderRepository.save(order);

        // 📨 Enviar comando para reservar inventario
        var command = new ReserveInventoryCommand(order.getId().toString(), productId, quantity);
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, INVENTORY_RESERVE, command);

        // 🧾 Devolver DTO
        return OrderServiceDto.builder()
                .id(order.getId().toString())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .createAt(order.getCreateAt())
                .build();
    }

    @Override
    public OrderServiceDto getById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(RuntimeException::new);
        return OrderServiceDto.builder()
                .id(order.getId().toString())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createAt(order.getCreateAt())
                .build();
    }
}
