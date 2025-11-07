package com.payment.payment_service.infrastructure.messaging.listener;

import com.payment.payment_service.config.RabbitKeys;
import com.payment.payment_service.config.RabbitMQConfig;
import com.payment.payment_service.domain.model.Payment;
import com.payment.payment_service.application.dto.ProcessPaymentCommand;
import com.payment.payment_service.application.service.PaymentService;
import com.payment.payment_service.infrastructure.messaging.publisher.PaymentEventPublisher;

import com.payment.payment_service.domain.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCommandListener {

    private final PaymentService paymentService;
    private final PaymentEventPublisher eventPublisher;

    @RabbitListener(queues = RabbitKeys.PAYMENT_EVENTS_QUEUE)
    public void handleProcessPaymentCommand(ProcessPaymentCommand command) {
        log.info("Received ProcessPaymentCommand for order: {}", command.getOrderId());

        try {
            Payment payment = paymentService.processPayment(command);

            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                eventPublisher.publishPaymentCompleted(payment);
            } else {
                eventPublisher.publishPaymentFailed(payment);
            }

        } catch (Exception e) {
            log.error("Error processing payment for order: {}", command.getOrderId(), e);

            eventPublisher.publishPaymentFailedWithError(
                    command.getOrderId(), 
                    command.getAmount(), 
                    "System error: " + e.getMessage()
            );
        }
    }
}
