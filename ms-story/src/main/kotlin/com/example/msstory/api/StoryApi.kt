package com.example.msstory.api

import com.example.msstory.bl.*
import com.example.msstory.dto.*
import com.example.msstory.entity.StoryEntity
import com.example.msstory.service.UserService
import io.github.resilience4j.bulkhead.BulkheadFullException
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/story")
class StoryApi  @Autowired constructor(
    private val storyBl: StoryBl,
    private val chapterBl: ChapterBl,
    private val commentBl: CommentBl,
    private val writerBl: WriterBl,
    private val tagBl: TagBl,
    private val libraryBl: LibraryBl,
    private val userService: UserService
) {

    // Logger
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(StoryApi::class.java)
    }

    // =============================================================================================
    // HISTORIA
    /**
     * Endpoint para publicar una historia
     * @param story
     * @return ResponseDto<StoryDto>
     * Se necesita token de autorización
     */
    @PostMapping("/new")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "createStoryFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "createStoryFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "createStoryFallback")
    @Retry(name = "storyRT", fallbackMethod = "createStoryFallback")
    fun createStory(@RequestHeader headers: Map<String, String>, @RequestBody storyEntity: StoryEntity): ResponseDto<String>{
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterId(userId)
        LOGGER.info("Inicio de creacion de una historia.")
        //Reemplazamos el writerId
        storyEntity.writerId = writerId
        return storyBl.createStory(storyEntity)
    }

    /**
     * Endpoint para obtener la lista de historias - paginación
     * @param headers, page, size
     * @return ResponseDto<List<StoryDto>>
     * Se necesita token de autorización - Página de inicio
     */
    @GetMapping("/list")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "listStoryFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "listStoryFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "listStoryFallback")
    @Retry(name = "storyRT", fallbackMethod = "listStoryFallback")
    fun listStory(@RequestHeader headers: Map<String, String>, @RequestParam page: Int, @RequestParam size: Int): ResponseDto<List<StoryDto>> {
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterIdWithoutCreating(userId)
        LOGGER.info("Obteniendo historias.")
        return storyBl.listStoryByWriterIdNot(writerId, page, size)
    }

    /**
     * Endpoint para obtener la lista de historias de un usuario por su username - paginación
     * @param headers, username, page, size
     * @return ResponseDto<List<StoryDto>>
    */
    @GetMapping("/{username}")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "listStoriesByUsernameFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "listStoriesByUsernameFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "listStoriesByUsernameFallback")
    @Retry(name = "storyRT", fallbackMethod = "listStoriesByUsernameFallback")
    fun listStoriesByUsername(@RequestHeader headers: Map<String, String>, @PathVariable username: String, @RequestParam page: Int, @RequestParam size: Int): ResponseDto<List<StoryDto>> {
        LOGGER.info("Obteniendo las historias del usuario $username")
        val userDto = userService.getUserByUsername(username, headers).data
        val writerId = writerBl.getWriterIdWithoutCreating(userDto.sub)
        return storyBl.listStoryByWriterId(writerId, page, size)
    }


    /**
     * Endpoint para obtener la lista de historias por título - paginación
     * @param title, page, size
     * @return Any
     */
    @GetMapping("/list/title")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "listStoryByTitleFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "listStoryByTitleFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "listStoryByTitleFallback")
    @Retry(name = "storyRT", fallbackMethod = "listStoryByTitleFallback")
    fun listStoryByTitle(@RequestParam title: String, @RequestParam page: Int, @RequestParam size: Int): ResponseDto<List<StoryDto>> {
        LOGGER.info("Obteniendo historias por titulo.")
        return storyBl.listStoryByTitle(title, page, size)
    }

    /**
     * Endpoint para obtener la lista de historias por categoría - paginación
     * @param categoryId, page, size
     * @return Any
     */
    @GetMapping("/list/category")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "listStoryByCategoryFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "listStoryByCategoryFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "listStoryByCategoryFallback")
    @Retry(name = "storyRT", fallbackMethod = "listStoryByCategoryFallback")
    fun listStoryByCategory(@RequestParam categoryId: Long, @RequestParam page: Int, @RequestParam size: Int): ResponseDto<List<StoryDto>> {
        LOGGER.info("Obteniendo historias por categoria.")
        return storyBl.listStoryByCategoryId(categoryId, page, size)
    }

    /**
     * Endpoint para obtener datos de una historia
     * @param storyId
     * @return ResponseDto<StoryDto>
     */
    @GetMapping("")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "getStoryFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "getStoryFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "getStoryFallback")
    @Retry(name = "storyRT", fallbackMethod = "getStoryFallback")
    fun getStory(@RequestParam storyId: Long): ResponseDto<StoryDto>{
        LOGGER.info("Obteniendo los datos de una historia.")
        return storyBl.getStory(storyId)
    }

    /**
     * Endpoint para actualizar una historia
     * @param story
     * @return ResponseDto<StoryDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol user
     */
    @PutMapping("")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "updateStoryFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "updateStoryFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "updateStoryFallback")
    @Retry(name = "storyRT", fallbackMethod = "updateStoryFallback")
    fun updateStory(@RequestHeader headers: Map<String, String>, @RequestBody story: StoryDto): ResponseDto<StoryDto>{
        LOGGER.info("Inicio de actualizacion de una historia.")
        //Verificamos que el usuario que quiere actualizar la historia sea el mismo que la creo
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterIdWithoutCreating(userId)
        if(story.writerId != writerId){
            throw Exception("No tienes permisos para actualizar esta historia.")
        }
        return storyBl.updateStory(story)
    }

    /**
     * Endpoint para aumentar el número de votos de una historia
     * @param storyId
     * @return ResponseDto<StoryDto>
     * Se necesita token de autorización
     */
    @PutMapping("/vote")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "voteStoryFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "voteStoryFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "voteStoryFallback")
    @Retry(name = "storyRT", fallbackMethod = "voteStoryFallback")
    fun voteStory(@RequestHeader headers: Map<String, String>,@RequestParam storyId: Long): ResponseDto<StoryDto>{
        //Verificamos que el usuario está autenticado
        try{
            val userDto = userService.getUser(headers).data
            LOGGER.info("Inicio de votacion de una historia.")
            return storyBl.updateVotes(storyId)
        }catch (e: Exception){
            throw Exception("No tienes permisos para votar esta historia.")
        }
    }

    /**
     * Endpoint para eliminar una historia - eliminar lógico
     * @param storyId
     * @return ResponseDto<StoryDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol user
     */
    @DeleteMapping("")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "deleteStoryFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "deleteStoryFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "deleteStoryFallback")
    @Retry(name = "storyRT", fallbackMethod = "deleteStoryFallback")
    fun deleteStory(@RequestHeader headers: Map<String, String>, @RequestParam storyId: Long): ResponseDto<StoryDto>{
        LOGGER.info("Inicio de eliminacion logica de una historia.")
        //Verificamos que el usuario que quiere eliminar la historia sea el mismo que la creo
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterIdWithoutCreating(userId)
        val storyDto = storyBl.getStory(storyId).data
        if(storyDto.writerId != writerId){
            return ResponseDto(storyDto, "No puedes eliminar una historia que no es tuya", false)
        }
        return storyBl.deleteStory(storyId)
    }

    // =============================================================================================
    // CAPITULO
    /**
     * Endpoint para publicar un capítulo
     * @param chapter
     * @return ResponseDto<ChapterDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol user
     */
    @PostMapping("/chapter/new")
    @CircuitBreaker(name = "chapterCB", fallbackMethod = "createChapterFallback")
    @Bulkhead(name = "chapterBH", fallbackMethod = "createChapterFallback")
    @RateLimiter(name = "chapterRL", fallbackMethod = "createChapterFallback")
    @Retry(name = "chapterRT", fallbackMethod = "createChapterFallback")
    fun createChapter(@RequestHeader headers: Map<String, String>, @RequestBody chapter: ChapterDto): ResponseDto<ChapterDto>{
        LOGGER.info("Inicio de creacion de un capitulo.")
        //Verificamos que el usuario que quiere crear el capitulo sea el mismo que la creo la historia
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterIdWithoutCreating(userId)
        val storyDto = storyBl.getStory(chapter.storyId).data
        if(storyDto.writerId != writerId){
            throw Exception ("No puedes crear un capitulo en una historia que no es tuya")
        }
        return chapterBl.createChapter(chapter)
    }

    /**
     * Endpoint para obtener los capítulos de una historia
     * @param storyId
     * @return ResponseDto<List<ChapterDto>>
     */
    @GetMapping("/{storyId}/chapter/list")
    @CircuitBreaker(name = "chapterCB", fallbackMethod = "getChaptersFallback")
    @Bulkhead(name = "chapterBH", fallbackMethod = "getChaptersFallback")
    @RateLimiter(name = "chapterRL", fallbackMethod = "getChaptersFallback")
    @Retry(name = "chapterRT", fallbackMethod = "getChaptersFallback")
    fun getChapters(@PathVariable storyId: Long): ResponseDto<List<ChapterDto>>{
        LOGGER.info("Inicio de obtencion de los capitulos de la historia.")
        return chapterBl.getChapters(storyId)
    }

    /**
     * Endpoint para obtener datos de un capítulo
     * @param chapterId
     * @return ResponseDto<ChapterDto>
     */
    @GetMapping("/chapter")
    @CircuitBreaker(name = "chapterCB", fallbackMethod = "getChapterFallback")
    @Bulkhead(name = "chapterBH", fallbackMethod = "getChapterFallback")
    @RateLimiter(name = "chapterRL", fallbackMethod = "getChapterFallback")
    @Retry(name = "chapterRT", fallbackMethod = "getChapterFallback")
    fun getChapter(@RequestParam chapterId: Long): ResponseDto<ChapterDto>{
        LOGGER.info("Inicio de obtencion de un capitulo.")
        return chapterBl.getChapter(chapterId)
    }

    /**
     * Endpoint para actualizar un capítulo
     * @param chapter
     * @return ResponseDto<ChapterDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol escritor
     */
    @PutMapping("/chapter")
    @CircuitBreaker(name = "chapterCB", fallbackMethod = "updateChapterFallback")
    @Bulkhead(name = "chapterBH", fallbackMethod = "updateChapterFallback")
    @RateLimiter(name = "chapterRL", fallbackMethod = "updateChapterFallback")
    @Retry(name = "chapterRT", fallbackMethod = "updateChapterFallback")
    fun updateChapter(@RequestHeader headers: Map<String, String>, @RequestBody chapter: ChapterDto): ResponseDto<ChapterDto>{
        //Verificamos que el usuario que quiere actualizar el capitulo sea el mismo que la creo la historia
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterIdWithoutCreating(userId)
        val storyDto = storyBl.getStory(chapter.storyId).data
        if(storyDto.writerId != writerId){
            throw Exception("No puedes actualizar un capitulo en una historia que no es tuya")
        }
        LOGGER.info("Inicio de actualizacion de un capitulo.")
        return chapterBl.updateChapter(chapter)
    }

    /**
     * Endpoint para eliminar un capítulo - eliminación lógica
     * @param chapterId
     * @return ResponseDto<ChapterDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol escritor
     */
    @DeleteMapping("/chapter")
    @CircuitBreaker(name = "chapterCB", fallbackMethod = "deleteChapterFallback")
    @Bulkhead(name = "chapterBH", fallbackMethod = "deleteChapterFallback")
    @RateLimiter(name = "chapterRL", fallbackMethod = "deleteChapterFallback")
    @Retry(name = "chapterRT", fallbackMethod = "deleteChapterFallback")
    fun deleteChapter(@RequestHeader headers: Map<String, String>, @RequestParam chapterId: Long): ResponseDto<ChapterDto>{
        LOGGER.info("Inicio de la eliminacion lógica de un capitulo.")
        //Verificamos que el usuario que quiere eliminar el capitulo sea el mismo que la creo la historia
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterIdWithoutCreating(userId)
        val chapterDto = chapterBl.getChapter(chapterId).data
        val storyDto = storyBl.getStory(chapterDto.storyId).data
        if(storyDto.writerId != writerId){
            throw Exception("No puedes eliminar un capitulo en una historia que no es tuya")
        }
        return chapterBl.deleteChapter(chapterId)
    }

    // =============================================================================================
    // COMENTARIOS
    /**
     * Endpoint para publicar un comentario
     * @param comment
     * @return ResponseDto<ChapterDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol usuario/user
     */
    @PostMapping("/chapter/comment/new")
    @CircuitBreaker(name = "commentCB", fallbackMethod = "createCommentFallback")
    @Bulkhead(name = "commentBH", fallbackMethod = "createCommentFallback")
    @RateLimiter(name = "commentRL", fallbackMethod = "createCommentFallback")
    @Retry(name = "commentRT", fallbackMethod = "createCommentFallback")
    fun createComment(@RequestHeader headers: Map<String, String>, @RequestBody comment: CommentDto): ResponseDto<CommentDto>{
        LOGGER.info("Inicio de creacion de un comentario.")
        //Reemplazamos el userÍd
        val userDto = userService.getUser(headers).data
        comment.userId = userDto.sub
        return commentBl.createComment(comment)
    }

    /**
     * Endpoint para obtener los comentarios de un capítulo
     * @param chapterId
     * @return ResponseDto<List<ChapterDto>>
     */
    @GetMapping("/chapter/{chapterId}/comment/list")
    @CircuitBreaker(name = "commentCB", fallbackMethod = "getCommentsFallback")
    @Bulkhead(name = "commentBH", fallbackMethod = "getCommentsFallback")
    @RateLimiter(name = "commentRL", fallbackMethod = "getCommentsFallback")
    @Retry(name = "commentRT", fallbackMethod = "getCommentsFallback")
    fun getComments(@PathVariable chapterId: Long): ResponseDto<List<CommentDto>>{
        LOGGER.info("Inicio de obtencion de los comentarios de un capitulo.")
        return commentBl.getComments(chapterId)
    }

    /**
     * Endpoint para eliminar un comentario - eliminación lógica
     * @param commentId
     * @return ResponseDto<CommentDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol usuario/user
     */
    @DeleteMapping("/chapter/comment")
    @CircuitBreaker(name = "commentCB", fallbackMethod = "deleteCommentFallback")
    @Bulkhead(name = "commentBH", fallbackMethod = "deleteCommentFallback")
    @RateLimiter(name = "commentRL", fallbackMethod = "deleteCommentFallback")
    @Retry(name = "commentRT", fallbackMethod = "deleteCommentFallback")
    fun deleteComment(@RequestHeader headers: Map<String, String>, @RequestParam commentId: Long): ResponseDto<CommentDto>{
        LOGGER.info("Inicio de la eliminacion lógica de un comentario.")
        //Verificamos que el usuario que quiere eliminar el comentario sea el mismo que lo creo
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val commentDto = commentBl.getComment(commentId).data
        if(commentDto.userId != userId){
            throw Exception("No puedes eliminar un comentario que no es tuyo")
        }
        return commentBl.deleteComment(commentId)
    }

    // =============================================================================================
    // CATEGORIAS
    /**
     * Endpoint para obtener la lista de categorias
     * @return ResponseDto<List<CategoryDto>>
     */
    @GetMapping("/category/list")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "getCategoriesFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "getCategoriesFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "getCategoriesFallback")
    @Retry(name = "storyRT", fallbackMethod = "getCategoriesFallback")
    fun getCategories(): ResponseDto<List<CategoryDto>>{
        LOGGER.info("Inicio de obtencion de las categorias.")
        return storyBl.getCategories()
    }

    // =============================================================================================
    // TAG
    /**
     * Endpoint para crear un tag y asociarlo a una historia
     * @param tags, storyId
     * @return ResponseDto<List<StoryTagDto>>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol escritor
     */
    @PostMapping("/tags")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "createStoryTagFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "createStoryTagFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "createStoryTagFallback")
    @Retry(name = "storyRT", fallbackMethod = "createStoryTagFallback")
    fun createStoryTag(@RequestHeader headers: Map<String, String>, @RequestBody tags: List<String>, @RequestParam storyId: Long): ResponseDto<List<StoryTagDto>>{
        LOGGER.info("Inicio de creacion de un tag y su relación con la historia.")
        //Verificamos que el usuario que quiere crear el tag sea el mismo que la creo la historia
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterIdWithoutCreating(userId)
        val storyDto = storyBl.getStory(storyId).data
        if(storyDto.writerId != writerId){
            throw Exception("No puedes crear un tag en una historia que no es tuya")
        }
        return tagBl.createStoryTags(tags, storyId)
    }

    /**
     * Endpoint para obtener los tags de una historia
     * @param storyId
     * @return ResponseDto<List<StoryTagDto>>
     */
    @GetMapping("/tags")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "getStoryTagsFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "getStoryTagsFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "getStoryTagsFallback")
    @Retry(name = "storyRT", fallbackMethod = "getStoryTagsFallback")
    fun getStoryTags(@RequestParam storyId: Long): ResponseDto<List<TagDto>>{
        LOGGER.info("Inicio de obtencion de los tags de una historia.")
        return tagBl.getStoryTags(storyId)
    }

    /**
     * Endpoint para actualizar los tags de una historia
     * @param tags, storyId
     * @return ResponseDto<List<StoryTagDto>>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol escritor
     */
    @PutMapping("/tags")
    @CircuitBreaker(name = "storyCB", fallbackMethod = "updateStoryTagFallback")
    @Bulkhead(name = "storyBH", fallbackMethod = "updateStoryTagFallback")
    @RateLimiter(name = "storyRL", fallbackMethod = "updateStoryTagFallback")
    @Retry(name = "storyRT", fallbackMethod = "updateStoryTagFallback")
    fun updateStoryTag(@RequestHeader headers: Map<String, String>, @RequestBody tags: List<String>, @RequestParam storyId: Long): ResponseDto<List<StoryTagDto>>{
        LOGGER.info("Inicio de actualizacion de los tags de una historia.")
        //Verificamos que el usuario que quiere actualizar el tag sea el mismo que la creo la historia
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        val writerId = writerBl.getWriterIdWithoutCreating(userId)
        val storyDto = storyBl.getStory(storyId).data
        if(storyDto.writerId != writerId){
            throw Exception("No puedes actualizar un tag en una historia que no es tuya")
        }
        return tagBl.updateStoryTags(tags, storyId)
    }
    //Se eliminan tags?

    // =============================================================================================
    // ESCRITOR
    /**
     * Endpoint para obtener el writerId del usuario
     * @param userId
     * @return Long
     */
    @GetMapping("/writerId")
    fun getWriterId(@RequestParam userId: String): Long{
        LOGGER.info("Inicio de obtencion del writerId.")
        return writerBl.getWriterId(userId)
    }

    /**
     * Endpoint para eliminar al escritor - eliminación lógica
     * @param userId
     * @return String
     */
    @DeleteMapping("/writer")
    fun deleteWriter(@RequestParam userId: String): String{
        LOGGER.info("Inicio de la eliminacion lógica del escritor.")
        return writerBl.deleteWriter(userId)
    }

    /**
     * Endpoint para obtener el userId del escritor
     * @param writerId
     * @return String
     */
    @GetMapping("/writer/userId")
    fun getWriterUserId(@RequestParam writerId: Long): ResponseDto<String>{
        LOGGER.info("Inicio de obtencion del userId del escritor.")
        return writerBl.getUserId(writerId)
    }

    // =============================================================================================
    // LIBRARY
    /**
     * Endpoint para guardar una historia en la biblioteca del usuario
     * @param userId, storyId
     * @return ResponseDto<LibraryDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol usuario/user
     */
    @PostMapping("/library")
    @CircuitBreaker(name = "libraryCB", fallbackMethod = "saveStoryFallback")
    @Bulkhead(name = "libraryBH", fallbackMethod = "saveStoryFallback")
    @RateLimiter(name = "libraryRL", fallbackMethod = "saveStoryFallback")
    @Retry(name = "libraryRT", fallbackMethod = "saveStoryFallback")
    fun saveStory(@RequestHeader headers: Map<String, String>, @RequestParam storyId: Long): ResponseDto<LibraryDto>{
        LOGGER.info("Inicio de guardado de una historia en la biblioteca del usuario.")
        //Obtenemos el userId del usuario
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        return libraryBl.saveStoryInLibrary(userId, storyId)
    }

    /**
     * Endpoint para obtener la biblioteca del usuario
     * @param headers, username
     * @return ResponseDto<List<LibraryDto>>
     * Endpoint solo disponible para el rol usuario/user
     **/
    @GetMapping("/library")
    @CircuitBreaker(name = "libraryCB", fallbackMethod = "getLibraryFallback")
    @Bulkhead(name = "libraryBH", fallbackMethod = "getLibraryFallback")
    @RateLimiter(name = "libraryRL", fallbackMethod = "getLibraryFallback")
    @Retry(name = "libraryRT", fallbackMethod = "getLibraryFallback")
    fun getLibrary(@RequestHeader headers: Map<String, String>, @RequestParam username:String): ResponseDto<List<StoryDto>>{
        LOGGER.info("Inicio de obtencion de la biblioteca del usuario.")
        //Obtenemos el userId del usuario
        val userDto = userService.getUserByUsername(username, headers).data
        val userId = userDto.sub
        return libraryBl.getStoriesInLibrary(userId)
    }


    /**
     * Endpoint para eliminar una historia de la biblioteca del usuario
     * @param headers, storyId
     * @return ResponseDto<LibraryDto>
     * Se necesita token de autorización
     * Endpoint solo disponible para el rol usuario/user
     */
    @DeleteMapping("/library")
    @CircuitBreaker(name = "libraryCB", fallbackMethod = "deleteStoryFromLibraryFallback")
    @Bulkhead(name = "libraryBH", fallbackMethod = "deleteStoryFromLibraryFallback")
    @RateLimiter(name = "libraryRL", fallbackMethod = "deleteStoryFromLibraryFallback")
    @Retry(name = "libraryRT", fallbackMethod = "deleteStoryFromLibraryFallback")
    fun deleteStoryFromLibrary(@RequestHeader headers: Map<String, String>, @RequestParam storyId: Long): ResponseDto<LibraryDto>{
        LOGGER.info("Inicio de eliminacion de una historia de la biblioteca del usuario.")
        //Obtenemos el userId del usuario
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        return libraryBl.deleteStoryInLibrary(userId, storyId)
    }

    /**
     * Endpoint GET para actualizar la vista de la biblioteca al abrir una historia - RabbitMq
     * @param headers, storyId
     * @return ResponseDto<String>
     *
     */
    @GetMapping("/library/refresh")
    fun refreshLibraryStories(@RequestHeader headers: Map<String, String>, @RequestParam storyId: Long): ResponseDto<String>{
        LOGGER.info("Inicio de actualizacion de la vista de la biblioteca.")
        //Obtenemos el userId del usuario
        val userDto = userService.getUser(headers).data
        val userId = userDto.sub
        //Mandamos el mensaje a la cola
        val response = libraryBl.sendRabbit(userId, storyId)
        return ResponseDto("Success", "Se ha enviado el mensaje a la cola", true)
    }

    /**
     * Endpoint GET para obtener la cantidad de libros  por categoria
     * @param headers, categoryId
     * @return cantidad
     */
    @GetMapping("/category/count")
    fun getCategoryCount(@RequestParam categoryId: Long): ResponseDto<Int>{
        LOGGER.info("Inicio de obtencion de la cantidad de libros por categoria.")
        return ResponseDto(
            data = storyBl.getCategoryCount(categoryId),
            message = "Cantidad de libros por categoria",
            success = true
        )
    }
    /**
     * Endpoint para obtener los usuarios que guardaron el libro en su biblioteca
     * @param headers, storyId
     * @return ResponseDto<List<UserDto>>
     * Endpoint solo disponible para el rol usuario/user
     **/
    @GetMapping("/library/users/email")
    fun getLibraryUsersEmail(@RequestHeader headers: Map<String, String>, @RequestParam storyId:Long): ResponseDto<List<UserDto>>{
        LOGGER.info("Inicio de obtencion de los usuarios que guardaron el libro en su biblioteca.")
        val registers = libraryBl.getLibraryByStoryId(storyId)
        val users = registers.map { userService.getUserById(it.userId, headers).data }
        return ResponseDto(
            data = users,
            message = "Se obtuvieron los usuarios que guardaron el libro en su biblioteca",
            success = true
        )
    }

    // =============================================================================================
    // Fallback methods
    // =============================================================================================
    fun createStoryFallback(@RequestHeader headers: Map<String, String>, @RequestBody storyEntity: StoryEntity, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo crear la historia"
        if(e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun listStoryFallback(@RequestHeader headers: Map<String, String>, @RequestParam page: Int, @RequestParam size: Int, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener la lista de historias"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun listStoriesByUsernameFallback(@RequestHeader headers: Map<String, String>, @PathVariable username: String, @RequestParam page: Int, @RequestParam size: Int, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener la lista de historias del usuario $username"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun listStoryByTitleFallback(@RequestParam title: String, @RequestParam page: Int, @RequestParam size: Int, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener la lista de historias con el titulo $title"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun listStoryByCategoryFallback(@RequestParam categoryId: Long, @RequestParam page: Int, @RequestParam size: Int, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener la lista de historias con la categoria $categoryId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }

    fun getStoryFallback(@RequestParam storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener la historia $storyId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun updateStoryFallback(@RequestHeader headers: Map<String, String>, @RequestBody story: StoryDto, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo actualizar la historia"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun voteStoryFallback(@RequestHeader headers: Map<String, String>,@RequestParam storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = ""
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun deleteStoryFallback(@RequestHeader headers: Map<String, String>, @RequestParam storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo eliminar la historia $storyId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun createChapterFallback(@RequestHeader headers: Map<String, String>, @RequestBody chapter: ChapterDto, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo crear el capitulo"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun getChaptersFallback(@PathVariable storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener los capitulos de la historia $storyId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)

    }
    fun getChapterFallback(@RequestParam chapterId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener el capitulo $chapterId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun updateChapterFallback(@RequestHeader headers: Map<String, String>, @RequestBody chapter: ChapterDto, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo actualizar el capitulo"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun deleteChapterFallback(@RequestHeader headers: Map<String, String>, @RequestParam chapterId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo eliminar el capitulo $chapterId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun createCommentFallback(@RequestHeader headers: Map<String, String>, @RequestBody comment: CommentDto, e: Exception): ResponseDto<out Any> {
        val errorMessage = when (e) {
            is RequestNotPermitted -> "Error: ha excedido el límite máximo de llamadas permitidas en el período."
            else -> "Error al crear el comentario: $e.message"
        }
        LOGGER.error(errorMessage)
        return ResponseDto("Error", "No se pudo crear el comentario", false)
    }
    fun getCommentsFallback(@PathVariable chapterId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener los comentarios del capitulo $chapterId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun deleteCommentFallback(@RequestHeader headers: Map<String, String>, @RequestParam commentId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo eliminar el comentario $commentId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun getCategoriesFallback(e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener las categorias"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun createStoryTagFallback(@RequestHeader headers: Map<String, String>, @RequestBody tags: List<String>, @RequestParam storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo crear los tags de la historia $storyId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun getStoryTagsFallback(@RequestParam storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener los tags de la historia $storyId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun updateStoryTagFallback(@RequestHeader headers: Map<String, String>, @RequestBody tags: List<String>, @RequestParam storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo actualizar los tags de la historia $storyId"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun saveStoryFallback(@RequestHeader headers: Map<String, String>, @RequestParam storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo guardar la historia $storyId en la bilbioteca"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun getLibraryFallback(@RequestHeader headers: Map<String, String>, @RequestParam username:String, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo obtener la biblioteca del usuario $username"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }
    fun deleteStoryFromLibraryFallback(@RequestHeader headers: Map<String, String>, @RequestParam storyId: Long, e: Exception): ResponseDto<out Any> {
        var errorMessage = "No se pudo eliminar la historia $storyId de la biblioteca"
        if (e is RequestNotPermitted){
            errorMessage = "Ha excedido el límite máximo de llamadas permitidas en el período."
        }else if (e is BulkheadFullException){
            errorMessage = "El servicio no está disponible en este momento, por favor intente más tarde."
        }
        LOGGER.error("Error: $errorMessage, ${e.message}" )
        return ResponseDto("Error", errorMessage, false)
    }

}