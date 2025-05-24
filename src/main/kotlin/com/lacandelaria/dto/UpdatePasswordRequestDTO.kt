package com.lacandelaria.dto

data class UpdatePasswordRequestDTO(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)