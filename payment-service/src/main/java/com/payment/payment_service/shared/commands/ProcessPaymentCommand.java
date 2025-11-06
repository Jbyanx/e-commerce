public record ProcessPaymentCommand(
    String orderId,
    java.math.BigDecimal amount
) {}