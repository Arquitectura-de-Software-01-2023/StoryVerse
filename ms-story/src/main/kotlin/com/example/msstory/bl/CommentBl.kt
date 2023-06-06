package com.example.msstory.bl

import com.example.msstory.dao.Comment
import com.example.msstory.dao.storyRepository.CommentRepository
import com.example.msstory.dto.CommentDto
import com.example.msstory.dto.ResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentBl @Autowired constructor(private val commentRepository: CommentRepository) {

    // Logger
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(CommentBl::class.java.name)
    }

    // Método para crear un comentario en un capitulo
    fun createComment(commentDto: CommentDto): ResponseDto<CommentDto> {
        CommentBl.LOGGER.info("Iniciando logica para crear un comentario en un capitulo.")
        val comment = Comment()
        comment.userId = commentDto.userId
        comment.chapterId = commentDto.chapterId
        comment.description = commentDto.description
        comment.date = Date()
        comment.status = true
        commentRepository.save(comment)
        CommentBl.LOGGER.info("El comentario fue creado y guardado en la base de datos correctamente.")
        return ResponseDto(
            data = commentDto,
            message = "Comentario creado",
            success = true
        )
    }
    // Método para obtener un comentario por su id
    fun getComment(commentId: Long): ResponseDto<CommentDto>{
        CommentBl.LOGGER.info("Iniciando logica para obtener un comentario por su id.")
        val comment = commentRepository.findByCommentId(commentId)
        val commentDto = CommentDto(
                commentId = comment.commentId,
                userId = comment.userId,
                chapterId = comment.chapterId,
                description = comment.description,
                date = comment.date,
                status = comment.status
            )
        return ResponseDto(
                data = commentDto,
                message = "Comentario encontrado",
                success = true
            )
    }

    // Método para obtener los comentarios de un capítulo
    fun getComments(chapterId: Long): ResponseDto<List<CommentDto>> {
        CommentBl.LOGGER.info("Iniciando logica para obtener los comentarios de un capítulo.")
        val comments: List<Comment> = commentRepository.findAll().filter { comment -> comment.chapterId.toLong() == chapterId }
        val commentsDto: List<CommentDto> = comments.filter { it.status }
            .map { comment ->
                CommentDto(
                    commentId = comment.commentId,
                    userId = comment.userId,
                    chapterId = comment.chapterId,
                    description = comment.description,
                    date = comment.date,
                    status = comment.status
                )
            }
        CommentBl.LOGGER.info("Se obtuvieron los comentarios del capítulo.")
        return ResponseDto(
            data = commentsDto,
            message = "Comentarios encontrados",
            success = true
        )
    }

    // Método para eliminar un comentario
    fun deleteComment(commentId: Long): ResponseDto<CommentDto> {
        CommentBl.LOGGER.info("Iniciando logica para eliminar un comentario.")
        val comment = commentRepository.findByCommentId(commentId)
        comment.status = false
        commentRepository.save(comment)
        CommentBl.LOGGER.info("El comentario fue eliminado correctamente.")
        return ResponseDto(
            data = CommentDto(
                commentId = comment.commentId,
                userId = comment.userId,
                chapterId = comment.chapterId,
                description = comment.description,
                date = comment.date,
                status = comment.status
            ),
            message = "Comentario eliminado",
            success = true
        )
    }

}