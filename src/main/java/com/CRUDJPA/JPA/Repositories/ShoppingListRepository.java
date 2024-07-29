package com.CRUDJPA.JPA.Repositories;

import com.CRUDJPA.JPA.Models.ShoppingList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {

    /**
     * Encuentra una lista de compras por el ID del usuario.
     *
     * @param userId El ID del usuario asociado a la lista de compras.
     * @return Un `Optional` que contiene la lista de compras si existe, o vacío si no se encuentra.
     */
    Optional<ShoppingList> findByUserId(Long userId);
}
