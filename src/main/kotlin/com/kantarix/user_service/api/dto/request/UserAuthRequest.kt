package com.kantarix.user_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UserAuthRequest(
    @field:NotNull
    @field:NotBlank
    @field:Length(min = 2)
    @JsonProperty("username")
    private val _username: String?,

    @field:Length(min = 8)
    @JsonProperty("password")
    private val _password: String?,
) {
    val username: String
        get() = _username!!

    val password: String
        get() = _password!!
}