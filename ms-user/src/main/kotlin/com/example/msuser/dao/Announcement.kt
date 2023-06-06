package com.example.msuser.dao
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "announcement")
class Announcement (
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "description", nullable = false)
    var description: String,
    var date: Date,
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "announcement_id", nullable = false)
    var announcementId: Long = 0
)
{
    constructor(): this("", "", Date(), true)
}
