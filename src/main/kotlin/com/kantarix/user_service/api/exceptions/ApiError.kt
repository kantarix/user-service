package com.kantarix.user_service.api.exceptions

import org.springframework.http.HttpStatus

enum class ApiError(
    val httpStatus: HttpStatus,
    val message: String,
) {
    USERNAME_ALREADY_TAKEN(
        HttpStatus.CONFLICT,
        "This username is already taken."
    ),
    INCORRECT_CREDENTIALS(
        HttpStatus.BAD_REQUEST,
        "Incorrect credentials."
    ),
    USER_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "User not found."
    ),
    ;

    fun toException() =
        ApiException(
            httpStatus = httpStatus,
            code = name,
            message = message,
        )

}
