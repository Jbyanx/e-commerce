package com.unimagdalena.inventory_service.message.command;

public record ReleaseInventoryCommand(
        String orderId,
        String productId,
        int quantity
) {
}
