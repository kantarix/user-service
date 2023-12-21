package com.kantarix.user_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank
    @field:Length(min = 2)
    @JsonProperty("name")
    private val _name: String?,

    @field:NotBlank
    @field:Length(min = 2)
    @JsonProperty("username")
    private val _username: String?,

    @field:Length(min = 8)
    @JsonProperty("password")
    private val _password: String?,

    @field:Length(min = 8)
    @JsonProperty("confirmPassword")
    private val _confirmPassword: String?,
) {
    val name: String
        get() = _name!!

    val username: String
        get() = _username!!

    val password: String
        get() = _password!!

    val confirmPassword: String
        get() = _confirmPassword!!
}