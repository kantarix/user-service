package com.kantarix.user_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TokensRequest(
    @field:NotBlank
    @JsonProperty("accessToken")
    private val _accessToken: String?,

    @field:NotNull
    @JsonProperty("refreshToken")
    private val _refreshToken: UUID?,
) {
    val accessToken: String
        get() = _accessToken!!

    val refreshToken: UUID
        get() = _refreshToken!!
}