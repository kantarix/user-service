package com.kantarix.user_service.api.controllers

import com.kantarix.user_service.api.dto.request.AuthRequest
import com.kantarix.user_service.api.dto.request.PasswordRequest
import com.kantarix.user_service.api.dto.request.RefreshTokenRequest
import com.kantarix.user_service.api.dto.request.RegisterRequest
import com.kantarix.user_service.api.services.AuthService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@Tag(name = "User")
class AuthController(
    val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(@Validated @RequestBody userRegister: RegisterRequest) =
        authService.register(userRegister)

    @PostMapping("/auth")
    fun auth(@Validated @RequestBody userAuth: AuthRequest) =
        authService.auth(userAuth)

    @PostMapping("/refresh")
    fun refresh(
        @RequestHeader("X-Access-Token") accessToken: String,
        @Validated @RequestBody refreshToken: RefreshTokenRequest,
    ) = authService.refresh(refreshToken)

    @PostMapping("/signout")
    fun signout(@RequestHeader("X-Access-Token") accessToken: String) =
        authService.signout(accessToken)

    @DeleteMapping("/account")
    fun deleteAccount(
        @RequestHeader("X-Access-Token") accessToken: String,
        @Validated @RequestBody passwordRequest: PasswordRequest,
    ) = authService.deleteAccount(accessToken, passwordRequest)

}