package com.lacandelaria.services

import com.lacandelaria.exceptions.InvalidCredentialsException
import com.lacandelaria.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDetailsService : UserDetailsService {
    @Autowired private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw InvalidCredentialsException("Token Inválido")
        return User(
            user.username,
            user.password,
            listOf()
        )
    }
}