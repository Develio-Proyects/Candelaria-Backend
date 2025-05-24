package com.lacandelaria.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {
    @Value("\${jwt.secret-key}") private lateinit var secretKey: String
    @Value("\${jwt.expiration-time}") private lateinit var expirationTime: String

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationTime.toLong()))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun extractUsername(token: String): String? {
        return try {
            extractClaim(token, Claims::getSubject)
        } catch (e: JwtException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun validateToken(token: String, username: String?): Boolean {
        if (username == null) return false
        val extractedUsername = extractUsername(token)
        return extractedUsername == username && !isTokenExpired(token)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractClaim(token, Claims::getExpiration).before(Date())
    }
}