package com.kantarix.user_service.api.repositories

import com.kantarix.user_service.store.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Int> {

    fun findByUsername(username: String): UserEntity?

}