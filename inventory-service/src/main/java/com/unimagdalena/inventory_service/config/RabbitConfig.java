package com.unimagdalena.inventory_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.unimagdalena.inventory_service.config.RabbitKeys.*;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue reserveQueue() {
        return new Queue(INVENTORY_RESERVE_QUEUE, true);
    }

    @Bean
    public Queue releaseQueue() {
        return new Queue(INVENTORY_RELEASE_QUEUE, true);
    }

    @Bean
    public Binding reserveBinding() {
        return BindingBuilder.bind(reserveQueue())
                .to(orderExchange())
                .with(INVENTORY_RESERVE);
    }

    @Bean
    public Binding releaseBinding() {
        return BindingBuilder.bind(releaseQueue())
                .to(orderExchange())
                .with(INVENTORY_RELEASE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

}
