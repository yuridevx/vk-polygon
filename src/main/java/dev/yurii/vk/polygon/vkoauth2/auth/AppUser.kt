package dev.yurii.vk.polygon.vkoauth2.auth

import com.vk.api.sdk.client.actors.UserActor
import dev.yurii.vk.polygon.persistence.entities.Credentials
import dev.yurii.vk.polygon.persistence.entities.User
import lombok.Getter
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*

@Getter
class AppUser(
        val user: User,
        private val credentials: Credentials
) : OAuth2User {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.emptyList()
    }

    override fun getName(): String {
        return user.userName!!
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return Collections.emptyMap()
    }


    fun getUserActor(): UserActor {
        if (credentials.provider == Credentials.CredentialsProvider.VK_PERSONAL) {
            return UserActor(Integer.parseInt(credentials.entityId), credentials.accessToken)
        }
        throw AccessDeniedException("Must login with vk_personal token")
    }
}
