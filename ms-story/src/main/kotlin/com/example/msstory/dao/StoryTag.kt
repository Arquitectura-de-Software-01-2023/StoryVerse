package com.example.msstory.dao

import javax.persistence.*

@Entity
@Table(name ="story_tag")
@SequenceGenerator(name = "story_tag_sequence", sequenceName = "story_tag_sequence", allocationSize = 1)
class StoryTag (
    @Column(name = "story_id", nullable = false)
    var storyId: Long,
    @Column(name = "tag_id", nullable = false)
    var tagId: Long,
    @Column(name = "status", nullable = false)
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "story_tag_sequence")
    @Column(name = "story_tag_id")
    var storyTagId: Long = 0
) {
    constructor(): this(0,0,true)
}