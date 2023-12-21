package com.kantarix.user_service.security

import com.kantarix.user_service.store.entities.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetails(
    private val user: UserEntity,
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null

    override fun getPassword() = user.password

    override fun getUsername() = user.username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

}