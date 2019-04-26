package dev.yurii.vk.schema.vkoauth2.data

import dev.yurii.vk.schema.persistence.entities.User
import lombok.Getter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

@Getter
class AppUser(
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
