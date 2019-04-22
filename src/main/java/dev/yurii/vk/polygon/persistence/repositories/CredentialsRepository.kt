package dev.yurii.vk.polygon.persistence.repositories

import dev.yurii.vk.polygon.persistence.entities.Credentials
import org.springframework.data.repository.CrudRepository

interface CredentialsRepository : CrudRepository<Credentials, Long> {
    fun findByProviderAndEntityId(provider: Credentials.CredentialsProvider?, entityId: String?): Credentials?
}
