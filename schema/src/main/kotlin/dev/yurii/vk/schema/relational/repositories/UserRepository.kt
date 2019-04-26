package dev.yurii.vk.schema.relational.repositories

import dev.yurii.vk.schema.relational.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long>
