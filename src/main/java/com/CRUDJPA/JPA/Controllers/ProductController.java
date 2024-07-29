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

    // Inyección de dependencia para el Servicio de productos
    @Autowired
    private ProductService productService;

    /**
     * Método para listar todos los productos.
     *
     * @return Lista de todos los productos en la base de datos.
     */
    @GetMapping
    public List<Product> list(){
        return productService.findAll();
    }

    /**
     * Método para obtener un producto por su ID.
     *
     * @param id ID del producto a buscar.
     * @return ResponseEntity con el producto si se encuentra, o un estado 404 si no se encuentra.
     */
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

    /**
     * Método para crear un nuevo producto.
     *
     * @param product Producto a crear.
     * @param bindingResult Resultados de la validación del producto.
     * @return ResponseEntity con el producto creado si es válido, o un estado 409 si el producto ya existe.
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult bindingResult){

        // Verifica si hay errores de validación en el producto
        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }

        // Verifica si el producto ya existe
        Optional<Product> productNew = productService.save(product);
        if (productNew.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("productName", "Product already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        // Retorna el producto creado con un estado 201 (Creado)
        return ResponseEntity.status(HttpStatus.CREATED).body(productNew);
    }

    /**
     * Método para actualizar un producto existente.
     *
     * @param id ID del producto a actualizar.
     * @param product Producto con la información actualizada.
     * @param bindingResult Resultados de la validación del producto.
     * @return ResponseEntity con el producto actualizado si es válido, o un estado 409 si el producto no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Product product, BindingResult bindingResult){

        if(bindingResult.hasFieldErrors()){
            return validation(bindingResult);
        }

        Optional<Product> productNew = productService.update(product,id);

        if (productNew.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("Product", "Product don't exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productNew);
    }

    /**
     * Método para eliminar un producto por su ID.
     *
     * @param id ID del producto a eliminar.
     * @return ResponseEntity con un mensaje de éxito si el producto se elimina, o un estado 404 si el producto no se encuentra.
     */
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

    /**
     * Método para validar los resultados de la validación de un producto.
     *
     * @param result Resultados de la validación del producto.
     * @return ResponseEntity con los errores de validación.
     */
    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            String message = "El campo " + err.getField() + " " + err.getDefaultMessage();
            errors.put(err.getField(), message);
        });

        return ResponseEntity.badRequest().body(errors);
    }

}




