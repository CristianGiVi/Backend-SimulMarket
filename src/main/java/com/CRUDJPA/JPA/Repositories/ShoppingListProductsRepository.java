package com.CRUDJPA.JPA.Repositories;

import com.CRUDJPA.JPA.Models.ShoppingListProducts;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListProductsRepository extends CrudRepository<ShoppingListProducts, Long> {

    /**
     * Busca un producto en una lista de compras específica por sus identificadores.
     *
     * @param shoppingListId Identificador de la lista de compras.
     * @param productId Identificador del producto.
     * @return Un `Optional` que contiene el `ShoppingListProducts` si existe, o está vacío si no se encuentra.
     */
    Optional<ShoppingListProducts> findByShoppingListIdAndProductId(Long shoppingListId, Long productId);

    /**
     * Busca todos los productos en una lista de compras específica.
     *
     * @param shoppingListId Identificador de la lista de compras.
     * @return Una lista de `ShoppingListProducts` que pertenecen a la lista de compras especificada.
     */
    List<ShoppingListProducts> findByShoppingListId(Long shoppingListId);

    /**
     * Busca un producto en cualquier lista de compras por su identificador de producto.
     *
     * @param productId Identificador del producto.
     * @return Un `Optional` que contiene el `ShoppingListProducts` si existe, o está vacío si no se encuentra.
     */
    Optional<ShoppingListProducts> findByProductId(Long productId);

}
