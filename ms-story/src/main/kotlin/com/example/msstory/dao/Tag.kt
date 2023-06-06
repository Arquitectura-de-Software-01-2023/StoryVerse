package com.example.msstory.dao

import javax.persistence.*

@Entity
@Table(name ="tag")
@SequenceGenerator(name = "tag_sequence", sequenceName = "tag_sequence", allocationSize = 1)
class Tag (
    @Column(name = "name", nullable = false, length = 100)
    var name: String,
    @Column(name = "status", nullable = false)
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_sequence")
    @Column(name = "tag_id")
    var tagId: Long = 0
) {
    constructor(): this("",true)
}