package com.kantarix.user_service.api.controllers

import com.kantarix.user_service.api.dto.request.AccessTokenRequest
import com.kantarix.user_service.api.dto.request.TokensRequest
import com.kantarix.user_service.api.dto.request.UserAuthRequest
import com.kantarix.user_service.api.dto.request.UserRegisterRequest
import com.kantarix.user_service.api.services.AuthService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthController(
    val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(@Validated @RequestBody userRegister: UserRegisterRequest) =
        authService.register(userRegister)

    @PostMapping("/auth")
    fun auth(@Validated @RequestBody userAuth: UserAuthRequest) =
        authService.auth(userAuth)

    @PostMapping("/refresh")
    fun refresh(@Validated @RequestBody tokens: TokensRequest) =
        authService.refresh(tokens)

    @PostMapping("/signout")
    fun signout(@Validated @RequestBody token: AccessTokenRequest) =
        authService.signout(token)

}