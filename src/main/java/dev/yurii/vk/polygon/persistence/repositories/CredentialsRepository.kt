package dev.yurii.vk.polygon.persistence.repositories

import dev.yurii.vk.polygon.persistence.entities.Credentials
import org.springframework.data.domain.Example
import org.springframework.data.repository.CrudRepository

interface CredentialsRepository : CrudRepository<Credentials, Long> {

    fun findOne(example: Example<Credentials>): Credentials?
}
