package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.Role;
import com.CRUDJPA.JPA.Models.User;
import com.CRUDJPA.JPA.Repositories.RoleRepository;
import com.CRUDJPA.JPA.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    // Inyección de dependencia para el codificador de contraseñas
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inyección de dependencia para el repositorio de usuarios
    @Autowired
    private UserRepository userRepository;

    // Inyección de dependencia para el repositorio de roles
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Funcion que encuentra todos los usuarios de la base de datos
     *
     * @return una lista con todos los usuarios del sistema
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    /**
     *  Funcion que guarda un nuevo usuario después de codificar su contraseña y asignarle roles
     *
     * @param user El usuario que va a ser guardado en la base de datos
     * @return el usuario guardado, o null si ya existe un usuario con el mismo nombre de usuario.
     */
    @Transactional
    @Override
    public Optional<User> save(User user) {

        // Comprueba si ya existe un usuario con el mismo nombre de usuario
        Optional<User> existingUser = userRepository.findByUserName(user.getUserName());
        if (existingUser.isPresent()) {
            // Retorna Optional.empty() si el usuario ya existe
            return Optional.empty();
        }

        // Añade el rol ROLE_USER
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRole.ifPresent(roles::add);

        // Si el usuario es un administrador, se añade el rol ROLE_ADMIN
        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        // Establece al usuario los roles y lo habilita
        user.setRoles(roles);
        user.setEnable(true);

        // Codificar la contraseña del usuario
        String passwordEncoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);

        // Guarda y devuelve el usuario
        return Optional.of(userRepository.save(user));
    }




}
