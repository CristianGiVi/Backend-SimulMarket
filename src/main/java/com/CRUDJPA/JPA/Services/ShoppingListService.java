package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.ShoppingList;

import java.util.List;
import java.util.Optional;

public interface ShoppingListService {

    ShoppingList save(String header);

    List<ShoppingList> findAll();

    Optional<ShoppingList> deleteShoppingList(String header);

}
