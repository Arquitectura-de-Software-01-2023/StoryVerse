package com.example.msstory.bl

import com.example.msstory.dao.Story
import com.example.msstory.dao.Writer
import com.example.msstory.dao.storyRepository.WriterRepository
import com.example.msstory.dto.ResponseDto
import com.example.msstory.dto.StoryDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class WriterBl @Autowired constructor(private val writerRepository: WriterRepository) {

    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(WriterBl::class.java.name)
    }

    // Método para obtener el writerId del usuario, en caso de que no exista crearlo
    fun getWriterId(userId: String): Long {
        WriterBl.LOGGER.info("Iniciando logica para obtener el writerId del usuario.")
        val writer = writerRepository.findByUserId(userId)
        return if (writer != null) {
            WriterBl.LOGGER.info("El escritor ya existe.")
            writer.writerId
        } else {
            WriterBl.LOGGER.info("El escritor no existe, se procede a crearlo.")
            val newWriter = Writer()
            newWriter.userId = userId
            newWriter.date = Date()
            newWriter.status = true
            writerRepository.save(newWriter)
            WriterBl.LOGGER.info("El escritor fue creado y guardado en la base de datos correctamente.")
            newWriter.writerId
        }
    }

    // Método para eliminar un escritor - eliminación logica
    fun deleteWriter(userId: String): String {
        WriterBl.LOGGER.info("Iniciando logica para eliminar un escritor.")
        val writer = writerRepository.findByUserId(userId)
        return if (writer != null) {
            WriterBl.LOGGER.info("El escritor existe.")
            writer.status = false
            writerRepository.save(writer)
            WriterBl.LOGGER.info("El escritor fue eliminado correctamente.")
            "El escritor fue eliminado correctamente."
        } else {
            WriterBl.LOGGER.info("El escritor no existe.")
            "El escritor no existe."
        }
    }
    fun getWriterIdWithoutCreating(userId: String): Long {
        WriterBl.LOGGER.info("Iniciando logica para obtener el writerId del usuario.")
        val writer = writerRepository.findByUserId(userId)
        return if (writer != null) {
            WriterBl.LOGGER.info("El escritor ya existe.")
            writer.writerId
        } else {
            return 0
        }
    }

    // Método para obtener el userId del escritor
    fun getUserId(writerId: Long): ResponseDto<String>{
        WriterBl.LOGGER.info("Iniciando logica para obtener el userId del escritor.")
        val writer = writerRepository.findByWriterId(writerId)
        return if (writer != null) {
            WriterBl.LOGGER.info("El escritor existe.")
            return ResponseDto(
                data = writer.userId,
                message = "El escritor existe.",
                success = true
            )
        } else {
            WriterBl.LOGGER.info("El escritor no existe.")
            return ResponseDto(
                data = "",
                message = "El escritor no existe.",
                success = false
            )
        }
    }

}