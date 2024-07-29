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

    @Autowired
    private ShoppingListService shoppingListService;

    @GetMapping
    public List<ShoppingList> list(){
        return shoppingListService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader("Authorization") String header) {

        ShoppingList  shoppingList = shoppingListService.save(header);

        if (shoppingList == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("Error", "The user already have a shopping list");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(shoppingList);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteShoppingList(@RequestHeader("Authorization") String header) {
        Optional<ShoppingList> optionalShoppingList = shoppingListService.deleteShoppingList(header);

        if (optionalShoppingList.isEmpty()) {
            return ResponseEntity.ok(optionalShoppingList.get());
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "Shopping list not found or could not be deleted");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        }
    }

}
