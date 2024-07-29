package com.CRUDJPA.JPA.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "user")
public class User {

    // Identificador único de cada usuario, generado automáticamente de forma ascendente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de usuario, debe ser único y no puede estar en blanco.
    @NotBlank
    @Column(unique = true, name = "user_name")
    private String userName;

    // Contraseña del usuario, no puede estar en blanco y no se serializa en la salida JSON.
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Lista de roles que tiene el usuario. Relación muchos a muchos con la entidad `Role`.
     *
     * La tabla de unión es `user_x_role`.
     * La anotación `@JsonProperty` asegura que esta lista no sea serializada al convertir a JSON.
     */
    @ManyToMany
    @JoinTable(name = "user_x_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Role> roles;

    // Campo transitorio (no se almacena en la base de datos), indica si el usuario es administrador.
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    // Indica si el usuario está habilitado o no.
    private Boolean enable;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
