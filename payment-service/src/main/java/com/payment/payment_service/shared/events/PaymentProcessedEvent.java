public record PaymentProcessedEvent(
    String orderId,
    java.math.BigDecimal amount,
    PaymentStatus status
) {}