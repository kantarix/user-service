package com.kantarix.user_service.api.exceptions

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.kantarix.user_service.api.dto.ErrorDto
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {  }

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<ErrorDto> {
        log.debug(ex) { ex.message }
        return ResponseEntity.status(ex.httpStatus).body(
            ErrorDto(code = ex.code, messages = listOf(ex.message))
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorDto> {
        val allErrors = ex.bindingResult.allErrors
        val errorMessages = mutableListOf<String>()

        for (error in allErrors)
            error.defaultMessage?.let { errorMessages.add(it) }

        return ResponseEntity.badRequest().body(
            ErrorDto(code = "VALIDATION_EXCEPTION", messages = errorMessages)
        )
    }

    @ExceptionHandler(JWTVerificationException::class)
    fun handleVerificationException(ex: JWTVerificationException): ResponseEntity<ErrorDto> {
        log.debug(ex) { ex.message }
        val apiError = when (ex) {
            TokenExpiredException::class -> ApiError.EXPIRED_TOKEN
            else -> ApiError.INCORRECT_TOKEN
        }
        return handleApiException(apiError.toException())
    }

}