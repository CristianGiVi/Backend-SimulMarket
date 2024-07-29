package com.CRUDJPA.JPA.Services;

import com.CRUDJPA.JPA.Models.ShoppingList;
import com.CRUDJPA.JPA.Models.User;
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
public class ShoppingListServiceImpl implements ShoppingListService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingListProductsRepository shoppingListProductsRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ShoppingList> findAll() {
        return (List<ShoppingList>) shoppingListRepository.findAll();
    }

    @Transactional
    @Override
    public ShoppingList save(String header) {

        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);
        String userName = (String) tokenContent.get("sub");

        Optional<User> optionalUser = userRepository.findByUserName(userName);
        User user = optionalUser.get();

        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());

        if(optionalShoppingList.isPresent()){
            return null;
        }

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setUser(user);

        return shoppingListRepository.save(shoppingList);
    }


    @Override
    @Transactional
    public Optional<ShoppingList> deleteShoppingList(String header) {
        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);
        String userName = (String) tokenContent.get("sub");

        Optional<User> optionalUser = userRepository.findByUserName(userName);
        User user = optionalUser.get();

        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());
        if (optionalShoppingList.isEmpty()) {
            return Optional.empty();
        }

        ShoppingList shoppingList = optionalShoppingList.get();

        shoppingListProductsRepository.deleteAll(shoppingList.getProducts());
        shoppingListRepository.delete(shoppingList);

        return Optional.of(shoppingList);
    }


}
