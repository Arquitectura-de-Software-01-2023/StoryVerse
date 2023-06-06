package com.example.msstory.consumer

import com.example.msstory.bl.LibraryBl
import com.example.msstory.dto.RefreshLibraryDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StoryConsumer @Autowired constructor(private val libraryBl: LibraryBl){
    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(LibraryBl::class.java.name)
    }
    @RabbitListener(queues = ["notification2Queue"])
    fun receiveMessage(refreshLibraryDto: RefreshLibraryDto){
        LOGGER.info("Mensaje recibido: $refreshLibraryDto")
        //Eliminamos
        libraryBl.deleteStoryInLibrary(refreshLibraryDto.userId, refreshLibraryDto.storyId)
        //Guardamos
        libraryBl.saveStoryInLibrary(refreshLibraryDto.userId, refreshLibraryDto.storyId)
    }
}