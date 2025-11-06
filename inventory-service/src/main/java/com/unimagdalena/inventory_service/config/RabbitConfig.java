package com.unimagdalena.inventory_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String RESERVE_QUEUE = "inventory.reserve.queue";
    public static final String RELEASE_QUEUE = "inventory.release.queue";

    @Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Queue reserveQueue() {
        return new Queue(RESERVE_QUEUE, true);
    }

    @Bean
    public Queue releaseQueue() {
        return new Queue(RELEASE_QUEUE, true);
    }

    @Bean
    public Binding reserveBinding() {
        return BindingBuilder.bind(reserveQueue())
                .to(inventoryExchange())
                .with("inventory.reserve");
    }

    @Bean
    public Binding releaseBinding() {
        return BindingBuilder.bind(releaseQueue())
                .to(inventoryExchange())
                .with("inventory.release");
    }
}