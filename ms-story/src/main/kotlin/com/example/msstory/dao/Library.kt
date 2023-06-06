package com.example.msstory.dao

import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "library")
@SequenceGenerator(name = "library_sequence", sequenceName = "library_sequence", allocationSize = 1)
class Library (
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "story_id", nullable = false)
    var storyId: Long,
    @Column(name = "date", nullable = false)
    var date: Date,
    @Column(name = "status", nullable = false)
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "library_sequence")
    @Column(name = "library_id")
    var libraryId: Long = 0
) {
    constructor(): this("",0,Date(),true)
}