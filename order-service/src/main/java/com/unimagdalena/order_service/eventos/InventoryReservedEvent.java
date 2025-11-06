package com.unimagdalena.order_service.eventos;

public record InventoryReservedEvent(String orderId, String productId, int quantity, long totalAmount) {}
