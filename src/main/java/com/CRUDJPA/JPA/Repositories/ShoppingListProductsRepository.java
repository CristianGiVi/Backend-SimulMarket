package com.CRUDJPA.JPA.Repositories;

import com.CRUDJPA.JPA.Models.Product;
import com.CRUDJPA.JPA.Models.ShoppingListProducts;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListProductsRepository extends CrudRepository<ShoppingListProducts, Long> {

    Optional<ShoppingListProducts> findByShoppingListIdAndProductId(Long shoppingListId, Long productId);

    List<ShoppingListProducts> findByShoppingListId(Long shoppingListId);

    Optional<ShoppingListProducts> findByProductId(Long productId);

}
