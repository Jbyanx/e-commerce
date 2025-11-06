package com.unimagdalena.order_service.services.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";

    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String PAYMENT_QUEUE = "payment.queue";

    public static final String RESERVE_INVENTORY = "inventory.reserve";
    public static final String PROCESS_PAYMENT = "payment.process";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE, true);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public Binding bindInventory() {
        return BindingBuilder.bind(inventoryQueue()).to(orderExchange()).with(RESERVE_INVENTORY);
    }

    @Bean
    public Binding bindPayment() {
        return BindingBuilder.bind(paymentQueue()).to(orderExchange()).with(PROCESS_PAYMENT);
    }
}

