package com.example.msuser.dao
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "follower")
class Follower(
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "follower_id", nullable = false)
    var followerId: String,
    var date: Date,
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "follow_id", nullable = false)
    var followId: Long = 0
)

{
    constructor(): this("", "", Date(), true)
}