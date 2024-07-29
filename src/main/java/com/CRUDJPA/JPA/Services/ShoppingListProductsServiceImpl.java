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

    // Inyección de dependencia para el repositorio de la lista de compras
    @Autowired
    private ShoppingListRepository shoppingListRepository;

    // Inyección de dependencia para el repositorio de los productos en la lista de compras
    @Autowired
    private ShoppingListProductsRepository shoppingListProductsRepository;

    // Inyección de dependencia para el repositorio de los productos
    @Autowired
    private ProductRepository productRepository;

    // Inyección de dependencia para el repositorio de usuarios
    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene todos los productos en listas de compras.
     *
     * @return Una lista de todos los `ShoppingListProducts`.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingListProducts> findAll() {
        return (List<ShoppingListProducts>) shoppingListProductsRepository.findAll();
    }

    /**
     * Busca un producto en la lista de compras por su identificador.
     *
     * @param id El identificador del `ShoppingListProducts`.
     * @return Un `Optional` que contiene el `ShoppingListProducts` si existe, o está vacío si no se encuentra.
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<ShoppingListProducts> findById(Long id) {
        return shoppingListProductsRepository.findById(id);
    }

    /**
     * Agrega un producto a la lista de compras del usuario especificado en el token.
     *
     * @param header El encabezado de autorización que contiene el token JWT.
     * @param productId El identificador del producto a agregar.
     * @param quantity La cantidad del producto a agregar.
     * @return Un `Optional` que contiene el `ShoppingListProducts` agregado si la operación fue exitosa, o está vacío si no se pudo agregar.
     */
    @Transactional
    public Optional<ShoppingListProducts> addProductToShoppingList(String header, Long productId, Integer quantity) {

        // Obtener la información del token JWT
        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);
        String userName = (String) tokenContent.get("sub");

        // Buscar el usuario asociado al token
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        User user = optionalUser.get();

        // Buscar la lista de compras del usuario
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());
        if(optionalShoppingList.isEmpty()){
            return Optional.empty();
        }
        ShoppingList shoppingList = optionalShoppingList.get();

        // Buscar el producto por su identificador
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }
        Product product = optionalProduct.get();

        // Verificar si el producto ya está en la lista de compras
        Optional<ShoppingListProducts> existingEntry = shoppingListProductsRepository.findByShoppingListIdAndProductId(shoppingList.getId(), product.getId());
        if (existingEntry.isPresent()) {
            return Optional.empty();
        }

        // Crear una nueva entrada para el producto en la lista de compras
        ShoppingListProducts newShoppingListProducts = new ShoppingListProducts(shoppingList, product, quantity);

        // Guardar la nueva entrada en la base de datos
        ShoppingListProducts savedProduct = shoppingListProductsRepository.save(newShoppingListProducts);
        return Optional.of(savedProduct);
    }

    /**
     * Obtiene todos los productos en la lista de compras del usuario especificado en el token.
     *
     * @param header El encabezado de autorización que contiene el token JWT.
     * @return Una lista de `ShoppingListProducts` pertenecientes a la lista de compras del usuario.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShoppingListProducts> getProductsByUser(String header) {

        // Obtener la información del token JWT
        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);
        String userName = (String) tokenContent.get("sub");

        // Buscar el usuario asociado al token
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        User user = optionalUser.get();

        // Buscar la lista de compras del usuario
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());
        if (optionalShoppingList.isEmpty()) {
            return null;
        }

        ShoppingList shoppingList = optionalShoppingList.get();

        // Obtener todos los productos de la lista de compras
        return shoppingListProductsRepository.findByShoppingListId(shoppingList.getId());
    }

    /**
     * Elimina un producto de la lista de compras del usuario especificado en el token.
     *
     * @param header El encabezado de autorización que contiene el token JWT.
     * @param productId El identificador del producto a eliminar.
     * @return Un `Optional` que contiene el `ShoppingListProducts` eliminado si la operación fue exitosa, o está vacío si no se pudo eliminar.
     */
    @Override
    @Transactional
    public Optional<ShoppingListProducts> delete(String header, Long productId) {

        // Obtener la información del token JWT
        Map<String, Object> tokenContent = JwtUtil.getClaimsMapFromToken(header);
        String userName = (String) tokenContent.get("sub");

        // Buscar el usuario asociado al token
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();

        // Buscar la lista de compras del usuario
        Optional<ShoppingList> optionalShoppingList = shoppingListRepository.findByUserId(user.getId());
        if (optionalShoppingList.isEmpty()) {
            return Optional.empty();
        }
        ShoppingList shoppingList = optionalShoppingList.get();

        // Buscar el producto en la lista de compras
        Optional<ShoppingListProducts> productOptional = shoppingListProductsRepository.findByShoppingListIdAndProductId(shoppingList.getId(), productId);
        if (productOptional.isPresent()) {
            shoppingListProductsRepository.delete(productOptional.get());
            return productOptional;
        }

        return Optional.empty();
    }




}
