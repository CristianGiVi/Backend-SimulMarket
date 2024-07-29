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

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @Override
    public Product save(Product product) {

        Optional<Product> optionalProduct = productRepository.findByName(product.getName());

        if (optionalProduct.isPresent()) {
            return null;
        }

        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product update(Product product, Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            return null;
        }

        Product prodDb = productOptional.orElseThrow();
        prodDb.setName(product.getName());
        prodDb.setPrice(product.getPrice());
        prodDb.setDescription(product.getDescription());

        return productRepository.save(prodDb);
    }

    @Transactional
    @Override
    public void delete(Product product) {
        Optional<Product> productOptional = productRepository.findById(product.getId());
        productOptional.ifPresent(prod -> {
            productRepository.delete(prod);
        });
    }
}
