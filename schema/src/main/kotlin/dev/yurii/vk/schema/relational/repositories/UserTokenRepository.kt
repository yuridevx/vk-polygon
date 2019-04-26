package dev.yurii.vk.schema.relational.repositories

import dev.yurii.vk.schema.relational.entities.UserToken
import org.springframework.data.repository.CrudRepository

interface UserTokenRepository : CrudRepository<UserToken, Long> {
    fun findByUserId(userId: Int): UserToken?
}
