package com.unimagdalena.order_service.comandos;

public record ProcessPaymentCommand(String orderId, long amount) {}
