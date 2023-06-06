package com.example.msstory.dao

import javax.persistence.Entity
import javax.persistence.Table
import java.util.*
import javax.persistence.*

@Entity
@Table(name ="chapter")
@SequenceGenerator(name = "chapter_sequence", sequenceName = "chapter_sequence", allocationSize = 1)
class Chapter (
    @Column(name = "story_id", nullable = false)
    var storyId: Long,
    @Column(name = "title", nullable = false, length = 100)
    var title: String,
    @Column(name = "description", nullable = false, length = 10000)
    var description: String,
    @Column(name = "publication_date", nullable = false)
    var publicationDate: Date,
    @Column(name = "status", nullable = false)
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_sequence")
    @Column(name = "chapter_id")
    var chapterId: Long = 0
) {
    constructor(): this(0,"","",Date(),true)
}