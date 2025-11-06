package com.unimagdalena.inventory_service.listener;

import com.unimagdalena.inventory_service.message.command.ReleaseInventoryCommand;
import com.unimagdalena.inventory_service.message.command.ReserveInventoryCommand;
import com.unimagdalena.inventory_service.config.RabbitConfig;
import com.unimagdalena.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryListener {
    private final InventoryService inventoryService;

    @RabbitListener(queues = RabbitConfig.INVENTORY_QUEUE)
    public void handleReserveCommand(ReserveInventoryCommand cmd) {
        inventoryService.reserveInventory(cmd);
    }

    @RabbitListener(queues = "release.inventory.queue")
    public void handleReleaseCommand(ReleaseInventoryCommand cmd) {
        inventoryService.releaseInventory(cmd);
    }
}

