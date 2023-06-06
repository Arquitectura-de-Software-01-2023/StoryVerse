package com.example.msstory.bl

import com.example.msstory.dao.Chapter
import com.example.msstory.dao.storyRepository.ChapterRepository
import com.example.msstory.dto.ChapterDto
import com.example.msstory.dto.ResponseDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChapterBl @Autowired constructor(private val chapterRepository: ChapterRepository) {

    // Logger
    companion object {
        val objectMapper = jacksonObjectMapper()
        val LOGGER: Logger = LoggerFactory.getLogger(ChapterBl::class.java.name)
    }

    // Método para crear un capítulo
    fun createChapter(chapterDto: ChapterDto): ResponseDto<ChapterDto> {
        ChapterBl.LOGGER.info("Iniciando logica para crear un capítulo.")
        val chapter = Chapter()
        chapter.storyId = chapterDto.storyId
        chapter.title = chapterDto.title
        chapter.description = chapterDto.description
        chapter.publicationDate = Date()
        chapter.status = true
        chapterRepository.save(chapter)
        ChapterBl.LOGGER.info("El capitulo fue creado y guardado en la base de datos correctamente.")
        return ResponseDto(
            data = chapterDto,
            message = "Capitulo creado",
            success = true
        )
    }

    // Método para obtener los capítulos mediante el storyId
    fun getChapters(storyId: Long): ResponseDto<List<ChapterDto>> {
        ChapterBl.LOGGER.info("Iniciando logica para obtener los capitulos mediante el storyId.")
        val chapters: List<Chapter> = chapterRepository.findAll().filter { chapter -> chapter.storyId.toLong() == storyId }
        val chaptersDto: List<ChapterDto> = chapters.filter { it.status }
            .map { chapter ->
                ChapterDto(
                    chapterId = chapter.chapterId,
                    storyId = chapter.storyId,
                    title = chapter.title,
                    description = chapter.description,
                    publicationDate = chapter.publicationDate,
                    status = chapter.status
                )
            }
        ChapterBl.LOGGER.info("Se obtuvieron los capitulos de la historia mediante el storyId.")
        return ResponseDto(
            data = chaptersDto,
            message = "Capitulos encontrados",
            success = true
        )
    }

    // Método para obtener los capítulos mediante el chapterId
    fun getChapter(chapterId: Long): ResponseDto<ChapterDto> {
        ChapterBl.LOGGER.info("Iniciando logica para obtener los capítulos mediante el chapterId.")
        val chapter: Chapter = chapterRepository.findByChapterId(chapterId)
        val chapterDto: ChapterDto = ChapterDto(
            chapterId = chapter.chapterId,
            storyId = chapter.storyId,
            title = chapter.title,
            description = chapter.description,
            publicationDate = chapter.publicationDate,
            status = chapter.status
        )
        ChapterBl.LOGGER.info("Se obtuvo el capitulo mediante el chapterId.")
        return ResponseDto(
            data = chapterDto,
            message = "Capitulo encontrado",
            success = true
        )
    }

    // Método para actualizar un capítulo
    fun updateChapter(chapterDto: ChapterDto): ResponseDto<ChapterDto> {
        ChapterBl.LOGGER.info("Iniciando logica para actualizar un capítulo.")
        val chapter = chapterRepository.findById(chapterDto.chapterId).orElse(null)
        chapter.description = chapterDto.description
        chapter.title = chapterDto.title
        chapterRepository.save(chapter)
        ChapterBl.LOGGER.info("El capitulo fue actualizado y guardado en la base de datos correctamente.")
        return ResponseDto(
            data = chapterDto,
            message = "Capitulo actualizado",
            success = true
        )
    }

    // Método para eliminar un capítulo - eliminación lógica
    fun deleteChapter(chapterId: Long): ResponseDto<ChapterDto> {
        ChapterBl.LOGGER.info("Iniciando logica para eliminar un capítulo.")
        val chapter = chapterRepository.findById(chapterId).orElse(null)
        chapter.status = false
        val chapterDto = ChapterDto(
            chapterId = chapter.chapterId,
            storyId = chapter.storyId,
            title = chapter.title,
            description = chapter.description,
            publicationDate = chapter.publicationDate,
            status = chapter.status
        )
        chapterRepository.save(chapter)
        ChapterBl.LOGGER.info("El capitulo fue eliminado y guardado en la base de datos correctamente.")
        return ResponseDto(
            data = chapterDto,
            message = "Capitulo eliminado",
            success = true
        )
    }

}