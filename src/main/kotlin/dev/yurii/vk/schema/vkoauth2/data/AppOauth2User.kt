package dev.yurii.vk.schema.vkoauth2.data

import dev.yurii.vk.schema.relational.entities.User
import lombok.Getter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

@Getter
class AppOauth2User(
        val user: User
) : OAuth2User {

    private val authorities = ArrayList<GrantedAuthority>()
    private val attributes = HashMap<String, Any>()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getName(): String {
        return user.userName!!
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return attributes
    }
}
