package com.example.imagesearchformal.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange imageSearchExchange(ImageSearchProperties properties) {
        return new DirectExchange(properties.getMq().getExchange(), true, false);
    }

    @Bean
    public Queue imageSearchQueue(ImageSearchProperties properties) {
        return QueueBuilder.durable(properties.getMq().getQueue()).build();
    }

    @Bean
    public Binding imageSearchBinding(Queue imageSearchQueue,
                                      DirectExchange imageSearchExchange,
                                      ImageSearchProperties properties) {
        return BindingBuilder.bind(imageSearchQueue)
                .to(imageSearchExchange)
                .with(properties.getMq().getRoutingKey());
    }
}
