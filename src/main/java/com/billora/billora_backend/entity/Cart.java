package com.billora.billora_backend.entity;

import jakarta.persistence.*;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private Long sessionId;

    private String name;
    private String code;
    private double price;
    private int quantity;
    private String owner;

    private Double total;
    private String status;

    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}