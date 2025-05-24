package com.lacandelaria.domain

import jakarta.persistence.*

@Entity
@Table(name = "Users")
data class User (
    @Column
    var username: String,
    @Column
    var password: String
) {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String
}