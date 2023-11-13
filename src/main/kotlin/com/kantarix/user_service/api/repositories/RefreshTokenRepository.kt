package com.kantarix.user_service.api.repositories

import com.kantarix.user_service.store.entities.RefreshTokenEntity
import com.kantarix.user_service.store.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {

    fun deleteByAccessToken(accessToken: UUID)

    fun deleteByUser(user: UserEntity)

    @Modifying
    @Query(value = "delete from refresh_tokens where expires_at < :time", nativeQuery = true)
    fun deleteAllByExpiresAtLessThan(@Param("time") time: Instant)

}