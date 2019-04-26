package dev.yurii.vk.schema.persistence.repositories

import dev.yurii.vk.schema.persistence.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long>
