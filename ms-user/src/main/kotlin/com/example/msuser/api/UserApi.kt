package com.example.msuser.api

import com.example.msuser.bl.UserBl
import com.example.msuser.dto.*
import com.example.msuser.exception.StoryverseException
import com.example.msuser.util.AuthUtil
import io.github.resilience4j.bulkhead.BulkheadFullException
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
@RestController
@RequestMapping("api/v1/user")
class UserApi(private val userBl: UserBl) {
    // ======================================= USUARIOS ================================================
    /**
     * Endpoint GET para obtener un usuario por su username
     * @param username
     * @param headers
     * @return ResponseDto<UserDto>
     */
    @GetMapping("/{username}")
    @CircuitBreaker(name = "userCB", fallbackMethod = "getUserByUsernameFallback")
    @RateLimiter(name = "userRL", fallbackMethod = "rateLimiterGetUserByUsernameFallback")
    @Bulkhead(name = "userBH", fallbackMethod = "getUserByUsernameFallback")
    @Retry(name = "userRT", fallbackMethod = "getUserByUsernameFallback")
    fun getUserByUsername(
        @PathVariable username: String,
        @RequestHeader headers: Map<String, String>
    ): ResponseDto<UserDto> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUserByUsername(username, token)
        Thread.sleep(500);
        return ResponseDto(
            data = userDto,
            message = "Usuario obtenido",
            success = true
        )
    }

    /**
     * Endpoint GET para obtener un usuario con su token
     * @param headers
     * @return ResponseDto<UserDto>
     */

    @GetMapping("/")
    @CircuitBreaker(name = "userCB", fallbackMethod = "getUserFallback")
    @RateLimiter(name = "userRL", fallbackMethod = "rateLimiterGetUserFallback")
    @Bulkhead(name = "userBH", fallbackMethod = "getUserFallback")
    @Retry(name = "userRT", fallbackMethod = "getUserFallback")
    fun getUser(@RequestHeader headers: Map<String, String>): ResponseDto<UserDto> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUser(token)
        return ResponseDto(
            data = userDto,
            message = "Usuario obtenido",
            success = true
        )
    }

    /** Endpoint PUT para actualizar un usuario
     * @param headers
     * @param attributesDto
     * @return ResponseDto<UserDto>
     */
    @PutMapping
    @CircuitBreaker(name = "userCB", fallbackMethod = "updateUserFallback")
    @RateLimiter(name = "userRL", fallbackMethod = "rateLimiterUpdateUserFallback")
    @Bulkhead(name = "userBH", fallbackMethod = "updateUserFallback")
    @Retry(name = "userRT", fallbackMethod = "updateUserFallback")
    fun updateUser(
        @RequestHeader headers: Map<String, String>,
        @RequestBody attributesDto: AttributesDto
    ): ResponseDto<String> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)

        // Actualizamos el usuario de Keycloak
        userBl.updateUser(attributesDto, token)
        return ResponseDto(
            data = "Usuario actualizado",
            message = "Usuario actualizado",
            success = true
        )

    }

    /**
     * Endpoint PUT para cambiar la contraseña de un usuario
     * @param headers
     * @param changePasswordDto
     * @return ResponseDto<String>
     */
    @PutMapping("/change-password")
    @CircuitBreaker(name = "userCB", fallbackMethod = "changePasswordFallback")
    @RateLimiter(name = "userRL", fallbackMethod = "rateLimiterChangePasswordFallback")
    @Bulkhead(name = "userBH", fallbackMethod = "changePasswordFallback")
    @Retry(name = "userRT", fallbackMethod = "changePasswordFallback")
    fun changePassword(
        @RequestHeader headers: Map<String, String>,
        @RequestBody changePasswordDto: ChangePasswordDto
    ): ResponseDto<String> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Cambiamos la contraseña del usuario de Keycloak
        userBl.changePassword(changePasswordDto, token)
        return ResponseDto(
            data = "Contraseña cambiada",
            message = "Contraseña cambiada",
            success = true
        )
    }


    /**
     * Endpoint DELETE para eliminar un usuario por su token
     * @param headers
     * @return ResponseDto<String>
     */
    @DeleteMapping("/")
    @CircuitBreaker(name = "userCB", fallbackMethod = "deleteUserFallback")
    @RateLimiter(name = "userRL", fallbackMethod = "rateLimiterDeleteUserFallback")
    @Bulkhead(name = "userBH", fallbackMethod = "deleteUserFallback")
    @Retry(name = "userRT", fallbackMethod = "deleteUserFallback")
    fun deleteUser(@RequestHeader headers: Map<String, String>): ResponseDto<String> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Eliminamos el usuario de Keycloak
        userBl.deleteUser(token)
        return ResponseDto(
            data = "Usuario eliminado",
            message = "Usuario eliminado",
            success = true
        )
    }

    /**
     * Endpoint GET para obtener datos de usario por su userId
     * @param userId
     * @return ResponseDto<UserDto>
     */
    @GetMapping("/userId/{userId}")
    fun getUserByUserId(@PathVariable userId: String, @RequestHeader headers: Map<String, String>): ResponseDto<UserDto> {
        // Obtenemos el usuario de Keycloak
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        val userDto = userBl.getUserById(userId, token)
        return ResponseDto(
            data = userDto,
            message = "Usuario obtenido",
            success = true
        )
    }
    // =====================SEGUIDORES=====================================
    /**
     * Endpoint GET para obtener todos los seguidores de un usuario
     * @param username
     * @param headers
     * @return ResponseDto<List<UserDto>>
     */
    @GetMapping("/{username}/followers")
    @CircuitBreaker(name = "followerCB", fallbackMethod = "getFollowersFallback")
    @RateLimiter(name = "followerRL", fallbackMethod = "rateLimiterGetFollowersFallback")
    @Bulkhead(name = "followerBH", fallbackMethod = "getFollowersFallback")
    @Retry(name = "followerRT", fallbackMethod = "getFollowersFallback")
    fun getFollowers(
        @PathVariable username: String,
        @RequestHeader headers: Map<String, String>
    ): ResponseDto<List<UserDto>> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUserByUsername(username, token)
        // Obtenemos los seguidores del usuario
        val followers = userBl.getFollowers(userDto.sub, token)
        // Retornamos la respuesta
        return ResponseDto(
            data = followers,
            message = "Seguidores obtenidos",
            success = true
        )
    }

    /**
     * Endpoint GET para obtener todos los usuarios que sigue un usuario
     * @param username
     * @param headers
     * @return ResponseDto<List<UserDto>>
     */
    @GetMapping("/{username}/following")
    @CircuitBreaker(name = "followerCB", fallbackMethod = "getFollowingFallback")
    @RateLimiter(name = "followerRL", fallbackMethod = "rateLimiterGetFollowingFallback")
    @Bulkhead(name = "followerBH", fallbackMethod = "getFollowingFallback")
    @Retry(name = "followerRT", fallbackMethod = "getFollowingFallback")
    fun getFollowing(
        @PathVariable username: String,
        @RequestHeader headers: Map<String, String>
    ): ResponseDto<List<UserDto>> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUserByUsername(username, token)
        // Obtenemos los usuarios que sigue el usuario
        val following = userBl.getFollowing(userDto.sub, token)
        // Retornamos la respuesta
        return ResponseDto(
            data = following,
            message = "Usuarios seguidos obtenidos",
            success = true
        )
    }

    /**
     * Endpoint POST para seguir a un usuario
     * @param headers
     * @param target
     * @return ResponseDto<String>
     */
    @PostMapping("/follow")
    @CircuitBreaker(name = "followerCB", fallbackMethod = "followUserFallback")
    @RateLimiter(name = "followerRL", fallbackMethod = "rateLimiterFollowUserFallback")
    @Bulkhead(name = "followerBH", fallbackMethod = "followUserFallback")
    @Retry(name = "followerRT", fallbackMethod = "followUserFallback")
    fun followUser(@RequestHeader headers: Map<String, String>, @RequestParam target: String): ResponseDto<String> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUser(token)
        //Obtenemos el usuario a seguir
        val targetUser = userBl.getUserByUsername(target, token)
        // Seguimos al usuario
        userBl.followUser(userDto, targetUser)
        // Retornamos la respuesta
        return ResponseDto(
            data = "Usuario seguido",
            message = "Usuario seguido",
            success = true
        )
    }

    /**
     * Endpoint POST para dejar de seguir a un usuario
     * @param headers
     * @param target
     * @return ResponseDto<String>
     */
    @PostMapping("/unfollow")
    @CircuitBreaker(name = "followerCB", fallbackMethod = "unfollowUserFallback")
    @RateLimiter(name = "followerRL", fallbackMethod = "rateLimiterUnfollowUserFallback")
    @Bulkhead(name = "followerBH", fallbackMethod = "unfollowUserFallback")
    @Retry(name = "followerRT", fallbackMethod = "unfollowUserFallback")
    fun unfollowUser(@RequestHeader headers: Map<String, String>, @RequestParam target: String): ResponseDto<String> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUser(token)
        //Obtenemos el usuario a dejar de seguir
        val targetUser = userBl.getUserByUsername(target, token)
        // Dejamos de seguir al usuario
        userBl.unfollowUser(userDto, targetUser)
        // Retornamos la respuesta
        return ResponseDto(
            data = "Se dejo de seguir al usuario",
            message = "Se dejo de seguir al usuario",
            success = true
        )
    }

    /**
     * Endpoint GET para saber si un usuario sigue a otro
     * @param headers
     * @param target
     * @return ResponseDto<Boolean>
     */
    @GetMapping("/follows")
    fun followsUser(@RequestHeader headers: Map<String, String>, @RequestParam target: String): ResponseDto<Boolean> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUser(token)
        //Obtenemos el usuario a dejar de seguir
        val targetUser = userBl.getUserByUsername(target, token)
        // Dejamos de seguir al usuario
        val follows = userBl.followsUser(userDto, targetUser)
        // Retornamos la respuesta
        return ResponseDto(
            data = follows,
            message = if (follows) "El usuario sigue al usuario ${target}" else "El usuario no sigue al usuario ${target}",
            success = true
        )
    }


    //===========================Anuncios===========================
    /**
     * Endpoint POST para publicar un anuncio
     * @param headers
     * @param target
     * @return ResponseDto<String>
     */
    @PostMapping("/announce")
    @CircuitBreaker(name = "announcementCB", fallbackMethod = "publishAnnouncementFallback")
    @RateLimiter(name = "announcementRL", fallbackMethod = "rateLimiterPublishAnnouncementFallback")
    @Bulkhead(name = "announcementBH", fallbackMethod = "publishAnnouncementFallback")
    @Retry(name = "announcementRT", fallbackMethod = "publishAnnouncementFallback")
    fun publishAnnouncement(
        @RequestHeader headers: Map<String, String>,
        @RequestParam announce: String
    ): ResponseDto<String> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)

        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUser(token)
        // Publicamos el anuncio
        userBl.createAnnouncement(userDto, announce)
        // Retornamos la respuesta
        return ResponseDto(
            data = "Nuevo anuncio publicado",
            message = "Nuevo anuncio publicado",
            success = true
        )

    }

    /**
     * Endpoint GET para obtener todos los anuncios de un usuario
     * @param username
     * @param headers
     * @return ResponseDto<List<UserDto>>
     */
    @GetMapping("/{username}/announcements")
    @CircuitBreaker(name = "announcementCB", fallbackMethod = "getAnnouncementFallback")
    @RateLimiter(name = "announcementRL", fallbackMethod = "rateLimiterGetAnnouncementFallback")
    @Bulkhead(name = "announcementBH", fallbackMethod = "getAnnouncementFallback")
    @Retry(name = "announcementRT", fallbackMethod = "getAnnouncementFallback")
    fun getAnnouncements(
        @PathVariable username: String,
        @RequestHeader headers: Map<String, String>
    ): ResponseDto<List<AnnouncementDto>> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)

        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUserByUsername(username, token)
        // Obtenemos los anuncios del usuario
        val announcement = userBl.getAnnouncement(userDto.sub)
        // Retornamos la respuesta
        return ResponseDto(
            data = announcement,
            message = "Anuncios obtenidos",
            success = true
        )

    }

    /**
     * Endpoint DELETE para eliminar el anuncio de un usuario
     * @param headers
     * @param target
     * @return ResponseDto<String>
     */
    @DeleteMapping("/announce")
    @CircuitBreaker(name = "announcementCB", fallbackMethod = "deleteAnnouncementFallback")
    @RateLimiter(name = "announcementRL", fallbackMethod = "rateLimiterDeleteAnnouncementFallback")
    @Bulkhead(name = "announcementBH", fallbackMethod = "deleteAnnouncementFallback")
    @Retry(name = "announcementRT", fallbackMethod = "deleteAnnouncementFallback")
    fun deleteAnnouncement(
        @RequestHeader headers: Map<String, String>,
        @RequestParam announce: Long
    ): ResponseDto<String> {
        //AuthUtil
        val authUtil = AuthUtil()
        val token = authUtil.getTokenFromHeader(headers)
        // Obtenemos el usuario de Keycloak
        val userDto = userBl.getUser(token)
        // Dejamos de seguir al usuario
        userBl.deleteAnnouncement(userDto, announce)
        // Retornamos la respuesta
        return ResponseDto(
            data = "Se elimino el anuncio",
            message = "Se elimino el anuncio",
            success = true
        )
    }



    /**
     * Prueba rabitmq
     */
    @GetMapping("/testNotification")
    fun getConvertions(): ResponseEntity<String> {
        val convertions = userBl.sendNotification();
        return ResponseEntity.ok(convertions)
    }

    /** =====================================================
     * Fallback methods                                     *
     ======================================================*/
    // CIRCUIT BREAKER - BULKHEAD
    fun getUserByUsernameFallback(
        @PathVariable username: String,
        @RequestHeader headers: Map<String, String>,
        exception: Exception
    ): ResponseDto<out Any> {
        if (exception is BulkheadFullException) {
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        } else {
            return ResponseDto("Error: usuario $username no encontrado", "$exception.message", false)
        }
    }

    fun getUserFallback(
        @RequestHeader headers: Map<String, String>,
        exception: Exception
    ): ResponseDto<out Any> {
        if (exception is BulkheadFullException) {
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        } else {
            return ResponseDto("Error: usuario no encontrado", "$exception.message", false)
        }
    }

    fun updateUserFallback(
        @RequestHeader headers: Map<String, String>,
        @RequestBody attributesDto: AttributesDto,
        exception: Exception
    ): ResponseDto<String> {
        if (exception is BulkheadFullException) {
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        } else {
            return ResponseDto("Error: usuario no actualizado", "$exception.message", false)
        }
    }

    fun deleteUserFallback(
        @RequestHeader headers: Map<String, String>,
        exception: Exception
    ): ResponseDto<String> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: usuario no eliminado", "$exception.message", false)
        }
    }

    fun changePasswordFallback(
        @RequestHeader headers: Map<String, String>,
        @RequestBody changePasswordDto: ChangePasswordDto,
        exception: Exception
    ): ResponseDto<String> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: contraseña no actualizada", "$exception.message", false)
        }
    }

    fun getFollowersFallback(
        @PathVariable username: String,
        @RequestHeader headers: Map<String, String>,
        exception: Exception
    ): ResponseDto<out Any> {
        if (exception is BulkheadFullException) {
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        } else {
            return ResponseDto("Error: seguidores no encontrados", "$exception.message", false)
        }
    }

    fun getFollowingFallback(
        @PathVariable username: String,
        @RequestHeader headers: Map<String, String>,
        exception: Exception
    ): ResponseDto<out Any> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: usuarios seguidos no encontrados", "$exception.message", false)
        }
    }

    fun followsUserFallback(
        @RequestHeader headers: Map<String, String>,
        @RequestParam target: String,
        exception: Exception
    ): ResponseDto<out Any> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: no se pudo verificar si el usuario sigue a otro", "$exception.message", false)
        }
    }

    fun followUserFallback(
        @RequestHeader headers: Map<String, String>,
        @RequestParam target: String,
        exception: Exception
    ): ResponseDto<String> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: usuario no seguido", "$exception.message", false)
        }
    }

    fun unfollowUserFallback(
        @RequestHeader headers: Map<String, String>,
        @RequestParam target: String,
        exception: Exception
    ): ResponseDto<String> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: usuario no dejado de seguir", "$exception.message", false)
        }
    }

    fun publishAnnouncementFallback(
        @RequestHeader headers: Map<String, String>,
        @RequestParam announce: String,
        exception: Exception
    ): ResponseDto<String> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: anuncio no publicado", "$exception.message", false)
        }
    }

    fun getAnnouncementFallback(
        @PathVariable username: String,
        @RequestHeader headers: Map<String, String>,
        exception: Exception
    ): ResponseDto<out Any> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: anuncios no encontrados", "$exception.message", false)
        }
    }

    fun deleteAnnouncementFallback(
        @RequestHeader headers: Map<String, String>,
        @RequestParam announce: Long,
        exception: Exception
    ): ResponseDto<String> {
        if(exception is BulkheadFullException){
            return ResponseDto("Error", "El servicio está a toda su capacidad, intente mas tarde", false)
        }else{
            return ResponseDto("Error: anuncio no eliminado", "$exception.message", false)
        }
    }

    // RATE LIMITER
    fun rateLimiterGetUserByUsernameFallback(
        username: String,
        headers: Map<String, String>,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterGetUserFallback(
        headers: Map<String, String>,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterUpdateUserFallback(
        headers: Map<String, String>,
        attributesDto: AttributesDto,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterDeleteUserFallback(
        headers: Map<String, String>,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterChangePasswordFallback(
        headers: Map<String, String>,
        changePasswordDto: ChangePasswordDto,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterGetFollowersFallback(
        username: String,
        headers: Map<String, String>,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterGetFollowingFallback(
        username: String,
        headers: Map<String, String>,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterFollowUserFallback(
        headers: Map<String, String>,
        target: String,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterUnfollowUserFallback(
        headers: Map<String, String>,
        target: String,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterPublishAnnouncementFallback(
        headers: Map<String, String>,
        announce: String,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterGetAnnouncementFallback(
        username: String,
        headers: Map<String, String>,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }

    fun rateLimiterDeleteAnnouncementFallback(
        headers: Map<String, String>,
        announce: Long,
        exception: RequestNotPermitted
    ): ResponseDto<Nothing?> {
        val message = "Error: Ha excedido el límite máximo de llamadas permitidas en el período."
        return ResponseDto(data = null, message = message, success = false)
    }
}
