package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.ShoppingListProducts;

import java.util.List;
import java.util.Optional;

public interface ShoppingListProductsService {

    /**
     * Obtiene todos los productos en listas de compras.
     *
     * @return Una lista de todos los `ShoppingListProducts`.
     */
    List<ShoppingListProducts> findAll();

    /**
     * Agrega un producto a una lista de compras específica.
     *
     * @param header El encabezado de autorización que contiene el token JWT.
     * @param productId El identificador del producto a agregar.
     * @param quantity La cantidad del producto a agregar.
     * @return Un `Optional` que contiene el `ShoppingListProducts` agregado si la operación fue exitosa, o está vacío si no se pudo agregar.
     */
    Optional<ShoppingListProducts> addProductToShoppingList(String header, Long productId, Integer quantity);

    /**
     * Obtiene todos los productos de la lista de compras del usuario especificado en el token.
     *
     * @param header El encabezado de autorización que contiene el token JWT.
     * @return Una lista de `ShoppingListProducts` pertenecientes a la lista de compras del usuario.
     */
    List<ShoppingListProducts> getProductsByUser(String header);

    /**
     * Elimina un producto de la lista de compras.
     *
     * @param header El encabezado de autorización que contiene el token JWT.
     * @param productId El identificador del producto a eliminar.
     * @return Un `Optional` que contiene el `ShoppingListProducts` eliminado si la operación fue exitosa, o está vacío si no se pudo eliminar.
     */
    Optional<ShoppingListProducts> delete(String header, Long productId);

    /**
     * Busca un producto en una lista de compras por su identificador.
     *
     * @param id El identificador del `ShoppingListProducts`.
     * @return Un `Optional` que contiene el `ShoppingListProducts` si existe, o está vacío si no se encuentra.
     */
    Optional<ShoppingListProducts> findById(Long id);

}
