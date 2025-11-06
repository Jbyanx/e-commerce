package com.unimagdalena.inventory_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";

    @Bean
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE, true);
    }

    @Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with("inventory.key");
    }
}

