package com.billora.billora_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.billora.billora_backend.entity.Cart;
import com.billora.billora_backend.repository.CartRepository;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    // 1️⃣ CREATE CART
    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        cart.setStatus("PENDING");
        return cartRepository.save(cart);
    }

    // 2️⃣ GET CART BY ID (for cashier)
    @GetMapping("/{id}")
    public Cart getCart(@PathVariable Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}