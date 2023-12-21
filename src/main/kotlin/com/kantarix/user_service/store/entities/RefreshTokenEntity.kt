package com.kantarix.user_service.store.entities

import java.time.Instant
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "refresh_tokens")
class RefreshTokenEntity(

    @Id
    val id: UUID = UUID.randomUUID(),

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: UserEntity,

    var accessToken: UUID,

    val expiresAt: Instant,

)