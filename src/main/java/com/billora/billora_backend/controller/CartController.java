package com.billora.billora_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.billora.billora_backend.entity.Cart;
import com.billora.billora_backend.repository.CartRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    // 1️⃣ ADD PRODUCT TO CART
    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        cart.setStatus("PENDING");
        return cartRepository.save(cart);
    }

    // 2️⃣ GET CART BY SESSION (🔥 FIXED)
    @GetMapping("/{sessionId}")
    public List<Cart> getCartBySession(@PathVariable Long sessionId) {

        List<Cart> cartItems = cartRepository.findBySessionId(sessionId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("No cart items found");
        }

        return cartItems; // ✅ returns array
    }
}