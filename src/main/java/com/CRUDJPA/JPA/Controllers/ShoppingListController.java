package com.CRUDJPA.JPA.Controllers;

import com.CRUDJPA.JPA.Models.ShoppingList;
import com.CRUDJPA.JPA.Services.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/shoppinglist")
public class ShoppingListController {

    // Inyección de dependencia para el Servicio de la lista de compras
    @Autowired
    private ShoppingListService shoppingListService;

    /**
     * Obtiene todas las listas de compras.
     *
     * @return Una lista de todas las listas de compras.
     */
    @GetMapping
    public List<ShoppingList> list(){
        return shoppingListService.findAll();
    }

    /**
     * Crea una nueva lista de compras para el usuario autenticado.
     *
     * @param header El encabezado que contiene el token JWT.
     * @return Una respuesta que indica el resultado de la operación.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestHeader("Authorization") String header) {

        // Llama al servicio para guardar una nueva lista de compras
        Optional<ShoppingList>  shoppingList = shoppingListService.save(header);

        if (shoppingList.isEmpty()) {
            // Si ya existe una lista de compras para el usuario, retorna un conflicto
            Map<String, String> errors = new HashMap<>();
            errors.put("Error", "The user already have a shopping list");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        // Retorna una respuesta exitosa con la lista de compras creada
        return ResponseEntity.status(HttpStatus.CREATED).body(shoppingList);

    }

    /**
     * Elimina la lista de compras del usuario autenticado y si la lista de compras ya tiene productos enlazaaos, tambien
     * se borran del repositorio ShoppingListProducts
     *
     * @param header El encabezado que contiene el token JWT.
     * @return Una respuesta que indica el resultado de la operación.
     */
    @DeleteMapping
    public ResponseEntity<?> deleteShoppingList(@RequestHeader("Authorization") String header) {

        // Llama al servicio para eliminar la lista de compras
        Optional<ShoppingList> optionalShoppingList = shoppingListService.deleteShoppingList(header);

        if (optionalShoppingList.isEmpty()) {
            // Si no se encuentra la lista de compras, retorna una respuesta de no encontrado
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "Lista de compras no encontrada o no se pudo eliminar");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }

        // Retorna una respuesta exitosa con la lista de compras eliminada
        return ResponseEntity.ok(optionalShoppingList.get());
    }

}
