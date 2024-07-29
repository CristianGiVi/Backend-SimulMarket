package com.CRUDJPA.JPA.Repositories;

import com.CRUDJPA.JPA.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    // Método personalizado para encontrar un usuario por su nombre de usuario
    Optional<User> findByUserName(String username);
}
