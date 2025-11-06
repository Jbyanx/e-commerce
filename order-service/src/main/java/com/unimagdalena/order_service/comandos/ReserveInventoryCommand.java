package com.unimagdalena.order_service.comandos;

public record ReserveInventoryCommand(String orderId, String productId, int quantity) {}
