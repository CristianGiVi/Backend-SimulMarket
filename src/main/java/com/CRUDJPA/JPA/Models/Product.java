package com.CRUDJPA.JPA.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product")
public class Product {

    // Identificador único del producto. Se genera automáticamente de manera incremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del producto. No puede estar en blanco y debe tener al menos 5 caracteres.
    @NotBlank
    @Size(min = 5)
    private String name;

    // Precio del producto. No puede ser nulo y debe ser al menos 500.
    @NotNull
    @Min(500)
    private Integer price;

    // Descripción del producto. No puede estar en blanco.
    @NotBlank
    private String description;

    public Product() {
    }

    public Product(String name, Integer price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
