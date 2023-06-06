package com.example.msstory.bl

import com.example.msstory.dao.StoryTag
import com.example.msstory.dao.Tag
import com.example.msstory.dao.storyRepository.StoryTagRepository
import com.example.msstory.dao.storyRepository.TagRepository
import com.example.msstory.dto.ResponseDto
import com.example.msstory.dto.StoryTagDto
import com.example.msstory.dto.TagDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagBl @Autowired constructor(
    private val tagRepository: TagRepository,
    private val storyTagRepository: StoryTagRepository
) {

    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(TagBl::class.java.name)
    }

    // =============================================================================================
    // CREAR TAGS
    // Método para crear un story_tag (la relación entre el tag y la historia)
    fun createStoryTags(tags: List<String>, storyId: Long): ResponseDto<List<StoryTagDto>> {
        LOGGER.info("Iniciando creación de story_tag")
        val storyTags = mutableListOf<StoryTagDto>()
        tags.forEach { tag ->
            val tagId = this.createTag(tag)
            val storyTag = StoryTag(storyId = storyId, tagId = tagId, status = true)
            storyTagRepository.save(storyTag)
            storyTags.add(StoryTagDto(
                storyTagId = storyTag.storyTagId,
                storyId = storyTag.storyId,
                tagId = storyTag.tagId,
                status = storyTag.status
            ))
        }
        return ResponseDto(
            data = storyTags,
            message = "Story_tag creado exitosamente",
            success = true
        )
    }

    // Método para crear una tag en la base de datos, si es que no existe
    fun createTag(name: String): Long {
        TagBl.LOGGER.info("Iniciando logica para crear un tag.")
        var tag: Tag? = tagRepository.findByName(name)
        if (tag == null) {
            TagBl.LOGGER.info("El tag no existe, se procede a crearlo.")
            tagRepository.save(Tag(name = name, status = true))
            TagBl.LOGGER.info("El tag fue creado y guardado en la base de datos correctamente.")
        }
        tag = tagRepository.findByName(name)
        if (tag != null) {
            return tag.tagId
        }
        return 0
    }

    // =============================================================================================
    // OBTENER TAGS
    // Método para obtener un tag por su storyId
    fun getStoryTags(storyId: Long): ResponseDto<List<TagDto>> {
        LOGGER.info("Iniciando logica para obtener un tag por su storyId.")
        val storyTags = storyTagRepository.findByStoryId(storyId).filter { it.status }
        val tags = mutableListOf<TagDto>()
        storyTags.forEach { storyTag ->
            val tag = tagRepository.findByTagId(storyTag.tagId)
            tags.add(
                TagDto(
                    tagId = tag.tagId,
                    name = tag.name,
                    status = tag.status
                )
            )
        }
        return ResponseDto(
            data = tags,
            message = "Tags obtenidos exitosamente",
            success = true
        )
    }

    // =============================================================================================
    // ACTUALIZAR TAGS
    // Método para actualizar las tags de una historia
    fun updateStoryTags(tags: List<String>, storyId: Long): ResponseDto<List<StoryTagDto>> {
        TagBl.LOGGER.info("Iniciando logica para actualizar los tags de una historia.")
        this.desactivateStoryTags(storyId)
        val storyTags = mutableListOf<StoryTagDto>()
        tags.forEach { tag ->
            val tagId = this.createTag(tag)
            var storyTag = storyTagRepository.findByStoryIdAndTagId(storyId, tagId)
            if (storyTag == null) {
                storyTag = StoryTag(storyId = storyId, tagId = tagId, status = true)
                storyTagRepository.save(storyTag)
            } else {
                storyTag.status = true
                storyTagRepository.save(storyTag)
            }
            storyTags.add(StoryTagDto(
                storyTagId = storyTag.storyTagId,
                storyId = storyTag.storyId,
                tagId = storyTag.tagId,
                status = storyTag.status
            ))
        }
        TagBl.LOGGER.info("Los tags de la historia fueron actualizados correctamente.")
        return ResponseDto(
            data = storyTags,
            message = "Tags actualizados exitosamente",
            success = true
        )
    }

    // Método para desactivar todos los tags que están relacionados con la historia
    fun desactivateStoryTags(storyId: Long) {
        TagBl.LOGGER.info("Iniciando logica para desactivar los tags relacionados con la historia.")
        val storyTags = storyTagRepository.findByStoryId(storyId)
        storyTags.forEach { storyTag ->
            storyTag.status = false
            storyTagRepository.save(storyTag)
        }
        TagBl.LOGGER.info("Los tags relacionados con la historia fueron desactivados correctamente.")
    }
}