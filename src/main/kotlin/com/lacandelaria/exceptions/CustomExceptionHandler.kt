package com.lacandelaria.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParams(ex: MissingServletRequestParameterException): ResponseEntity<Map<String, String>> {
        val errorDetails = mapOf(
            "error" to "Parámetro faltante",
            "message" to "El parámetro '${ex.parameterName}' es obligatorio."
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errorDetails = mapOf(
            "error" to "Parámetros inválidos",
            "message" to "Parametros recibidos inválidos."
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<Map<String, String>> {
        val message = ex.message ?: "Ha ocurrido un error con la solicitud."
        val errorDetails = mapOf(
            "error" to "Solicitud incorrecta",
            "message" to message
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<Map<String, String>> {
        val message = ex.message ?: "Un recurso solicitado no fue encontrado."
        val errorDetails = mapOf(
            "error" to "Recurso no encontrado",
            "message" to message
        )
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentialsException(ex: InvalidCredentialsException): ResponseEntity<Map<String, String>> {
        val message = ex.message ?: "Credenciales inválidas."
        val errorDetails = mapOf(
            "error" to "No autorizado",
            "message" to message
        )
        return ResponseEntity(errorDetails, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Map<String, String>> {
        val message = "Parametro ${ex.parameter.parameterName} con tipo de dato incorrecto."
        val errorDetails = mapOf(
            "error" to "Parámetros inválidos",
            "message" to message
        )
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }
}