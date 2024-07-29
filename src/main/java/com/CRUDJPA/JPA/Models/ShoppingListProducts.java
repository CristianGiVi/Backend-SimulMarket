package com.CRUDJPA.JPA.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "shopping_list_products")
public class ShoppingListProducts {

    // Identificador único del producto. Se genera automáticamente de manera incremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación muchos a uno con la entidad `ShoppingList`.
     *
     * Cada producto en la lista de compras está asociado a una lista de compras específica.
     * La anotación `@JsonProperty` asegura que esta relación no sea serializada al convertir a JSON.
     */
    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ShoppingList shoppingList;

    /**
     * Relación muchos a uno con la entidad `Product`.
     *
     * Cada entrada en la lista de compras está asociada a un producto específico.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Cantidad del producto en la lista de compras.
     * No puede ser nulo y debe ser al menos 1.
     */
    @NotNull
    @Min(1)
    private Integer quantity;


    public ShoppingListProducts() {
    }

    public ShoppingListProducts(ShoppingList shoppingList, Product product, Integer quantity) {
        this.shoppingList = shoppingList;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
