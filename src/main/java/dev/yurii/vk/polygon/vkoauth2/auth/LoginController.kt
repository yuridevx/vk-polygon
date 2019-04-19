package dev.yurii.vk.polygon.vkoauth2.auth

import dev.yurii.vk.polygon.vkoauth2.services.VkAuthService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login/oauth2/code")
class LoginController {


    private lateinit var vkAuth: VkAuthService;

    @GetMapping("/{registrationId}")
    fun exchangeForAppAuth(@PathVariable registrationId: String): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth is OAuth2AuthorizationCodeAuthenticationToken) {
            val appAuth = vkAuth.getOrCreateUser(auth)

            SecurityContextHolder.getContext().authentication = appAuth;

            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/").build();
        }

        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

}