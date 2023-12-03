package com.kantarix.user_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.validation.constraints.NotNull

data class RefreshTokenRequest(
    @field:NotNull
    @JsonProperty("refreshToken")
    private val _refreshToken: UUID?,
) {
    val refreshToken: UUID
        get() = _refreshToken!!
}