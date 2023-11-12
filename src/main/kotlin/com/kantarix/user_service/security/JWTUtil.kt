package com.kantarix.user_service.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import java.util.UUID


@Component
class JWTUtil(
    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.tokenTtlMs}")
    val tokenTtlMs: Long,
) {

    fun generateToken(username: String) =
        JWT.create()
            .withSubject("user details")
            .withClaim(usernameClaim, username)
            .withClaim(uuidClaim, UUID.randomUUID().toString())
            .withIssuedAt(Date())
            .withExpiresAt(Instant.now().plusMillis(tokenTtlMs))
            .sign(Algorithm.HMAC256(secret))

    fun validateToken(token: String): DecodedJWT =
        JWT.require(Algorithm.HMAC256(secret))
            .withSubject("user details")
            .build()
            .verify(token)

    fun validateTokenAndRetrieveClaim(token: String, claim: String) =
        validateToken(token)
            .getClaim(claim)
            .asString()

    fun retrieveUsername(token: String) =
        validateTokenAndRetrieveClaim(token, usernameClaim)

    fun retrieveUUID(token: String) =
        UUID.fromString(validateTokenAndRetrieveClaim(token, uuidClaim))

    companion object {
        private const val usernameClaim = "username"
        private const val uuidClaim = "uuid"
    }

}