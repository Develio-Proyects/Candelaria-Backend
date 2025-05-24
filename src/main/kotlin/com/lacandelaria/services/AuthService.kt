package com.lacandelaria.services

import com.lacandelaria.exceptions.BadRequestException
import com.lacandelaria.exceptions.InvalidCredentialsException
import com.lacandelaria.exceptions.NotFoundException
import com.lacandelaria.repositories.UserRepository
import com.lacandelaria.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService {
    @Autowired private lateinit var authenticationManager: AuthenticationManager
    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var jwtUtil: JwtUtil
    @Autowired private lateinit var passwordEncoder: PasswordEncoder

    fun authenticate(username: String, password: String): String {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, password)
            )
        } catch (e: AuthenticationException) { throw InvalidCredentialsException("Credenciales inválidas") }

        val user = userRepository.findByUsername(username) ?: throw NotFoundException("Usuario no encontrado")
        return jwtUtil.generateToken(user.username)
    }

    fun updatePassword(username: String, oldPassword: String, newPassword: String) {
        val user = userRepository.findByUsername(username) ?: throw NotFoundException("Usuario no encontrado")

        if (newPassword.isEmpty() || newPassword.isBlank()) throw BadRequestException("La nueva contraseña no puede estar vacía")

        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw InvalidCredentialsException("La contraseña antigua es incorrecta")
        }

        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
    }

    fun validateToken(token: String): Boolean {
        val tokenWithoutBearer = token.removePrefix("Bearer ").trim()
        val username = jwtUtil.extractUsername(tokenWithoutBearer)
        return jwtUtil.validateToken(tokenWithoutBearer, username)
    }
}