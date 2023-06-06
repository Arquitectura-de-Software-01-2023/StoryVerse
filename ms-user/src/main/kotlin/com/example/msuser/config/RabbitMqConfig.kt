package com.example.msuser.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig {
    @Bean
    fun notification2Exchange(): DirectExchange {
        return DirectExchange("notification2Exchange")
    }

    @Bean
    fun notification2Queue(): Queue {
        return QueueBuilder.durable("notification2Queue").build()
    }

    @Bean
    fun notification2Binding(notification2Queue: Queue, notification2Exchange: DirectExchange): Binding {
        return BindingBuilder
            .bind(notification2Queue)
            .to(notification2Exchange)
            .with("notification2RoutingKey")
    }

    @Bean
    fun converter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun amqpTemplate(connectionFactory: ConnectionFactory): AmqpTemplate{
        val amqpTemplate = RabbitTemplate(connectionFactory)
        amqpTemplate.messageConverter = converter()
        return amqpTemplate
    }

}