package com.example.msuser.dao.repository
import com.example.msuser.dao.Announcement
import com.example.msuser.dto.AnnouncementDto
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.Modifying

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Service
interface AnnouncementRepository : CrudRepository<Announcement, Long> {
    @Modifying
    @Query("UPDATE Announcement set status = :status where userId = :userId and announcementId = :announcementId")
    fun updateStatus(userId: String, announcementId: Long, status: Boolean)


    fun findByUserIdAndStatus(userId: String, status: Boolean): List<Announcement>

    @Query("SELECT announcementId FROM Announcement where userId = :userId and status = true")
    fun getAnnouncementIds(userId: String): List<Long>

}
