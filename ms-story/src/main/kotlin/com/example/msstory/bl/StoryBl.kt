package com.example.msstory.bl

import com.example.msstory.dao.Story
import com.example.msstory.dao.storyRepository.CategoryRepository
import com.example.msstory.dao.storyRepository.ListStoryRepository
import com.example.msstory.dao.storyRepository.StoryRepository
import com.example.msstory.dto.CategoryDto
import com.example.msstory.dto.ResponseDto
import com.example.msstory.dto.StoryDto
import com.example.msstory.entity.StoryEntity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class StoryBl @Autowired constructor(
    private val storyRepository: StoryRepository,
    private val listStoryRepository: ListStoryRepository,
    private val categoryRepository: CategoryRepository,
    private val tagBl: TagBl
) {
    // Logger
    companion object {
        val objectMapper = jacksonObjectMapper()
        val LOGGER: Logger = LoggerFactory.getLogger(StoryBl::class.java.name)
    }

    // Método para crear una historia
    fun createStory(storyEntity: StoryEntity): ResponseDto<String>{
        val story = Story()
        story.writerId = storyEntity.writerId
        story.categoryId = storyEntity.categoryId
        story.title = storyEntity.title
        story.urlCover = storyEntity.urlCover
        story.description = storyEntity.description
        story.audience = storyEntity.audience
        story.language = "Español"
        story.publicationDate = Date()
        story.votes = 0
        story.status = true
        storyRepository.save(story)
        StoryBl.LOGGER.info("Historia creada y guardada en la base de datos correctamente.")
        val lastStory = storyRepository.findFirstByOrderByStoryIdDesc();
        tagBl.createStoryTags(storyEntity.tags, lastStory.storyId)
        return ResponseDto(
            data = lastStory.storyId.toString(),
            message = "Historia creada",
            success = true
        )
    }

    // Método para obtener la lista de historias que no hayan sido
    // escritas por el writerId - paginación
    fun listStoryByWriterIdNot(writerId: Long, page: Int, size: Int): ResponseDto<List<StoryDto>> {
        StoryBl.LOGGER.info("Iniciando logica para obtener lista de historias que no fueron escritas por el usuario mediante el writerId.")
        val pageable = PageRequest.of(page, size)
        val listStory = listStoryRepository.findByWriterIdNotAndStatusIsTrue(writerId, pageable)
        val listStoryDto = mutableListOf<StoryDto>()
        listStory.forEach {
            val storyDto = StoryDto(
                storyId = it.storyId,
                writerId = it.writerId,
                categoryId = it.categoryId,
                title = it.title,
                urlCover = it.urlCover,
                description = it.description,
                audience = it.audience,
                language = it.language,
                publicationDate = it.publicationDate,
                votes = it.votes,
                status = it.status
            )
            listStoryDto.add(storyDto)
        }
        StoryBl.LOGGER.info("Lista de historias que no fueron escritas por el usuario obtenida correctamente.")
        return ResponseDto(
            data = listStoryDto,
            message = "Lista de historias que no fueron escritas por el usuario obtenida correctamente.",
            success = true
        )
    }

    // Método para obtener la lista de historias mediante el writerId - paginación
    fun listStoryByWriterId(writerId: Long, page: Int, size: Int): ResponseDto<List<StoryDto>> {
        StoryBl.LOGGER.info("Iniciando logica para obtener lista de historias que fueron escritas por el usuario mediante el writerId.")
        val pageable = PageRequest.of(page, size)
        val listStory = listStoryRepository.findByWriterIdAndStatusIsTrue(writerId, pageable)
        val listStoryDto = mutableListOf<StoryDto>()
        listStory.forEach {
            val storyDto = StoryDto(
                storyId = it.storyId,
                writerId = it.writerId,
                categoryId = it.categoryId,
                title = it.title,
                urlCover = it.urlCover,
                description = it.description,
                audience = it.audience,
                language = it.language,
                publicationDate = it.publicationDate,
                votes = it.votes,
                status = it.status
            )
            listStoryDto.add(storyDto)
        }
        StoryBl.LOGGER.info("Lista de historias que fueron escritas por el usuario obtenida correctamente.")
        return ResponseDto(
            data = listStoryDto,
            message = "Lista de historias que fueron escritas por el usuario obtenida correctamente.",
            success = true
        )
    }

    // Método para obtener una lista de historias que contengan la palabra o conjunto de palabras en el título
    fun listStoryByTitle(title: String, page: Int, size: Int): ResponseDto<List<StoryDto>> {
        StoryBl.LOGGER.info("Iniciando logica para obtener lista de historias que contengan la palabra o conjunto de palabras en el título.")
        val pageable = PageRequest.of(page, size)
        val listStory = listStoryRepository.findByTitleContainingAndStatusIsTrue(title, pageable)
        val listStoryDto = mutableListOf<StoryDto>()
        listStory.forEach {
            val storyDto = StoryDto(
                storyId = it.storyId,
                writerId = it.writerId,
                categoryId = it.categoryId,
                title = it.title,
                urlCover = it.urlCover,
                description = it.description,
                audience = it.audience,
                language = it.language,
                publicationDate = it.publicationDate,
                votes = it.votes,
                status = it.status
            )
            listStoryDto.add(storyDto)
        }
        StoryBl.LOGGER.info("Lista de historias que contengan la palabra o conjunto de palabras en el título obtenida correctamente.")
        return ResponseDto(
            data = listStoryDto,
            message = "Lista de historias que contengan la palabra o conjunto de palabras en el título obtenida correctamente.",
            success = true
        )
    }

    // Método para obtener la lista de historias mediante el categoryId - paginación
    fun listStoryByCategoryId(categoryId: Long, page: Int, size: Int): ResponseDto<List<StoryDto>> {
        StoryBl.LOGGER.info("Iniciando logica para obtener lista de historias mediante el categoryId.")
        val pageable = PageRequest.of(page, size)
        val listStory = listStoryRepository.findByCategoryIdAndStatusIsTrue(categoryId, pageable)
        val listStoryDto = mutableListOf<StoryDto>()
        listStory.forEach {
            val storyDto = StoryDto(
                storyId = it.storyId,
                writerId = it.writerId,
                categoryId = it.categoryId,
                title = it.title,
                urlCover = it.urlCover,
                description = it.description,
                audience = it.audience,
                language = it.language,
                publicationDate = it.publicationDate,
                votes = it.votes,
                status = it.status
            )
            listStoryDto.add(storyDto)
        }
        StoryBl.LOGGER.info("Lista de historias obtenida correctamente.")
        return ResponseDto(
            data = listStoryDto,
            message = "Lista de historias obtenida correctamente.",
            success = true
        )
    }

    // Método para obtener una historia mediante el storyId
    fun getStory(storyId: Long): ResponseDto<StoryDto> {
        StoryBl.LOGGER.info("Iniciando logica para obtener una historia mediante el storyId.")
        val story: Story = storyRepository.findByStoryId(storyId)
        if (story == null) {
            StoryBl.LOGGER.error("No se encontró la historia.")
        }
        val storyDto: StoryDto = StoryDto(
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
        StoryBl.LOGGER.info("Historia obtenida correctamente.")
        return ResponseDto(
            data = storyDto,
            message = "Historia encontrada",
            success = true
        )
    }

    // Método para actualizar los datos de una historia mediante su id
    fun updateStory(storyDto: StoryDto): ResponseDto<StoryDto> {
        StoryBl.LOGGER.info("Iniciando logica para actualizar los datos de una historia mediante su id.")
        val story = storyRepository.findById(storyDto.storyId).orElse(null)
        story.categoryId = storyDto.categoryId
        story.title = storyDto.title
        story.urlCover = storyDto.urlCover
        story.description = storyDto.description
        story.audience = storyDto.audience
        storyRepository.save(story)
        StoryBl.LOGGER.info("Historia actualizada correctamente.")
        return ResponseDto(
            data = storyDto,
            message = "Historia actualizada",
            success = true
        )
    }

    // Método para aumentar el número de votos de una historia
    fun updateVotes(storyId: Long): ResponseDto<StoryDto> {
        StoryBl.LOGGER.info("Iniciando logica para aumentar el número de votos de una historia.")
        val story = storyRepository.findById(storyId).orElse(null)
        story.votes = story.votes + 1
        storyRepository.save(story)
        StoryBl.LOGGER.info("Número de votos de la historia aumentado correctamente.")
        return ResponseDto(
            data = StoryDto(
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
            ),
            message = "Número de votos de la historia aumentado",
            success = true
        )
    }

    // Método para eliminar una historia - eliminación lógica
    fun deleteStory(storyId: Long): ResponseDto<StoryDto> {
        StoryBl.LOGGER.info("Iniciando logica para eliminar una historia mediante su id.")
        val story = storyRepository.findById(storyId).orElse(null)
        story.status = false
        storyRepository.save(story)
        val storyDto = StoryDto(
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
        StoryBl.LOGGER.info("Historia eliminada correctamente.")
        return ResponseDto(
            data = storyDto,
            message = "Historia eliminada",
            success = true
        )
    }

    // Método para obtener todas las categorias
    fun getCategories(): ResponseDto<List<CategoryDto>> {
        StoryBl.LOGGER.info("Iniciando logica para obtener todas las categorias.")
        val categories = categoryRepository.findAll()
        val categoriesDto = categories.map {
            CategoryDto(
                categoryId = it.categoryId,
                name = it.name,
                status = it.status
            )
        }
        StoryBl.LOGGER.info("Categorias obtenidas correctamente.")
        return ResponseDto(
            data = categoriesDto,
            message = "Categorias encontradas",
            success = true
        )
    }

    //Metodo para obtener la cantidad de historias por categoria
    fun getCategoryCount(category: Long): Int {
        StoryBl.LOGGER.info("Iniciando logica para obtener la cantidad de historias por categoria.")
        val count = storyRepository.findByCategoryIdAndStatus(category, true).size
        StoryBl.LOGGER.info("Cantidad de historias por categoria obtenida correctamente.")
        return count;
    }
}