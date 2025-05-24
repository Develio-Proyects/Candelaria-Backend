package com.lacandelaria.controllers

import com.lacandelaria.dto.LoginRequestDTO
import com.lacandelaria.dto.ResponseWithMessageDTO
import com.lacandelaria.dto.UpdatePasswordRequestDTO
import com.lacandelaria.services.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController {
    @Autowired private lateinit var authService: AuthService

    @PostMapping("/login")
    fun login(@RequestBody loginRequestDTO: LoginRequestDTO): ResponseEntity<Map<String, String>> {
        val token = authService.authenticate(loginRequestDTO.username, loginRequestDTO.password)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @PutMapping("/update-password")
    fun updatePassword(@RequestBody updatePasswordRequestDTO: UpdatePasswordRequestDTO): ResponseEntity<ResponseWithMessageDTO> {
        authService.updatePassword(
            updatePasswordRequestDTO.username,
            updatePasswordRequestDTO.oldPassword,
            updatePasswordRequestDTO.newPassword
        )
        return ResponseEntity.ok().body(ResponseWithMessageDTO("Contraseña actualizada correctamente"))
    }

    @PostMapping("/validate-token")
    fun validateToken(@RequestHeader("Authorization") token: String): ResponseEntity<Map<String, Boolean>> {
        val isValid = authService.validateToken(token)
        return ResponseEntity.ok().body(mapOf("isValid" to isValid))
    }
}