package com.unimagdalena.inventory_service.message.listener;

import com.unimagdalena.inventory_service.config.RabbitKeys;
import com.unimagdalena.inventory_service.message.command.ReleaseInventoryCommand;
import com.unimagdalena.inventory_service.message.command.ReserveInventoryCommand;
import com.unimagdalena.inventory_service.config.RabbitConfig;
import com.unimagdalena.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.unimagdalena.inventory_service.config.RabbitKeys.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryListener {

    private final InventoryService inventoryService;

    @RabbitListener(queues = INVENTORY_RESERVE_QUEUE)
    public void handleReserveCommand(ReserveInventoryCommand cmd) {
        log.info("Recibido ReserveInventoryCommand: {}", cmd);
        inventoryService.reserveInventory(cmd);
    }

    @RabbitListener(queues = INVENTORY_RELEASE_QUEUE)
    public void handleReleaseCommand(ReleaseInventoryCommand cmd) {
        log.info("Recibido ReleaseInventoryCommand: {}", cmd);
        inventoryService.releaseInventory(cmd);
    }
}
