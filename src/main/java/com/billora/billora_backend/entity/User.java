package com.billora.billora_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"username\"")   // keep this as it is
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")           // ✅ NEW
    private String role;             // CUSTOMER / CASHIER

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ✅ NEW GETTERS & SETTERS
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}