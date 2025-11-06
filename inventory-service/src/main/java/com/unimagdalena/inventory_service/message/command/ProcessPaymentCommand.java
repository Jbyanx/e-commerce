package com.unimagdalena.inventory_service.message.command;

import java.math.BigDecimal;

public record ProcessPaymentCommand(
        String orderId,
        BigDecimal amount
) {
}
