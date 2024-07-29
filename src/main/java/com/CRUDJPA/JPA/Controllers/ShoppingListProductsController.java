package com.CRUDJPA.JPA.Controllers;

import com.CRUDJPA.JPA.Models.ShoppingListProducts;
import com.CRUDJPA.JPA.Services.ShoppingListProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/shoppinglistproducts")
public class ShoppingListProductsController {

    // Inyección de dependencia para el servicio de los productos en las listas de compras
    @Autowired
    private ShoppingListProductsService shoppingListProductsService;

    /*
        {
            "productId" : 1,
            "quantity" : 1
        }
    */

    /**
     * Agrega un producto a la lista de compras del usuario especificado en el encabezado.
     *
     * @param header El encabezado de autorización que contiene el token JWT.
     * @param body contiene el identificador del producto y la cantidad.
     * @return Una respuesta HTTP con el estado de la operación y el producto agregado si fue exitoso.
     */
    @PostMapping
    public ResponseEntity<?> addProductToShoppingList(@RequestHeader("Authorization") String header,
                                                      @RequestBody Map<String, Integer> body) {

        // Verificar si el cuerpo de la solicitud contiene los parámetros requeridos
        if (!body.containsKey("productId") || !body.containsKey("quantity")) {
            Map<String, String> errors = new HashMap<>();
            if (!body.containsKey("productId")) {
                errors.put("productId", "El campo 'productId' es requerido.");
            }
            if (!body.containsKey("quantity")) {
                errors.put("quantity", "El campo 'quantity' es requerido.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // Obtener los valores del cuerpo de la solicitud
        Long productId = Long.valueOf(body.get("productId"));
        Integer quantity = body.get("quantity");

        // Llamar al servicio para agregar el producto a la lista de compras
        Optional<ShoppingListProducts> addedProduct = shoppingListProductsService.addProductToShoppingList(header, productId, quantity);

        // Verificar si el producto fue agregado correctamente y devolver la respuesta adecuada
        if (addedProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct.get());
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("Error", "El producto seleccionado ya esta en tu lista de compras");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }
    }

    /**
     * Obtiene todos los productos en la lista de compras del usuario especificado en el encabezado.
     *
     * @param header El encabezado de autorización que contiene el token JWT.
     * @return Una respuesta HTTP con la lista de productos en la lista de compras del usuario.
     */
    @GetMapping
    public ResponseEntity<?> getShoppingList(@RequestHeader("Authorization") String header){

        // Obtener la lista de productos en la lista de compras del usuario
        List<ShoppingListProducts> shoppingListProducts = shoppingListProductsService.getProductsByUser(header);

        // Verificar si se encontraron productos y devolver la respuesta adecuada
        if (!shoppingListProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(shoppingListProducts);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("Error", "You commited an error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }

    }

    /**
     * Elimina un producto de la lista de compras del usuario especificado en el encabezado.
     *
     * @param id     El identificador del producto que se desea eliminar.
     * @param header El encabezado de autorización que contiene el token JWT.
     * @return Una respuesta HTTP con el estado de la operación y el producto eliminado si fue exitoso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestHeader("Authorization") String header) {

        // Verificar si el producto fue eliminado correctamente y devolver la respuesta adecuada
        Optional<ShoppingListProducts> optionalShoppingListProducts = shoppingListProductsService.delete(header, id);
        if (optionalShoppingListProducts.isPresent()) {
            return ResponseEntity.ok(optionalShoppingListProducts.get());
        }

        Map<String, String> errors = new HashMap<>();
        errors.put("error", "El producto con " + id + " encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

}
