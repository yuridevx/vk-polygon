package dev.yurii.vk.polygon.persistence.repositories

import dev.yurii.vk.polygon.persistence.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long>
