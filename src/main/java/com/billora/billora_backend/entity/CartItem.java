package com.billora.billora_backend.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class CartItem {

    private String name;
    private int quantity;
    private double price;

    // GETTERS & SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}