package com.CRUDJPA.JPA.Repositories;

import com.CRUDJPA.JPA.Models.ShoppingList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {
    Optional<ShoppingList> findByUserId(Long userId);
}
