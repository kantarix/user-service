package com.kantarix.user_service.api.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.kantarix.user_service.api.dto.TokenPair
import com.kantarix.user_service.api.dto.request.AuthRequest
import com.kantarix.user_service.api.dto.request.PasswordRequest
import com.kantarix.user_service.api.dto.request.RefreshTokenRequest
import com.kantarix.user_service.api.dto.request.RegisterRequest
import com.kantarix.user_service.api.events.DomainEvent
import com.kantarix.user_service.api.events.UserDeletedDomainEvent
import com.kantarix.user_service.api.exceptions.ApiError
import com.kantarix.user_service.api.repositories.OutboxMessageRepository
import com.kantarix.user_service.api.repositories.UserRepository
import com.kantarix.user_service.security.JWTUtil
import com.kantarix.user_service.security.UserDetails
import com.kantarix.user_service.store.entities.OutboxMessageEntity
import com.kantarix.user_service.store.entities.UserEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val outboxMessageRepository: OutboxMessageRepository,
    private val refreshTokenService: RefreshTokenService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil,
    private val mapper: ObjectMapper,
) {

    @Transactional
    fun register(registerRequest: RegisterRequest): TokenPair {
        if (registerRequest.password != registerRequest.confirmPassword)
            throw ApiError.PASSWORD_MISMATCH.toException()

        registerRequest.toEntity()
            .let {
                userRepository
                    .findByUsername(it.username)
                    ?.let { throw ApiError.USERNAME_ALREADY_TAKEN.toException() }
                    ?: userRepository.save(it)
            }

        return auth(AuthRequest(registerRequest.username, registerRequest.password))
    }

    @Transactional
    fun auth(authRequest: AuthRequest): TokenPair {
        val authToken = UsernamePasswordAuthenticationToken(
            authRequest.username,
            authRequest.password
        )

        val user = authenticationManager.authenticate(authToken).principal as UserDetails

        return generateTokens(user.username)
    }

    @Transactional
    fun refresh(tokenRequest: RefreshTokenRequest): TokenPair =
        refreshTokenService.findById(tokenRequest.refreshToken)
            ?.let {
                if (!refreshTokenService.isExpired(it))
                    generateTokens(it.user.username, it.id)
                else
                    throw ApiError.EXPIRED_TOKEN.toException()
            }
            ?: throw ApiError.INCORRECT_TOKEN.toException()

    private fun generateTokens(
        username: String,
        refreshToken: UUID? = null,
    ): TokenPair =
        userRepository.findByUsername(username)
            ?.let {
                val newAccessToken = jwtUtil.generateToken(it.username, it.id)
                val newRefreshToken = refreshToken
                    ?.let { refreshTokenService.updateAccessToken(token = it, accessTokenId = jwtUtil.retrieveUUID(newAccessToken)) }
                    ?: refreshTokenService.generateToken(accessTokenId = jwtUtil.retrieveUUID(newAccessToken), user = it)
                return TokenPair(
                    accessToken = newAccessToken,
                    refreshToken = newRefreshToken,
                    ttlMs = jwtUtil.tokenTtlMs,
                )
            }
            ?: throw ApiError.USER_NOT_FOUND.toException()

    @Transactional
    fun signout(accessToken: String) =
        refreshTokenService.deleteByAccessToken(
            jwtUtil.retrieveUUID(accessToken)
        )

    @Transactional
    fun deleteAccount(accessToken: String, passwordRequest: PasswordRequest) {
        jwtUtil.retrieveUserId(accessToken)
            ?.let { userRepository.findByIdOrNull(it) }
            ?.let {
                it.takeIf { passwordEncoder.matches(passwordRequest.password, it.password) }
                    ?: throw ApiError.PASSWORD_MISMATCH.toException()
            }
            ?.let {
                refreshTokenService.deleteByAccessToken(jwtUtil.retrieveUUID(accessToken))
                userRepository.delete(it)
                saveUserDeletedEvent(it.id)
            }
            ?: throw ApiError.USER_NOT_FOUND.toException()
    }

    private fun RegisterRequest.toEntity() =
        UserEntity(
            name = name,
            username = username,
            password = passwordEncoder.encode(password),
        )

    private fun DomainEvent.toJsonString() =
        mapper.writeValueAsString(this)

    private fun saveUserDeletedEvent(userId: Int) =
        outboxMessageRepository.save(
            OutboxMessageEntity(
                topic = "USER_DELETED",
                message = UserDeletedDomainEvent(userId).toJsonString()
            )
        )

}