package dev.yurii.vk.polygon.persistence.repositories;

import dev.yurii.vk.polygon.persistence.entities.Credentials;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    Credentials findOne(Example<Credentials> example);
}
