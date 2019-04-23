package dev.yurii.vk.polygon.vkoauth2.services

import dev.yurii.vk.polygon.vkoauth2.data.AppUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User

open class AppUserService : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private lateinit var vkAuthService: AppVkAuthService

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        return AppUser(vkAuthService.getOrCreateUser(userRequest))
    }
}