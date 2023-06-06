package com.example.msuser.producer

import com.example.msuser.dto.NotificationDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotificationProducer @Autowired constructor(private val amqpTemplate: AmqpTemplate) {
    companion object{
        val LOGGER: Logger = LoggerFactory.getLogger(NotificationProducer::class.java)
    }

    fun sendNotification(notificationDto: NotificationDto): String{
        val response = amqpTemplate.convertAndSend("notification2Exchange",
            "notification2RoutingKey",
            notificationDto)
        LOGGER.info("Mensaje enviado: $response")
        return "Mensaje enviado: ${response.toString()}"
    }
}