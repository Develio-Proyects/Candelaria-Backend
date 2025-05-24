package com.lacandelaria.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.lacandelaria.exceptions.InvalidCredentialsException
import com.lacandelaria.services.MyUserDetailsService
import com.lacandelaria.utils.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter : OncePerRequestFilter() {
    @Autowired private lateinit var jwtUtil: JwtUtil
    @Autowired private lateinit var userDetailsService: MyUserDetailsService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")

        var username: String? = null
        var jwt: String? = null

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            try {
                username = jwtUtil.extractUsername(jwt)
            } catch (e: SignatureException) {
                handleException(response, "Token inválido")
                return
            } catch (e: MalformedJwtException) {
                handleException(response, "Token malformado")
                return
            } catch (e: ExpiredJwtException) {
                handleException(response, "Token expirado")
                return
            } catch (e: UnsupportedJwtException) {
                handleException(response, "Token no soportado")
                return
            } catch (e: IllegalArgumentException) {
                handleException(response, "Token vacío o inválido")
                return
            } catch (e: InvalidCredentialsException) {
                handleException(response, e.message)
                return
            }
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (jwtUtil.validateToken(jwt!!, userDetails.username)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        chain.doFilter(request, response)
    }

    private fun handleException(response: HttpServletResponse, message: String?) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        val responseBody = objectMapper.writeValueAsString(mapOf("error" to (message ?: "Unauthorized")))
        response.writer.write(responseBody)
    }

    companion object {
        private val objectMapper = ObjectMapper()
    }
}