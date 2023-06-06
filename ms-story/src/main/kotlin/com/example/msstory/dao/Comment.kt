package com.example.msstory.dao

import java.util.*
import javax.persistence.*

@Entity
@Table(name ="comment")
@SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence", allocationSize = 1)
class Comment (
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "chapter_id", nullable = false)
    var chapterId: Long,
    @Column(name = "description", nullable = false, length = 400)
    var description: String,
    @Column(name = "date", nullable = false)
    var date: Date,
    @Column(name = "status", nullable = false)
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence")
    @Column(name = "comment_id")
    var commentId: Long = 0
) {
    constructor(): this("",0,"",Date(),true)
}