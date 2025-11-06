package com.unimagdalena.inventory_service.service;

import com.unimagdalena.inventory_service.repository.InventoryRepository;
import com.unimagdalena.inventory_service.message.command.ReleaseInventoryCommand;
import com.unimagdalena.inventory_service.message.command.ReserveInventoryCommand;
import com.unimagdalena.inventory_service.message.event.InventoryRejectedEvent;
import com.unimagdalena.inventory_service.message.event.InventoryReleasedEvent;
import com.unimagdalena.inventory_service.message.event.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import static com.unimagdalena.inventory_service.config.RabbitConfig.INVENTORY_EXCHANGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RabbitTemplate rabbitTemplate;

    public void reserveInventory(ReserveInventoryCommand cmd) {
        log.info("Reservando inventario para producto {} (orden: {})", cmd.productId(), cmd.orderId());

        inventoryRepository.findByProductId(cmd.productId()).ifPresentOrElse(item -> {
            if (item.getAvailableQuantity() >= cmd.quantity()) {
                item.setAvailableQuantity(item.getAvailableQuantity() - cmd.quantity());
                inventoryRepository.save(item);

                BigDecimal total = item.getPrice().multiply(BigDecimal.valueOf(cmd.quantity()));

                var event = new InventoryReservedEvent(
                        cmd.orderId(), cmd.productId(), cmd.quantity(), total
                );
                rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, "inventory.reserved", event);
                log.info("Inventario reservado y evento enviado: {}", event);
            } else {
                var event = new InventoryRejectedEvent(cmd.orderId(), cmd.productId(), cmd.quantity());
                rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, "inventory.rejected", event);
                log.warn("Inventario insuficiente para producto {}", cmd.productId());
            }
        }, () -> {
            var event = new InventoryRejectedEvent(cmd.orderId(), cmd.productId(), cmd.quantity());
            rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, "inventory.rejected", event);
            log.warn("Producto {} no encontrado en inventario", cmd.productId());
        });
    }

    public void releaseInventory(ReleaseInventoryCommand cmd) {
        log.info("Liberando inventario para producto {} (orden: {})", cmd.productId(), cmd.orderId());

        inventoryRepository.findByProductId(cmd.productId()).ifPresent(item -> {
            item.setAvailableQuantity(item.getAvailableQuantity() + cmd.quantity());
            inventoryRepository.save(item);

            var event = new InventoryReleasedEvent(cmd.orderId(), cmd.productId(), cmd.quantity());
            rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, "inventory.released", event);
            log.info("Inventario liberado y evento enviado: {}", event);
        });
    }
}
