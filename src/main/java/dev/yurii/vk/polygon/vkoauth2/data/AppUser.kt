package dev.yurii.vk.polygon.vkoauth2.data

import com.vk.api.sdk.client.actors.UserActor
import dev.yurii.vk.polygon.persistence.entities.GroupToken
import dev.yurii.vk.polygon.persistence.entities.User
import dev.yurii.vk.polygon.persistence.entities.UserToken
import lombok.Getter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.util.Assert
import java.util.*

@Getter
class AppUser(
        val user: User
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

    fun ensureUserToken(): UserToken {
        Assert.notNull(user.userToken, "User token must exists")
        return user.userToken!!
    }

    fun findGroupToken(groupId: Int): GroupToken? {
        for (token in user.groupTokens) {
            if (token.groupId == groupId) {
                return token
            }
        }
        return null
    }

    fun toUserActor(): UserActor? {
        return ensureUserToken().toUserActor()
    }

}
