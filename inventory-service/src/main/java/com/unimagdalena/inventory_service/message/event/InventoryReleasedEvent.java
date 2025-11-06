package com.unimagdalena.inventory_service.message.event;

public record InventoryReleasedEvent(String orderId, String productId, int quantity) {}

