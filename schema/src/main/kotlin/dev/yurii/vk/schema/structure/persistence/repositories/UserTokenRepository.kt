package dev.yurii.vk.schema.persistence.repositories

import dev.yurii.vk.schema.persistence.entities.UserToken
import org.springframework.data.repository.CrudRepository

interface UserTokenRepository : CrudRepository<UserToken, Long> {
    fun findByUserId(userId: Int): UserToken?
}
