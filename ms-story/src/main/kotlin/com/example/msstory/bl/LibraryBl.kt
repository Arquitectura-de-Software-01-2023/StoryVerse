package com.example.msstory.bl

import com.example.msstory.dao.Library
import com.example.msstory.dao.storyRepository.LibraryRepository
import com.example.msstory.dao.storyRepository.StoryRepository
import com.example.msstory.dto.*
import com.example.msstory.producer.StoryProducer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LibraryBl @Autowired constructor(
    private val libraryRepository: LibraryRepository,
    private val storyRepository: StoryRepository,
    private val storyProducer: StoryProducer
) {

    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(LibraryBl::class.java.name)
    }

    // Método para guardar un libro en su biblioteca
    fun saveStoryInLibrary(userId: String, storyId: Long): ResponseDto<LibraryDto> {
        LibraryBl.LOGGER.info("Iniciando logica para guardar un libro en su biblioteca.")
        var library: Library? = libraryRepository.findByUserIdAndStoryId(userId, storyId)
        if (library != null) {
            LibraryBl.LOGGER.info("El libro ya estuvo en la biblioteca.")
            library.status = true
            libraryRepository.save(library)
        } else {
            LibraryBl.LOGGER.info("El libro no estuvo en la biblioteca.")
            library = Library(userId = userId, storyId = storyId, date = Date(), status = true)
            libraryRepository.save(library)
        }
        LibraryBl.LOGGER.info("El libro fue guardado en su biblioteca correctamente.")
        return ResponseDto(
            data = LibraryDto(libraryId = library.libraryId,userId = library.userId, storyId = library.storyId, date = library.date, status = library.status),
            message = "Libro guardado en su biblioteca",
            success = true
        )
    }

    // Método para obtener los libros de la biblioteca
    fun getStoriesInLibrary(userId: String): ResponseDto<List<StoryDto>> {
        LibraryBl.LOGGER.info("Iniciando logica para obtener los libros de la biblioteca.")
        val books: List<Library> = libraryRepository.findAll().filter { library -> library.userId == userId }
        val booksDto: List<StoryDto> = books.filter { it.status }
            .map { library ->
                val story = storyRepository.findByStoryId(library.storyId)
                StoryDto(
                    storyId = story.storyId,
                    writerId = story.writerId,
                    categoryId = story.categoryId,
                    title = story.title,
                    urlCover = story.urlCover,
                    description = story.description,
                    audience = story.audience,
                    language = story.language,
                    publicationDate = story.publicationDate,
                    votes = story.votes,
                    status = story.status
                )
            }
        LibraryBl.LOGGER.info("Se obtuvieron los libros de la biblioteca.")
        return ResponseDto(
            data = booksDto,
            message = "Libros encontrados",
            success = true
        )
    }

    // Método para eliminar un libro de la biblioteca
    fun deleteStoryInLibrary(userId: String, storyId: Long): ResponseDto<LibraryDto> {
        LibraryBl.LOGGER.info("Iniciando logica para eliminar un libro de la biblioteca.")
        val library: Library = libraryRepository.findByStoryIdAndUserId(storyId, userId)
        library.status = false
        libraryRepository.save(library)
        LibraryBl.LOGGER.info("El libro fue eliminado de la biblioteca correctamente.")
        return ResponseDto(
            data = LibraryDto(libraryId = library.libraryId,userId = library.userId, storyId = library.storyId, date = library.date, status = library.status),
            message = "Libro eliminado de la biblioteca",
            success = true
        )
    }

    //Metodo para actualizar la vista de la biblioteca
    fun sendRabbit(userId: String, storyId: Long): String{
        LOGGER.info("Iniciando logica para enviar un mensaje a RabbitMQ.")
        return storyProducer.sendMessage(RefreshLibraryDto(storyId = storyId, userId = userId))

    }

    //Método para obtener todos los registros de la tabla Library mediante su storyId
    fun getLibraryByStoryId(storyId: Long): List<LibraryDto> {
        LOGGER.info("Iniciando logica para obtener todos los registros de la tabla Library mediante su storyId.")
        val library: List<Library> =
            libraryRepository.findAll().filter { library -> library.storyId == storyId && library.status }
        val libraryDto: List<LibraryDto> = library.map { library ->
            LibraryDto(
                libraryId = library.libraryId,
                userId = library.userId,
                storyId = library.storyId,
                date = library.date,
                status = library.status
            )
        }
        LOGGER.info("Se obtuvieron todos los registros de la tabla Library mediante su storyId.")
        return libraryDto
    }
}