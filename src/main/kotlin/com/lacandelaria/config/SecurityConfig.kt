package com.lacandelaria.config

import com.lacandelaria.services.MyUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Autowired private lateinit var jwtRequestFilter: JwtRequestFilter
    @Value("\${spring.url.front}") private lateinit var allowOrigin: String

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .cors { }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(
                        HttpMethod.POST,
                        "/api/products",
                        "/api/categories"
                    ).authenticated()
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/api/products/**",
                        "/api/categories/**",
                        "/api/auth/update-password"
                    ).authenticated()
                    .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/products/**",
                        "/api/categories/**"
                    ).authenticated()
                    .anyRequest().permitAll()
            }
            .headers { headers ->
                headers.frameOptions { frameOptions ->
                    frameOptions.sameOrigin() // Necesario para la UI de H2
                }
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sesiones sin estado para JWT
            }
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java) // Filtro JWT

        return http.build()
    }

    @Bean
    fun authenticationManager(
        http: HttpSecurity,
        passwordEncoder: PasswordEncoder,
        userDetailsService: MyUserDetailsService
    ): AuthenticationManager {
        val auth = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
        return auth.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() // Usar BCrypt para codificar las contraseñas
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf(allowOrigin)
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}