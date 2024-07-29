package com.CRUDJPA.JPA.Repositories;

import com.CRUDJPA.JPA.Models.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByName(String name);
}
