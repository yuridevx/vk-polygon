package dev.yurii.vk.polygon.persistence.repositories

import dev.yurii.vk.polygon.persistence.entities.UserToken
import org.springframework.data.repository.CrudRepository

interface UserTokenRepository : CrudRepository<UserToken, Long> {
    fun findByUserId(userId: Int): UserToken?
}
