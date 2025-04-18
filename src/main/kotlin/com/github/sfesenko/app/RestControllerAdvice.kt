package com.github.sfesenko.app

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import java.time.Instant

@RestControllerAdvice
class RestControllerAdvice {

    /**
     * Customize validation error
     */
    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleValidationExceptions(ex: HandlerMethodValidationException): ResponseEntity<Map<String, Any>> {
        val errors = ex.parameterValidationResults.flatMap { result ->
            result.resolvableErrors.map { error ->
                error.defaultMessage ?: "Validation failed"
            }
        }

        val body = mapOf(
            "timestamp" to Instant.now(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to HttpStatus.BAD_REQUEST.reasonPhrase,
            "message" to errors.joinToString(", ")
        )

        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }
}