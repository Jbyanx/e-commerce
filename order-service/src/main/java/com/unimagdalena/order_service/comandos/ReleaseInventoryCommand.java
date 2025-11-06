package com.unimagdalena.order_service.comandos;

public record ReleaseInventoryCommand(String orderId, String productId, int quantity) {}
