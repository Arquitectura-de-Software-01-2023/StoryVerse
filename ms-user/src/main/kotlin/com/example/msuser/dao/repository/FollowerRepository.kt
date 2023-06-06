package com.example.msuser.dao.repository
import com.example.msuser.dao.Follower
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Service
interface FollowerRepository : CrudRepository<Follower, Long> {
    @Modifying
    @Query("UPDATE Follower set status = :status where followerId = :sourceId and userId = :targetId")
    fun updateStatus(sourceId: String, targetId: String, status: Boolean)

    //@Query("SELECT follow_id FROM Follower where follower_id = :sourceId and user_id = :targetId")
    //fun isRegistered(sourceId: String, targetId: String): Int
    fun existsByFollowerIdAndUserId(sourceId: String, targetId: String): Boolean

    //Funcion para obtener los seguidores de un usuario
    @Query("SELECT followerId FROM Follower where userId = :userId and status = true")
    fun getFollowers(userId: String): List<String>

    //Funcion para obtener los usuarios que sigue un usuario
    @Query("SELECT userId FROM Follower where followerId = :followerId and status = true")
    fun getFollowing(followerId: String): List<String>

    //Funcion para saber si un usuario sigue a otro
    fun existsByFollowerIdAndUserIdAndStatus(sourceId: String, targetId: String, status: Boolean): Boolean


}
