package com.example.msstory.dao

import javax.persistence.*

@Entity
@Table(name ="category")
class Category (
    @Column(name = "name", nullable = false, length = 80)
    var name: String,
    @Column(name = "status", nullable = false)
    var status: Boolean,
    @Id
    @Column(name = "category_id")
    var categoryId: Long = 0
) {
    constructor(): this("",true)
}