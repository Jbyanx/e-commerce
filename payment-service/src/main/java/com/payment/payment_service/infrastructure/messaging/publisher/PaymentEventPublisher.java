package com.payment.payment_service.infrastructure.messaging.publisher;



import com.payment.payment_service.application.dto.PaymentCompletedEvent;
import com.payment.payment_service.application.dto.PaymentFailedEvent;
import com.payment.payment_service.config.RabbitKeys;
import com.payment.payment_service.config.RabbitMQConfig;
import com.payment.payment_service.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishPaymentCompleted(com.payment.payment_service.domain.model.Payment payment) {
        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .paymentId(payment.getId().toString())
                .completedAt(LocalDateTime.now())
                .build();

        log.info("Publishing PaymentCompletedEvent for order: {}", payment.getOrderId());
        
        rabbitTemplate.convertAndSend(
                RabbitKeys.ORDER_EXCHANGE,
                RabbitKeys.PAYMENT_COMPLETED,
                event
        );
    }

    public void publishPaymentFailed(Payment payment) {
        PaymentFailedEvent event = PaymentFailedEvent.builder()
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .reason(payment.getFailureReason())
                .failedAt(LocalDateTime.now())
                .build();

        log.info("Publishing PaymentFailedEvent for order: {}", payment.getOrderId());
        
        rabbitTemplate.convertAndSend(
                RabbitKeys.ORDER_EXCHANGE,
                RabbitKeys.PAYMENT_FAILED,
                event
        );
    }

    public void publishPaymentFailedWithError(String orderId, BigDecimal amount, String reason) {
        PaymentFailedEvent event = PaymentFailedEvent.builder()
                .orderId(orderId)
                .amount(amount)
                .reason(reason)
                .failedAt(LocalDateTime.now())
                .build();

        log.info("Publishing PaymentFailedEvent for order: {} (system error)", orderId);
        
        rabbitTemplate.convertAndSend(
                RabbitKeys.ORDER_EXCHANGE,
                RabbitKeys.PAYMENT_FAILED,
                event
        );
    }
}
