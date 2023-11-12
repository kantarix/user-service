package com.kantarix.user_service.api.dto

import java.util.UUID

data class Tokens(
    val accessToken: String,
    val refreshToken: UUID,
    val ttlMs: Long,
)