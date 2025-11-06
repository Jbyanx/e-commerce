package com.unimagdalena.inventory_service.message.event;

import java.math.BigDecimal;

public record InventoryReservedEvent(String orderId, String productId, int quantity, BigDecimal totalAmount) {}

