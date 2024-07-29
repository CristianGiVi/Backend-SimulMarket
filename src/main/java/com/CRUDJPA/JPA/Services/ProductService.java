package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    Product update(Product product, Long id);

    void delete(Product product);
}
