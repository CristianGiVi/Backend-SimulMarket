package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.ShoppingListProducts;

import java.util.List;
import java.util.Optional;

public interface ShoppingListProductsService {

    List<ShoppingListProducts> findAll();

    Optional<ShoppingListProducts> addProductToShoppingList(String header, Long productId, Integer quantity);

    List<ShoppingListProducts> getProductsByUser(String header);

    Optional<ShoppingListProducts> delete(String header, Long productId);

    Optional<ShoppingListProducts> findById(Long id);

}
