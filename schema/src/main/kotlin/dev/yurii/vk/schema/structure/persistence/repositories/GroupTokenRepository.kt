package dev.yurii.vk.schema.persistence.repositories

import dev.yurii.vk.schema.persistence.entities.GroupToken
import org.springframework.data.repository.CrudRepository

interface GroupTokenRepository : CrudRepository<GroupToken, Long>