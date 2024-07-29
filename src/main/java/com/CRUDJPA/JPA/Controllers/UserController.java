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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> list(){
        return userService.findAll();
    }



    /*
        {
            "userName": "admin",
            "password": "12345"
        }
    */

    // Create Admin

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult bindingResult){
        user.setAdmin(true);
        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }
        User savedUser = userService.save(user);
        if (savedUser == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("userName", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Create User

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult){
        user.setAdmin(false);
        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }
        User savedUser = userService.save(user);
        if (savedUser == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("userName", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            String message = "El campo " + err.getField() + " " + err.getDefaultMessage();
            errors.put(err.getField(), message);
        });

        return ResponseEntity.badRequest().body(errors);
    }

}
