package com.kantarix.user_service.api.exceptions

import org.springframework.http.HttpStatus

enum class ApiError(
    private val httpStatus: HttpStatus,
    private val message: String,
) {
    USERNAME_ALREADY_TAKEN(
        HttpStatus.CONFLICT,
        "This username is already taken."
    ),
    PASSWORD_MISMATCH(
        HttpStatus.BAD_REQUEST,
        "Password mismatch."
    ),
    USER_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "User not found."
    ),
    INCORRECT_TOKEN(
        HttpStatus.BAD_REQUEST,
        "Token does not exist."
    ),
    EXPIRED_TOKEN(
        HttpStatus.BAD_REQUEST,
        "Token was expired."
    ),
    ;

    fun toException() =
        ApiException(
            httpStatus = httpStatus,
            code = name,
            message = message,
        )

}
