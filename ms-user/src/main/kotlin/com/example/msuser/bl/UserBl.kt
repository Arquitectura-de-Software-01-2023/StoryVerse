package com.example.msuser.bl

import com.example.msuser.dao.Announcement
import com.example.msuser.dao.Follower
import com.example.msuser.dao.repository.AnnouncementRepository
import com.example.msuser.dao.repository.FollowerRepository
import com.example.msuser.dto.*
import com.example.msuser.exception.StoryverseException
import com.example.msuser.producer.NotificationProducer
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.*
import java.util.logging.Logger


@Suppress("DEPRECATION")
@Service
@Transactional
class UserBl @Autowired constructor(private val followerRepository: FollowerRepository,
    private val notificationProducer: NotificationProducer,
    private val announcementRepository: AnnouncementRepository) {
    var LOGGER: Logger = Logger.getLogger(UserBl::class.java.name)

    @Value("\${keycloakAPI.userinfo.url}")
    lateinit var keycloakUserinfoUrl: String


    @Value("\${keycloakAPI.account.url}")
    lateinit var keycloakAccountUrl: String

    /**
     * Método que se encarga de obtener la información de un usuario por su id
     * @param userId
     * @param token
     * @return UserDto
     */
    fun getUserById(userId: String, token: String): UserDto {
        LOGGER.info("Obteniendo usuario por id: $userId")
        try {
            val client: OkHttpClient = OkHttpClient().newBuilder().build()
            val request: Request = Request.Builder()
                    .url("$keycloakAccountUrl/$userId")
                    .addHeader("Authorization", "Bearer $token")
                    .method("GET", null)
                    .build()

            // Enviar la solicitud de userinfo y manejar la respuesta
            val response = client.newCall(request).execute()
            if (!response.isSuccessful){
                throw StoryverseException("Error al obtener el usuario")
            }else {
                val parsedResponse = parseSingleResponse(response)
                LOGGER.info("Se obtuvo el usuario: $parsedResponse")
                return UserDto(
                        sub = parsedResponse.id,
                        email = parsedResponse.email,
                        preferred_username = parsedResponse.username,
                        birthdate = parsedResponse.attributes.birthdate[0],
                        library_private = parsedResponse.attributes.libraryPrivate[0],
                        url_pfp = parsedResponse.attributes.urlPfp[0],
                        url_header = parsedResponse.attributes.urlHeader[0],
                        description = parsedResponse.attributes.description[0]
                )
            }
        }catch (e: Exception){
            LOGGER.severe("Error al obtener el usuario por id $userId: ${e.message}")
            throw StoryverseException("Error al obtener el usuario por id: $userId")
        }

    }


    /**
     * Método que se encarga de obtener la información de un usuario con su username
     * @param username
     * @param token
     * @return UserDto
     */
    fun getUserByUsername(username: String, token: String): UserDto {
        LOGGER.info("Realizando peticion a Keycloak para obtener el usuario por username")
        try {
            val client: OkHttpClient = OkHttpClient().newBuilder().build()
            val request: Request = Request.Builder()
                    .url("$keycloakAccountUrl?username=$username")
                    .addHeader("Authorization", "Bearer $token")
                    .method("GET", null)
                    .build()

            // Enviar la solicitud de userinfo y manejar la respuesta
            val response = client.newCall(request).execute()
            if (!response.isSuccessful){
                throw StoryverseException("Error al obtener el usuario")
            }else {
                val parsedResponse = parseResponse(response)
                if (parsedResponse.isEmpty()) {
                    throw StoryverseException("No se encontró el usuario")
                }
                LOGGER.info("Se obtuvo el usuario: $parsedResponse")
                //Thread.sleep(5000);
                return UserDto(
                        sub = parsedResponse[0].id,
                        email = parsedResponse[0].email,
                        preferred_username = parsedResponse[0].username,
                        birthdate = parsedResponse[0].attributes.birthdate[0],
                        library_private = parsedResponse[0].attributes.libraryPrivate[0],
                        url_pfp = parsedResponse[0].attributes.urlPfp[0],
                        url_header = parsedResponse[0].attributes.urlHeader[0],
                        description = parsedResponse[0].attributes.description[0]
                )
            }
        }catch (e: Exception){
            LOGGER.severe("Error al obtener el usuario por username $username: ${e.message}")
            throw StoryverseException("Error al obtener el usuario por username")
        }
    }

    /**
     * Método que se encarga de obtener la información de un usuario con su token
     * @param token
     * @return UserDto
     */
    fun getUser(token: String): UserDto {
        LOGGER.info("Realizando peticion a Keycloak para obtener el usuario con token")
        try{
            // Configurar el RestTemplate para hacer una solicitud de userinfo
            val restTemplate = RestTemplate()
            val headers = HttpHeaders()
            headers.accept = listOf(MediaType.APPLICATION_JSON)
            headers.setBearerAuth(token)
            val request = RequestEntity.get(URI(keycloakUserinfoUrl))
                    .headers(headers)
                    .build()

            // Enviar la solicitud de userinfo y manejar la respuesta
            val response = restTemplate.exchange(request, UserDto::class.java)

            if (response.statusCode.is2xxSuccessful&&response.body!=null) {
                LOGGER.info("Se obtuvo al usuario: ${response.body}")
                return response.body!!
            } else {
                throw StoryverseException("Error al obtener el usuario")
            }
        }catch (e:Exception){
            LOGGER.severe("Error al obtener el usuario por token: ${e.message}}")
            throw StoryverseException("Error al obtener el usuario por token")
        }

    }

    /**
     * Método que se encarga de actualizar la información de un usuario
     * @param attributesDto
     * @param token
     */
    fun updateUser(attributesDto: AttributesDto, token: String) {
        LOGGER.info("Realizando peticion para actualizar usuario API keycloak")

        try {
            //Obtenemos el id del usuario
            val userId = getUser(token).sub

            //Creamos el http client
            val client = OkHttpClient()

            //Creamos el objectMapper
            val objectMapper = ObjectMapper()
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            val json = objectMapper.writeValueAsString(attributesDto)
            LOGGER.info("Request json: $json")

            val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())

            //Definimos la peticion
            val request = Request.Builder()
                    .url("$keycloakAccountUrl/$userId")
                    .addHeader("Authorization", "Bearer $token")
                    .put(body)
                    .build()

            //Ejecutamos la peticion
            val response = client.newCall(request).execute()
            if (!response.isSuccessful){
                LOGGER.info("Error: ${response.body!!.string()}")
                throw StoryverseException("Error al actualizar usuario")
            }else{
                LOGGER.info("Respuesta: ${response.body!!.string()}")
                LOGGER.info("Usuario actualizado")
            }
        }catch (e: Exception){
            LOGGER.severe("Error al actualizar el usuario: ${e.message}")
            throw StoryverseException("Error al actualizar el usuario")
        }
    }

    /**
     * Método que se encarga de cambiar la contraseña de un usuario
     * @param changePasswordDto
     * @param token
     */
    fun changePassword(changePasswordDto: ChangePasswordDto, token:String){
        LOGGER.info("Realizando peticion para cambiar contraseña API keycloak")

        try{
            //Obtenemos el id del usuario
            val userId = getUser(token).sub

            //Creamos el http client
            val client = OkHttpClient()

            //Creamos el objectMapper
            val objectMapper = ObjectMapper()
            val json = objectMapper.writeValueAsString(changePasswordDto)

            val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())

            //Definimos la peticion
            val request = Request.Builder()
                    .url("$keycloakAccountUrl/$userId/reset-password")
                    .addHeader("Authorization", "Bearer $token")
                    .put(body)
                    .build()

            //Ejecutamos la peticion
            val response = client.newCall(request).execute()
            if (!response.isSuccessful){
                throw StoryverseException("No se pudo cambiar la contraseña, error: ${response.body!!.string()}")
            }else{
                LOGGER.info("Contraseña actualizada exitosamente")
            }
        }catch (e: Exception){
            LOGGER.severe("Error al cambiar la contraseña: ${e.message}")
            throw StoryverseException("Error al cambiar la contraseña: ${e.message}")
        }
    }

    /**
     * Método para eliminar de manera lógica la cuenta del usuario
     * @param token
     */
    fun deleteUser(token: String) {
        LOGGER.info("Realizando peticion para eliminar usuario API keycloak")

        try {
            //Obtenemos el id del usuario
            val userId = getUser(token).sub

            //Creamos el http client
            val client = OkHttpClient()

            //Definimos el body de la peticion
            val requestBody = """{"enabled": false}"""
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = requestBody.toRequestBody(mediaType)

            //Definimos la peticion
            val request = Request.Builder()
                    .url("$keycloakAccountUrl/$userId")
                    .addHeader("Authorization", "Bearer $token")
                    .put(body)
                    .build()

            //Ejecutamos la peticion
            val response = client.newCall(request).execute()

            if (!response.isSuccessful){
                LOGGER.info("Error: ${response.body!!.string()}")
                throw StoryverseException("Error al eliminar usuario")
            }else{
                LOGGER.info("Usuario eliminado")
            }
        }catch (e: Exception){
            LOGGER.severe("Error al eliminar el usuario: ${e.message}")
            throw StoryverseException("Error al eliminar el usuario")
        }

    }

    /**
     * Método para obtener todos los seguidores de un usuario
     *  @param userId
     *  @param token
     *  @return List<UserDto>
     */
    fun getFollowers(userId: String, token: String): List<UserDto> {
        LOGGER.info("Realizando peticion para obtener seguidores de un usuario")
        try {
            //Obtenemos los seguidores del usuario
            val followers = followerRepository.getFollowers(userId)
            LOGGER.info("Seguidores obtenidos")
            //Recorremos la lista y obtenemos la informacion de cada usuario
            val users = mutableListOf<UserDto>()
            followers.forEach {
                val user = getUserById(it, token)
                users.add(user)
            }
            LOGGER.info("Informacion de los seguidores obtenida")
            return users
        }catch (e: Exception){
            LOGGER.severe("Error al obtener los seguidores: ${e.message}")
            throw StoryverseException("Error al obtener los seguidores")
        }


    }
    /**
     * Método para obtener todos los seguidos de un usuario
     *  @param userId
     *  @param token
     *  @return List<UserDto>
     */
    fun getFollowing(userId: String, token: String): List<UserDto> {
        LOGGER.info("Realizando peticion para obtener seguidos de un usuario")
        try {
            //Obtenemos los seguidores del usuario
            val followed = followerRepository.getFollowing(userId)
            LOGGER.info("Seguidos obtenidos")
            //Recorremos la lista y obtenemos la informacion de cada usuario
            val users = mutableListOf<UserDto>()
            followed.forEach {
                val user = getUserById(it, token)
                users.add(user)
            }
            LOGGER.info("Informacion de los seguidos obtenida")
            return users
        }catch (e: Exception) {
            LOGGER.severe("Error al obtener los seguidos: ${e.message}")
            throw StoryverseException("Error al obtener los seguidos")
        }
    }


    /**
     * Método para seguir a un usuario
     *  @param sourceUser
     *  @param targetUser
     */
    fun followUser(sourceUser: UserDto, targetUser: UserDto) {
        LOGGER.info("Realizando peticion para seguir a un usuario")
        //Verificamos que el usuario no se este siguiendo a si mismo
        if (sourceUser.sub == targetUser.sub) {
            throw StoryverseException("No te puedes seguir a ti mismo")
        }
        //Verificamos que el usuario base no este siguiendo al usuario
        try {
            if (followerRepository.existsByFollowerIdAndUserId(sourceUser.sub, targetUser.sub)) {
                followerRepository.updateStatus(sourceUser.sub, targetUser.sub, true)
            } else {

                followerRepository.save(Follower(targetUser.sub, sourceUser.sub, Date(), true))
            }
        } catch (e: Exception) {
            followerRepository.save(Follower(targetUser.sub, sourceUser.sub, Date(), true))
        }
        //Utilizamos el repositorio de followers para guardar la relacion
        LOGGER.info("Usuario seguido")
    }

    /**
     * Método para dejar de seguir a un usuario
     * @param sourceUser
     * @param targetUser
     */
    fun unfollowUser(sourceUser: UserDto, targetUser: UserDto) {
        try {
            LOGGER.info("Realizando peticion para dejar de seguir a un usuario")
            //Utilizamos el repositorio de followers para guardar la relacion
            followerRepository.updateStatus(sourceUser.sub, targetUser.sub, false)
            LOGGER.info("Se dejo de seguir al usuario")
        }catch (e: Exception){
            LOGGER.severe("Error al dejar de seguir al usuario: ${e.message}")
            throw StoryverseException("Error al dejar de seguir al usuario")
        }

    }

    /**
     * Metodo para saber si un usuario sigue a otro
     * @param sourceUser
     * @param targetUser
     */
    fun followsUser(sourceUser: UserDto, targetUser: UserDto): Boolean {
        LOGGER.info("Realizando peticion para saber si un usuario sigue a otro")
        try {
            //Utilizamos el repositorio de followers para guardar la relacion
            val follows = followerRepository.existsByFollowerIdAndUserIdAndStatus(sourceUser.sub, targetUser.sub, true)
            LOGGER.info("Se obtuvo la informacion")
            return follows
        }catch (e: Exception){
            LOGGER.severe("Error al obtener la informacion: ${e.message}")
            throw StoryverseException("Error al obtener la informacion")
        }
    }

    /**
     * Método que parsea la respuesta de la API a una lista de PublicUserDto
     * @param response
     * @return List<PublicUserDto>
     */
    fun parseResponse(response: Response): List<PublicUserDto> {
        val mapper = jacksonObjectMapper()
        try {
            //return mapper.readValue(response.body?.string(), PublicUserDto::class.java)
            return mapper.readValue(response.body?.string()!!, object : TypeReference<List<PublicUserDto>>() {})
        } catch (e: Exception) {
            e.printStackTrace()
            LOGGER.info("Error al parsear la respuesta de la API")
            throw Exception("Error interno, parseo")
        }
    }

    /**
     * Método que parsea la respuesta de la API a un PublicUserDto
     * @param response
     * @return PublicUserDto
     */

    fun parseSingleResponse(response: Response): PublicUserDto{
        val mapper = jacksonObjectMapper()
        try {
            //return mapper.readValue(response.body?.string(), PublicUserDto::class.java)
            return mapper.readValue(response.body?.string()!!, PublicUserDto::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            LOGGER.info("Error al parsear la respuesta de la API")
            throw Exception("Error interno, parseo")
        }
    }


    //=============Anuncio==========
    /**
     * Método para publicar un anuncio
     * @param sourceUser
     * @param announcement
     */
    fun createAnnouncement(sourceUser: UserDto, announcement: String) {
        LOGGER.info("Realizando peticion para publicar un anuncio")
        try{
            //Utilizamos el repositorio de followers para guardar el anuncio
            announcementRepository.save(Announcement(sourceUser.sub, announcement, Date(), true))
            LOGGER.info("Anuncio publicado")
        }catch (e: Exception) {
            LOGGER.severe("Error al publicar el anuncio: ${e.message}")
            throw StoryverseException("Error al publicar el anuncio")
        }
    }


    /**
     * Método para obtener todos los anuncios de un usuario
     *  @param userId
     *  @return List<UserDto>
     */
    fun getAnnouncement(userId: String): List<AnnouncementDto> {
        LOGGER.info("Realizando peticion para obtener los anuncios de un usuario")
        try {
            //Obtenemos los seguidores del usuario
            val announcements = announcementRepository.findByUserIdAndStatus(userId, true)
            LOGGER.info("Anuncios obtenidos")
            //Recorremos la lista 
            val announcementsDto = mutableListOf<AnnouncementDto>()
            announcements.forEach {
                announcementsDto.add(AnnouncementDto(it.announcementId, it.userId, it.description, it.date, it.status))
            }
            return announcementsDto
        }catch (e: Exception) {
            LOGGER.severe("Error al obtener los anuncios: ${e.message}")
            throw StoryverseException("Error al obtener los anuncios")
        }

    }

    /**
     * Método para dejar de seguir a un usuario
     * @param sourceUser
     * @param targetUser
     */
    fun deleteAnnouncement(sourceUser: UserDto, announcementId: Long) {
        LOGGER.info("Realizando peticion para eliminar un anuncio")
        try{
            //Utilizamos el repositorio de announcement para eliminado logico
            announcementRepository.updateStatus(sourceUser.sub, announcementId, false)
            LOGGER.info("Se elimino el anuncio")
        }catch (e: Exception) {
            LOGGER.severe("Error al eliminar el anuncio: ${e.message}")
            throw StoryverseException("Error al eliminar el anuncio")
        }

    }


    /**
     * Prueba rabbitmq
     *
     */
    fun sendNotification(): String {
        LOGGER.info("Enviando notificacion")
        return notificationProducer.sendNotification(NotificationDto("Mensaje", "prueba", Date()))

    }

}
