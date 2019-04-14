package dev.yurii.vk.polygon.auth

import dev.yurii.vk.polygon.persistence.entities.Credentials
import dev.yurii.vk.polygon.persistence.entities.User
import lombok.Getter
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.Collections

@Getter
class AppAuthentication(val user: User, private val credentials: Credentials) : Authentication {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    override fun getCredentials(): Any {
        return credentials
    }

    override fun getDetails(): Any {
        return user
    }

    override fun getPrincipal(): Any {
        return user
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    @Throws(IllegalArgumentException::class)
    override fun setAuthenticated(isAuthenticated: Boolean) {
        throw IllegalArgumentException()
    }

    override fun getName(): String? {
        return user.userName
    }
}
