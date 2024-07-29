package com.CRUDJPA.JPA.Controllers;

import com.CRUDJPA.JPA.Models.ShoppingListProducts;
import com.CRUDJPA.JPA.Services.ShoppingListProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/shoppinglistproducts")
public class ShoppingListProductsController {

    @Autowired
    private ShoppingListProductsService shoppingListProductsService;

    @PostMapping
    public ResponseEntity<?> addProductToShoppingList(@RequestHeader("Authorization") String header,
                                                      @RequestBody Map<String, Integer> body) {

        Long productId = Long.valueOf(body.get("productId"));
        Integer quantity = body.get("quantity");

        Optional<ShoppingListProducts> addedProduct = shoppingListProductsService.addProductToShoppingList(header, productId, quantity);

        if (addedProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct.get());
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("Error", "You commited an error with the parametes submited");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }
    }


    @GetMapping
    public ResponseEntity<?> getShoppingList(@RequestHeader("Authorization") String header){

        List<ShoppingListProducts> shoppingListProducts = shoppingListProductsService.getProductsByUser(header);

        if (!shoppingListProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(shoppingListProducts);
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("Error", "You commited an error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestHeader("Authorization") String header) {

        Optional<ShoppingListProducts> optionalShoppingListProducts = shoppingListProductsService.delete(header, id);
        if (optionalShoppingListProducts.isPresent()) {
            return ResponseEntity.ok(optionalShoppingListProducts.get());
        }

        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Product with ID " + id + " not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

}
