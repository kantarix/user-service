package com.kantarix.user_service.api.services

import com.kantarix.user_service.api.exceptions.ApiError
import com.kantarix.user_service.api.repositories.RefreshTokenRepository
import com.kantarix.user_service.store.entities.RefreshTokenEntity
import com.kantarix.user_service.store.entities.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class RefreshTokenService(
    val refreshTokenRepository: RefreshTokenRepository,

    @Value("\${jwtRefresh.tokenTtlMs}")
    val tokenTtlMs: Long,
) {

    @Transactional
    fun generateToken(accessTokenId: UUID, user: UserEntity) =
        refreshTokenRepository.deleteByUser(user)
            .let {
                RefreshTokenEntity(
                    user = user,
                    accessToken = accessTokenId,
                    expiresAt = Instant.now().plusMillis(tokenTtlMs),
                )
                    .also { refreshTokenRepository.save(it) }
                    .id
            }

    @Transactional
    fun updateAccessToken(token: UUID, accessTokenId: UUID) =
        refreshTokenRepository.findByIdOrNull(token)
            ?.let {
                RefreshTokenEntity(
                    id = it.id,
                    user = it.user,
                    accessToken = accessTokenId,
                    expiresAt = it.expiresAt,
                )
            }
            ?.also { refreshTokenRepository.save(it) }
            ?.id
            ?: throw ApiError.INCORRECT_TOKEN.toException()

    fun isExpired(token: RefreshTokenEntity) =
        (token.expiresAt < Instant.now())
            .also { if (it) refreshTokenRepository.delete(token) }

    @Transactional(readOnly = true)
    fun findById(id: UUID) =
        refreshTokenRepository.findByIdOrNull(id)

    @Transactional
    fun deleteByAccessToken(accessTokenId: UUID) =
        refreshTokenRepository.deleteByAccessToken(accessTokenId)

}