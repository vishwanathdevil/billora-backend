package com.billora.billora_backend.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private double total;

    @ElementCollection
    private List<String> items; // simple product names

    // getters & setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}