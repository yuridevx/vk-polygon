package dev.yurii.vk.polygon.persistence.repositories

import dev.yurii.vk.polygon.persistence.entities.GroupToken
import dev.yurii.vk.polygon.persistence.entities.UserToken
import org.springframework.data.repository.CrudRepository

interface GroupTokenRepository : CrudRepository<GroupToken, Long> {
    fun findByGroupId(userId: Int): GroupToken?
}