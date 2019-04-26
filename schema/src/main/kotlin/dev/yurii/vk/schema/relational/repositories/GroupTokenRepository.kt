package dev.yurii.vk.schema.relational.repositories

import dev.yurii.vk.schema.relational.entities.GroupToken
import org.springframework.data.repository.CrudRepository

interface GroupTokenRepository : CrudRepository<GroupToken, Long>