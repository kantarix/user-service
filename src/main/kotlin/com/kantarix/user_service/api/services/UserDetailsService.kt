package com.kantarix.user_service.api.services

import com.kantarix.user_service.api.exceptions.ApiError
import com.kantarix.user_service.api.repositories.UserRepository
import com.kantarix.user_service.security.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        userRepository
            .findByUsername(username)
            ?.let { return UserDetails(it) }
            ?: throw ApiError.USER_NOT_FOUND.toException()
    }

}