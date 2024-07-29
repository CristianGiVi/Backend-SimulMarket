package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.User;
import com.CRUDJPA.JPA.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de detalles de usuario para la autenticación en Spring Security.
 * Esta clase se encarga de cargar los detalles de un usuario basado en el nombre de usuario.
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    // Inyección de dependencia del repositorio de usuarios
    @Autowired
    private UserRepository userRepository;

    /**
     * Carga los detalles de un usuario basado en su nombre de usuario.
     *
     * @param username El nombre de usuario para el cual se están cargando los detalles.
     * @return Los detalles del usuario que incluyen credenciales y roles.
     * @throws UsernameNotFoundException Si el nombre de usuario no se encuentra en el sistema.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscar el usuario en el repositorio utilizando el nombre de usuario
        Optional<User> userOptional = userRepository.findByUserName(username);

        // Lanzar una excepción si el usuario no se encuentra
        if(!userOptional.isPresent()){
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));
        }

        User user = userOptional.orElseThrow();

        // Convertir los roles del usuario a una lista de autoridades de Spring Security
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        // Devolver los detalles del usuario para la autenticación
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                user.getEnable(),
                true,
                true,
                true,
                authorities);
    }
}
