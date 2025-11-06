package com.unimagdalena.inventory_service.listener;

import com.unimagdalena.inventory_service.message.command.ReleaseInventoryCommand;
import com.unimagdalena.inventory_service.message.command.ReserveInventoryCommand;
import com.unimagdalena.inventory_service.config.RabbitConfig;
import com.unimagdalena.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryListener {

    private final InventoryService inventoryService;

    @RabbitListener(queues = RabbitConfig.RESERVE_QUEUE)
    public void handleReserveCommand(ReserveInventoryCommand cmd) {
        log.info("Recibido ReserveInventoryCommand: {}", cmd);
        inventoryService.reserveInventory(cmd);
    }

    @RabbitListener(queues = RabbitConfig.RELEASE_QUEUE)
    public void handleReleaseCommand(ReleaseInventoryCommand cmd) {
        log.info("Recibido ReleaseInventoryCommand: {}", cmd);
        inventoryService.releaseInventory(cmd);
    }
}
