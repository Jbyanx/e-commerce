package com.unimagdalena.inventory_service.message.command;

public record ReserveInventoryCommand(
        String orderId,
        String productId,
        int quantity
) {
}
