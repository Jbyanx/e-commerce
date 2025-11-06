package com.unimagdalena.order_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unimagdalena.order_service.entities.Order;

public interface OrderRepository extends JpaRepository<Order, String>{}
