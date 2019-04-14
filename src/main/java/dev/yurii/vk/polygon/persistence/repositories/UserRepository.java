package dev.yurii.vk.polygon.persistence.repositories;

import dev.yurii.vk.polygon.persistence.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
