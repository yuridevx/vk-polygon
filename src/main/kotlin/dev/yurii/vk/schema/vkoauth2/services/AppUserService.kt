package dev.yurii.vk.schema.vkoauth2.services

import com.vk.api.sdk.objects.UserAuthResponse
import dev.yurii.vk.schema.relational.services.AppAuthService
import dev.yurii.vk.schema.vkoauth2.data.AppOauth2User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User

class AppUserService : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private lateinit var authService: AppAuthService

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val token = userRequest.additionalParameters["vkToken"] as UserAuthResponse
        val user = authService.getOrCreateUser(token)

        return AppOauth2User(user)
    }
}