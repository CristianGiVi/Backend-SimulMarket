package com.CRUDJPA.JPA.Controllers;

import com.CRUDJPA.JPA.Models.Product;
import com.CRUDJPA.JPA.Services.ProductService;
import jakarta.validation.Valid;
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
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> list(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id){
        Optional<Product> productOptional = productService.findById(id);
        if(productOptional.isPresent()){
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }


    /*
        {
            "name" : "Producto 1",
            "price" : 1000,
            "description" : "Esta es la descripcion del producto1"
        }
    */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }
        Product productNew = productService.save(product);

        if (productNew == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("productName", "Product already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productNew);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Product product, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }

        Product productNew = productService.update(product,id);

        if (productNew == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("Product", "Product don't exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productNew);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Product> productOptional = productService.findById(id);
        if(productOptional.isPresent()){
            productService.delete(productOptional.get());
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Product with ID " + id + " not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }


    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            String message = "El campo " + err.getField() + " " + err.getDefaultMessage();
            errors.put(err.getField(), message);
        });

        return ResponseEntity.badRequest().body(errors);
    }

}




