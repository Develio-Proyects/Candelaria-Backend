package com.lacandelaria.domain

import jakarta.persistence.*

@Entity
class Category (
    @Column
    var name: String,
    @Column
    var position: Int
) {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String
}