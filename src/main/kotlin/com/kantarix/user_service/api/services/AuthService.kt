package com.kantarix.user_service.api.services

import com.kantarix.user_service.api.dto.request.UserAuthRequest
import com.kantarix.user_service.api.exceptions.ApiError
import com.kantarix.user_service.api.repositories.UserRepository
import com.kantarix.user_service.store.entities.UserEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class AuthService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager,
) {

    @Transactional
    fun registration(userAuthRequest: UserAuthRequest) {
        val user = userAuthRequest.toEntity()
        userRepository
            .findByUsername(user.username)
            ?.let { throw ApiError.USERNAME_ALREADY_TAKEN.toException() }
            ?: userRepository.save(user)
    }

    fun login(userAuthRequest: UserAuthRequest) {
        val authToken = UsernamePasswordAuthenticationToken(
            userAuthRequest.username,
            userAuthRequest.password
        )

        try {
            authenticationManager.authenticate(authToken)
        } catch (e: AuthenticationException) {
            throw ApiError.INCORRECT_CREDENTIALS.toException()
        }
    }

    private fun UserAuthRequest.toEntity() =
        UserEntity(
            username = username,
            password = passwordEncoder.encode(password),
        )

}