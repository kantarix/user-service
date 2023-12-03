package com.kantarix.user_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length

data class PasswordRequest (
    @field:Length(min = 8)
    @JsonProperty("password")
    private val _password: String?,
) {
    val password: String
        get() = _password!!
}