package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.Product;
import com.CRUDJPA.JPA.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    // Inyección de dependencia para el repositorio de productos
    @Autowired
    private ProductRepository productRepository;

    /**
     *  Recupera todos los productos de la base de datos.
     *
     *  @return Una lista de objetos `Product` que representa todos los productos.
     */
    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    /**
     *  Encuentra un producto por su identificador en la base de datos.
     *
     *  @param id El identificador del producto que se desea encontrar.
     *  @return Un objeto `Optional<Product>` que contiene el producto si se encuentra, o vacío si no existe.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     *  Guarda un nuevo producto en la base de datos.
     *  Verifica si un producto con el mismo nombre ya existe antes de guardar.
     *
     *  @param product El objeto `Product` que se desea guardar.
     *  @return El producto guardado si la operación es exitosa y el producto no existe previamente, o `null` si ya existe.
     */
    @Transactional
    @Override
    public Optional<Product> save(Product product) {

        // Verifica si ya existe un producto con el mismo nombre
        Optional<Product> optionalProduct = productRepository.findByName(product.getName());
        if (optionalProduct.isPresent()) {
            return Optional.empty();
        }

        // Guarda el producto en la base de datos
        return Optional.of(productRepository.save(product));
    }

    /**
     * Método para actualizar un producto existente.
     *
     * @param product Producto con la información actualizada.
     * @param id ID del producto que se desea actualizar.
     * @return El producto actualizado si se encuentra, o null si el producto no se encuentra.
     */
    @Transactional
    @Override
    public Optional<Product> update(Product product, Long id) {

        // Busca el producto por ID
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            return Optional.empty();
        }

        // Actualiza los detalles del producto encontrado
        Product prodDb = productOptional.orElseThrow();
        prodDb.setName(product.getName());
        prodDb.setPrice(product.getPrice());
        prodDb.setDescription(product.getDescription());

        // Guarda los cambios en la base de datos
        return Optional.of(productRepository.save(prodDb));
    }

    /**
     * Método para eliminar un producto.
     *
     * @param product Producto a eliminar.
     */
    @Transactional
    @Override
    public void delete(Product product) {

        // Busca el producto por ID
        Optional<Product> productOptional = productRepository.findById(product.getId());

        // Si el producto se encuentra, lo elimina
        productOptional.ifPresent(prod -> {
            productRepository.delete(prod);
        });
    }
}
