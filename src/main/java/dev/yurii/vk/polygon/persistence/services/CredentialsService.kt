package dev.yurii.vk.polygon.persistence.services

import dev.yurii.vk.polygon.persistence.repositories.CredentialsRepository
import org.springframework.stereotype.Component

@Component
class CredentialsService(val credentials: CredentialsRepository, var users: UserService) {

}
