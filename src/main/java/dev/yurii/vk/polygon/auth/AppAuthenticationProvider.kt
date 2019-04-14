package dev.yurii.vk.polygon.auth

import dev.yurii.vk.polygon.vkoauth2.services.VkAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken

class AppAuthenticationProvider : AuthenticationProvider {

    @Autowired
    lateinit var vkAuth: VkAuthService

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val authorizationCodeAuthentication = authentication as OAuth2AuthorizationCodeAuthenticationToken

        OAuth2AuthorizationExchangeValidator.validate(
                authorizationCodeAuthentication.authorizationExchange)

        return vkAuth.getOrCreateUser(authorizationCodeAuthentication)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication is OAuth2AuthorizationCodeAuthenticationToken
    }
}
