package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.ShoppingList;

import java.util.List;
import java.util.Optional;

public interface ShoppingListService {

    /**
     * Guarda una lista de compras. El método extrae el ID del usuario del encabezado (token JWT) para asociar la lista.
     *
     * @param header El encabezado que contiene el token JWT para extraer el ID del usuario.
     * @return La lista de compras guardada.
     */
    Optional<ShoppingList> save(String header);

    /**
     * Encuentra todas las listas de compras.
     *
     * @return Una lista de todas las listas de compras.
     */
    List<ShoppingList> findAll();

    /**
     * Elimina una lista de compras utilizando el encabezado (token JWT) para identificar al usuario y la lista.
     *
     * @param header El encabezado que contiene el token JWT para extraer el ID del usuario y la lista.
     * @return Un `Optional` que contiene la lista de compras eliminada si existía, o vacío si no se encuentra.
     */
    Optional<ShoppingList> deleteShoppingList(String header);

}
