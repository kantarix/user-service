package com.kantarix.user_service.api.controllers

import com.kantarix.user_service.api.dto.request.UserAuthRequest
import com.kantarix.user_service.api.services.AuthService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService,
) {

    @PostMapping("/registration")
    fun registration(@Validated @RequestBody userAuth: UserAuthRequest) =
        authService.registration(userAuth)

    @PostMapping("/login")
    fun login(@Validated @RequestBody userAuth: UserAuthRequest) =
        authService.login(userAuth)

}