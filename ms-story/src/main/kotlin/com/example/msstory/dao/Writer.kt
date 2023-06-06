package com.example.msstory.dao

import java.util.*
import javax.persistence.*

@Entity
@Table(name ="writer")
@SequenceGenerator(name = "writer_sequence", sequenceName = "writer_sequence", allocationSize = 1)
class Writer (
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "date", nullable = false)
    var date: Date,
    @Column(name = "status", nullable = false)
    var status: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "writer_sequence")
    @Column(name = "writer_id")
    var writerId: Long = 0
) {
    constructor(): this("",Date(),true)
}