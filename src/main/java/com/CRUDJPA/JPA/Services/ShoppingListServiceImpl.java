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

    // Inyección de dependencia para el repositorio de usuarios
    @Autowired
    private UserRepository userRepository;

    // Inyección de dependencia para el repositorio de la lista de compras
    @Autowired
    private ShoppingListRepository shoppingListRepository;

    // Inyección de dependencia para el repositorio de los productos en la lista de compras
    @Autowired
    private ShoppingListProductsRepository shoppingListProductsRepository;

    /**
     * Encuentra todas las listas de compras.
     *
     * @return Una lista de todas las listas de compras.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShoppingList> findAll() {
        return (List<ShoppingList>) shoppingListRepository.findAll();
    }

    /**
     * Guarda una nueva lista de compras para el usuario autenticado.
     *
     * @param header El encabezado que contiene el token JWT.
     * @return Un `Optional` que contiene la lista de compras guardada si no existía previamente, o vacío si ya existe.
     */
    @Transactional
    @Override
    public Optional<ShoppingList> save(String header) {

        // Extrae el contenido del token JWT del encabezado
        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);

        // Obtiene el nombre de usuario del token
        String userName = (String) tokenContent.get("sub");

        // Busca el usuario en la base de datos por su nombre
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        User user = optionalUser.get();

        // Verifica si ya existe una lista de compras para el usuario
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());

        // Si ya existe una lista de compras para el usuario, retorna vacío
        if(optionalShoppingList.isPresent()){
            return Optional.empty();
        }

        // Crea y guarda una nueva lista de compras asociada al usuario
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setUser(user);

        return Optional.of(shoppingListRepository.save(shoppingList));
    }

    /**
     * Elimina la lista de compras del usuario autenticado, ademas elimina todas las compras relacionadas con la lista
     * de compras eliminada
     *
     * @param header El encabezado que contiene el token JWT.
     * @return Un `Optional` que contiene la lista de compras eliminada si existía, o vacío si no se encontró.
     */
    @Override
    @Transactional
    public Optional<ShoppingList> deleteShoppingList(String header) {

        // Extrae el contenido del token JWT del encabezado
        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);

        // Obtiene el nombre de usuario del token
        String userName = (String) tokenContent.get("sub");

        // Busca el usuario en la base de datos por su nombre
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        User user = optionalUser.get();

        // Busca la lista de compras asociada al usuario
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());
        if (optionalShoppingList.isEmpty()) {
            return Optional.empty();
        }

        // Obtiene la lista de compras y elimina todos los productos asociados
        ShoppingList shoppingList = optionalShoppingList.get();
        shoppingListProductsRepository.deleteAll(shoppingList.getProducts());
        shoppingListRepository.delete(shoppingList);

        return Optional.of(shoppingList);
    }


}
