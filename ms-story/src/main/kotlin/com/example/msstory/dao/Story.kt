package com.example.msstory.dao

import java.util.*
import javax.persistence.*

@Entity
@Table(name ="story")
@SequenceGenerator(name = "story_sequence", sequenceName = "story_sequence", allocationSize = 1)
class Story (
    @Column(name = "writer_id", nullable = false)
    var writerId: Long,
    @Column(name = "category_id", nullable = false)
    var categoryId: Long,
    @Column(name = "title", nullable = false, length = 80)
    var title: String,
    @Column(name = "url_cover", nullable = false, length = 400)
    var urlCover: String,
    @Column(name = "description", nullable = false, length = 400)
    var description: String,
    @Column(name = "audience", nullable = false, length = 100)
    var audience: String,
    @Column(name = "language", nullable = false, length = 15)
    var language: String,
    @Column(name = "publication_date", nullable = false)
    var publicationDate: Date,
    @Column(name = "votes", nullable = false)
    var votes: Long,
    @Column(name = "status", nullable = false)
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "story_sequence")
    @Column(name = "story_id")
    var storyId: Long = 0
) {
    constructor(): this(0,0,"","","","","",Date(),0,true)
}