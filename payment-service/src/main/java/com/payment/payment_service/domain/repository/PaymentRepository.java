package com.ecommerce.payment.domain.repository;

import com.ecommerce.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findAllByOrderId(String orderId);
}
