package com.kantarix.user_service.api.services

import com.kantarix.user_service.api.dto.Tokens
import com.kantarix.user_service.api.dto.request.AccessTokenRequest
import com.kantarix.user_service.api.dto.request.TokensRequest
import com.kantarix.user_service.api.dto.request.UserAuthRequest
import com.kantarix.user_service.api.dto.request.UserRegisterRequest
import com.kantarix.user_service.api.exceptions.ApiError
import com.kantarix.user_service.api.repositories.UserRepository
import com.kantarix.user_service.security.JWTUtil
import com.kantarix.user_service.security.UserDetails
import com.kantarix.user_service.store.entities.UserEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AuthService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager,
    val jwtUtil: JWTUtil,
    val refreshTokenService: RefreshTokenService,
) {

    @Transactional
    fun register(userRegisterRequest: UserRegisterRequest): Tokens {
        if (userRegisterRequest.password != userRegisterRequest.confirmPassword)
            throw ApiError.PASSWORD_MISMATCH.toException()

        userRegisterRequest.toEntity()
            .let {
                userRepository
                    .findByUsername(it.username)
                    ?.let { throw ApiError.USERNAME_ALREADY_TAKEN.toException() }
                    ?: userRepository.save(it)
            }

        return auth(UserAuthRequest(userRegisterRequest.username, userRegisterRequest.password))
    }

    @Transactional
    fun auth(userAuthRequest: UserAuthRequest): Tokens {
        val authToken = UsernamePasswordAuthenticationToken(
            userAuthRequest.username,
            userAuthRequest.password
        )

        val user = authenticationManager.authenticate(authToken).principal as UserDetails

        return generateTokens(user.username)
    }

    @Transactional
    fun refresh(tokens: TokensRequest): Tokens =
        refreshTokenService.findById(tokens.refreshToken)
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
    ): Tokens =
        userRepository.findByUsername(username)
            ?.let {
                val newAccessToken = jwtUtil.generateToken(username)
                val newRefreshToken = refreshToken
                    ?.let { refreshTokenService.updateAccessToken(token = it, accessTokenId = jwtUtil.retrieveUUID(newAccessToken)) }
                    ?: refreshTokenService.generateToken(accessTokenId = jwtUtil.retrieveUUID(newAccessToken), user = it)
                return Tokens(
                    accessToken = newAccessToken,
                    refreshToken = newRefreshToken,
                    ttlMs = jwtUtil.tokenTtlMs,
                )
            }
            ?: throw ApiError.USER_NOT_FOUND.toException()

    @Transactional
    fun signout(accessTokenRequest: AccessTokenRequest) =
        refreshTokenService.deleteByAccessToken(
            jwtUtil.retrieveUUID(accessTokenRequest.accessToken)
        ).also { println("hello") }

    private fun UserRegisterRequest.toEntity() =
        UserEntity(
            name = name,
            username = username,
            password = passwordEncoder.encode(password),
        )

}