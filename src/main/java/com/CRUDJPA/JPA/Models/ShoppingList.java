package com.CRUDJPA.JPA.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "shopping_list")
public class ShoppingList {

    // Identificador único del producto. Se genera automáticamente de manera incremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación muchos a uno con la entidad `User`.
     * Cada lista de compras está asociada a un usuario.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Relación uno a muchos con la entidad `ShoppingListProducts`.
     * Cada lista de compras puede contener múltiples productos.
     * La anotación `@JsonProperty` asegura que esta lista no sea serializada al convertir a JSON.
     */
    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ShoppingListProducts> products;

    public ShoppingList() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ShoppingListProducts> getProducts() {
        return products;
    }

    public void setProducts(List<ShoppingListProducts> products) {
        this.products = products;
    }
}
