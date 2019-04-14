package dev.yurii.vk.polygon.persistence.services

import dev.yurii.vk.polygon.persistence.entities.Credentials
import dev.yurii.vk.polygon.persistence.entities.User
import dev.yurii.vk.polygon.persistence.repositories.CredentialsRepository
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken
import org.springframework.stereotype.Component

@Component
class UserService(private val credentials: CredentialsRepository, private val credentialsService: CredentialsService) {

}
