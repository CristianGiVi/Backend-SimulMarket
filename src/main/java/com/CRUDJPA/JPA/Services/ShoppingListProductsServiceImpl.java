package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.Product;
import com.CRUDJPA.JPA.Models.ShoppingList;
import com.CRUDJPA.JPA.Models.ShoppingListProducts;
import com.CRUDJPA.JPA.Models.User;
import com.CRUDJPA.JPA.Repositories.ProductRepository;
import com.CRUDJPA.JPA.Repositories.ShoppingListProductsRepository;
import com.CRUDJPA.JPA.Repositories.ShoppingListRepository;

import com.CRUDJPA.JPA.Repositories.UserRepository;
import com.CRUDJPA.JPA.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShoppingListProductsServiceImpl implements ShoppingListProductsService{

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingListProductsRepository shoppingListProductsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingListProducts> findAll() {
        return (List<ShoppingListProducts>) shoppingListProductsRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ShoppingListProducts> findById(Long id) {
        return shoppingListProductsRepository.findById(id);
    }

    @Transactional
    public Optional<ShoppingListProducts> addProductToShoppingList(String header, Long productId, Integer quantity) {


        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);
        String userName = (String) tokenContent.get("sub");

        Optional<User> optionalUser = userRepository.findByUserName(userName);
        User user = optionalUser.get();

        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());

        if(optionalShoppingList.isEmpty()){
            return Optional.empty();
        }

        ShoppingList shoppingList = optionalShoppingList.get();

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }

        Product product = optionalProduct.get();

        Optional<ShoppingListProducts> existingEntry = shoppingListProductsRepository.findByShoppingListIdAndProductId(shoppingList.getId(), product.getId());
        if (existingEntry.isPresent()) {
            return Optional.empty();
        }

        ShoppingListProducts newShoppingListProducts = new ShoppingListProducts(shoppingList, product, quantity);

        ShoppingListProducts savedProduct = shoppingListProductsRepository.save(newShoppingListProducts);
        return Optional.of(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingListProducts> getProductsByUser(String header) {

        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);
        String userName = (String) tokenContent.get("sub");

        Optional<User> optionalUser = userRepository.findByUserName(userName);
        User user = optionalUser.get();

        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());
        if (optionalShoppingList.isEmpty()) {
            return null;
        }

        ShoppingList shoppingList = optionalShoppingList.get();

        return shoppingListProductsRepository.findByShoppingListId(shoppingList.getId());
    }

    @Override
    @Transactional
    public Optional<ShoppingListProducts> delete(String header, Long productId) {

        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);
        String userName = (String) tokenContent.get("sub");

        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();

        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());
        if (optionalShoppingList.isEmpty()) {
            return Optional.empty();
        }

        ShoppingList shoppingList = optionalShoppingList.get();

        Optional<ShoppingListProducts> productOptional = shoppingListProductsRepository.findByShoppingListIdAndProductId(shoppingList.getId(), productId);
        if (productOptional.isPresent()) {
            shoppingListProductsRepository.delete(productOptional.get());
            return productOptional;
        }

        return Optional.empty();
    }




}
