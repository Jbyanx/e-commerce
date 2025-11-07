package com.unimagdalena.order_service.services.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.unimagdalena.order_service.services.config.RabbitKeys.*;


@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    // === INVENTORY ===
    @Bean
    public Queue inventoryEventsQueue() {
        return new Queue(INVENTORY_EVENTS_QUEUE, true);
    }

    @Bean
    public Binding inventoryReservedBinding(TopicExchange orderExchange) {
        return BindingBuilder.bind(inventoryEventsQueue())
                .to(orderExchange)
                .with(INVENTORY_RESERVED);
    }

    @Bean
    public Binding inventoryRejectedBinding(TopicExchange orderExchange) {
        return BindingBuilder.bind(inventoryEventsQueue())
                .to(orderExchange)
                .with(INVENTORY_REJECTED);
    }

    // === PAYMENT ===
    @Bean
    public Queue paymentEventsQueue() {
        return new Queue(PAYMENT_EVENTS_QUEUE, true);
    }

    @Bean
    public Binding paymentCompletedBinding(TopicExchange orderExchange) {
        return BindingBuilder.bind(paymentEventsQueue())
                .to(orderExchange)
                .with(PAYMENT_COMPLETED);
    }

    @Bean
    public Queue paymentEventsFailedQueue() {
        return new Queue(PAYMENT_EVENTS_FAILED_QUEUE, true);
    }

    @Bean
    public Binding paymentFailedBinding(TopicExchange orderExchange) {
        return BindingBuilder.bind(paymentEventsFailedQueue())
                .to(orderExchange)
                .with(PAYMENT_FAILED);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
