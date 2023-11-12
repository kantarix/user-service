package com.kantarix.user_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class AccessTokenRequest(
    @field:NotBlank
    @JsonProperty("accessToken")
    private val _accessToken: String?,
) {
    val accessToken: String
        get() = _accessToken!!
}