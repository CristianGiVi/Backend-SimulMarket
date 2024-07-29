package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    List<User> findAll();

}
