package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param user El objeto `User` que se desea guardar.
     * @return Un objeto `Optional<User>` que contiene el usuario guardado
     */
    Optional<User> save(User user);

    /**
     * Encuentra todos los usuarios en la base de datos.
     *
     * @return Una lista de objetos `User` que representa todos los usuarios.
     */
    List<User> findAll();

}
