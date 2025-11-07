package com.payment.payment_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.payment.payment_service.config.RabbitKeys.*;

@Configuration
public class RabbitMQConfig {

    // Exchange compartido por todos los microservicios
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    // Cola que escucha el comando "payment.process"
    @Bean
    public Queue paymentProcessQueue() {
        return new Queue(PAYMENT_COMMAND_QUEUE, true);
    }

    // Cola donde el order-service recibirá el evento "payment.completed"
    @Bean
    public Queue paymentEventsQueue() {
        return new Queue(PAYMENT_EVENTS_QUEUE, true);
    }

    // Cola donde el order-service recibirá el evento "payment.failed"
    @Bean
    public Queue paymentEventsFailedQueue() {
        return new Queue(PAYMENT_EVENTS_FAILED_QUEUE, true);
    }

    // Binding: order-service envía → payment-service procesa
    @Bean
    public Binding paymentProcessBinding(TopicExchange orderExchange) {
        return BindingBuilder.bind(paymentProcessQueue())
                .to(orderExchange)
                .with(PAYMENT_PROCESS);
    }

    // Bindings inversos (para eventos que publicará payment)
    @Bean
    public Binding paymentCompletedBinding(TopicExchange orderExchange) {
        return BindingBuilder.bind(paymentEventsQueue())
                .to(orderExchange)
                .with(PAYMENT_COMPLETED);
    }

    @Bean
    public Binding paymentFailedBinding(TopicExchange orderExchange) {
        return BindingBuilder.bind(paymentEventsFailedQueue())
                .to(orderExchange)
                .with(PAYMENT_FAILED);
    }

    // Conversor de mensajes a JSON
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
