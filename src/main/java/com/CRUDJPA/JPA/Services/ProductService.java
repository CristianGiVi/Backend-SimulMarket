package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * Obtiene una lista de todos los productos en la base de datos.
     *
     * @return Una lista de objetos `Product` que representa todos los productos.
     */
    List<Product> findAll();


    /**
     * Busca un producto por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return Un objeto `Optional<Product>` que contiene el producto si se encuentra, o vacío si no se encuentra.
     */
    Optional<Product> findById(Long id);

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @param product El objeto `Product` que se desea guardar.
     * @return El objeto `Product` guardado, que puede incluir un ID generado si es un nuevo producto.
     */
    Optional<Product> save(Product product);

    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param product El objeto `Product` con los datos actualizados.
     * @param id El ID del producto que se desea actualizar.
     * @return El objeto `Product` actualizado.
     */
    Optional<Product> update(Product product, Long id);

    /**
     * Elimina un producto de la base de datos.
     *
     * @param product El objeto `Product` que se desea eliminar.
     */
    void delete(Product product);
}
