package com.payment.payment_service.application.service;

import com.payment.payment_service.application.dto.ProcessPaymentCommand;
import com.payment.payment_service.domain.model.Payment;
import com.payment.payment_service.domain.model.PaymentStatus;
import com.payment.payment_service.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final Random random = new Random();

    @Transactional
    public Payment processPayment(ProcessPaymentCommand command) {
        log.info("Processing payment for order: {}, amount: {}", 
                command.getOrderId(), command.getAmount());

        // Simular procesamiento de pago
        boolean isSuccessful = simulatePaymentProcessing(command.getAmount());

        Payment payment;
        if (isSuccessful) {
            payment = Payment.builder()
                    .orderId(command.getOrderId())
                    .amount(command.getAmount())
                    .status(PaymentStatus.SUCCESS)
                    .build();
            
            log.info("Payment successful for order: {}", command.getOrderId());
        } else {
            payment = Payment.builder()
                    .orderId(command.getOrderId())
                    .amount(command.getAmount())
                    .status(PaymentStatus.FAILED)
                    .failureReason("Insufficient funds or payment gateway error")
                    .build();
            
            log.warn("Payment failed for order: {}", command.getOrderId());
        }

        return paymentRepository.save(payment);
    }

    /**
     * Simula el procesamiento de un pago.
     * Reglas:
     * - Si el monto es mayor a 10000, el pago falla
     * - Si el monto es menor o igual a 1000, hay un 95% de probabilidad de éxito
     */
    private boolean simulatePaymentProcessing(BigDecimal amount) {
        try {
            // Simular latencia de procesamiento
            Thread.sleep(1000 + random.nextInt(2000)); // 1-3 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Lógica de negocio simulada
        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            log.debug("Payment rejected: amount exceeds limit");
            return false;
        }

        // 95% de probabilidad de éxito para montos <= 10000
        return random.nextDouble() < 0.95;
    }
}
