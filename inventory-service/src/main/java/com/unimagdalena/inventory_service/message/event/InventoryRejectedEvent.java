package com.unimagdalena.inventory_service.message.event;

public record InventoryRejectedEvent(String orderId, String productId, int quantity) {}

