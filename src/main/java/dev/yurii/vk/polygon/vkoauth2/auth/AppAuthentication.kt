package dev.yurii.vk.polygon.vkoauth2.auth

import com.vk.api.sdk.client.actors.UserActor
import dev.yurii.vk.polygon.persistence.entities.Credentials
import dev.yurii.vk.polygon.persistence.entities.User
import lombok.Getter
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

@Getter
class AppAuthentication(val user: User, private val credentials: Credentials) : Authentication {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    override fun getCredentials(): Credentials {
        return credentials
    }

    override fun getDetails(): User {
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

    fun getUserActor(): UserActor {
        if (credentials.provider != Credentials.CredentialsProvider.VK_PERSONAL) {
            return UserActor(Integer.parseInt(credentials.entityId), credentials.accessToken)
        }
        throw AccessDeniedException("Must login with vk_personal token")
    }
}
