package com.example.msstory.producer

import com.example.msstory.dto.RefreshLibraryDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StoryProducer @Autowired constructor(private val amqpTemplate: AmqpTemplate) {
    companion object{
        val LOGGER: Logger = LoggerFactory.getLogger(StoryProducer::class.java)
    }

    fun sendMessage(refreshLibraryDto: RefreshLibraryDto): String{
        val response = amqpTemplate.convertAndSend("notification2Exchange",
            "notification2RoutingKey",
            refreshLibraryDto)
        LOGGER.info("Mensaje enviado: $response")
        return "Mensaje enviado: ${response.toString()}"
    }
}