package com.kantarix.user_service.api.repositories

import com.kantarix.user_service.store.entities.OutboxMessageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OutboxMessageRepository : JpaRepository<OutboxMessageEntity, Int>