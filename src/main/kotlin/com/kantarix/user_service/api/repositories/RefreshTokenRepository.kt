package com.kantarix.user_service.api.repositories

import com.kantarix.user_service.store.entities.RefreshTokenEntity
import com.kantarix.user_service.store.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {

    fun deleteByAccessToken(accessToken: UUID)

    fun deleteByUser(user: UserEntity)

}