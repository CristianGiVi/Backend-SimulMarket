package com.CRUDJPA.JPA.Controllers;

import com.CRUDJPA.JPA.Models.User;
import com.CRUDJPA.JPA.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    // Inyección de dependencia de UserService
    @Autowired
    private UserService userService;

    /**
     * Obtiene la lista de todos los usuarios.
     *
     * @return una lista de todos los usuarios.
     */
    @GetMapping
    public List<User> list(){
        return userService.findAll();
    }

    /*
     * Ejemplo de JSON para crear un usuario de rol ADMIN:
     * {
     *     "userName": "admin",
     *     "password": "12345"
     * }
     */

    /**
     *  Crea un nuevo usuario de rol administrador.
     *
     *  @param user el usuario a ser creado.
     *  @param bindingResult resultado de la validación del usuario.
     *  @return una respuesta HTTP con el usuario creado o con errores de validación.
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult bindingResult){

        // Establece que el usuario es administrador
        user.setAdmin(true);

        // Si hay errores de validación, devuelve una respuesta con los errores
        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }

        Optional<User> savedUser = userService.save(user);
        // Si el nombre de usuario ya existe, devuelve un error de conflicto
        if (savedUser.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("userName", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        // Devuelve una respuesta con el usuario creado y el estado HTTP 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /*
     * Ejemplo de JSON para crear un usuario de rol ADMIN:
     * {
     *     "userName": "user",
     *     "password": "12345"
     * }
     */

    /**
     *  Crea un nuevo usuario normal.
     *
     *  @param user el usuario a ser registrado.
     *  @param bindingResult resultado de la validación del usuario.
     *  @return una respuesta HTTP con el usuario registrado o con errores de validación.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult){

        // Establece que el usuario no es administrador
        user.setAdmin(false);

        // Si hay errores de validación, devuelve una respuesta con los errores
        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }
        Optional<User> savedUser = userService.save(user);

        // Si el nombre de usuario ya existe, devuelve un error de conflicto
        if (savedUser.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("userName", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        // Devuelve una respuesta con el usuario registrado y el estado HTTP 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    /**
     *  Maneja los errores de validación.
     *
     *  @param result el resultado de la validación.
     *  @return una respuesta HTTP con los errores de validación.
     */
    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            String message = "El campo " + err.getField() + " " + err.getDefaultMessage();
            errors.put(err.getField(), message);
        });

        return ResponseEntity.badRequest().body(errors);
    }

}
