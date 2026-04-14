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

    // ADD TO CART
    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        cart.setStatus("PENDING");
        return cartRepository.save(cart);
    }

    // GET CART
    @GetMapping("/{sessionId}")
    public List<Cart> getCartBySession(@PathVariable Long sessionId) {
        return cartRepository.findBySessionId(sessionId);
    }

    // ✅ CLEAR CART (FIXED)
    @DeleteMapping("/{sessionId}")
    public String clearCart(@PathVariable Long sessionId) {

        List<Cart> cartItems = cartRepository.findBySessionId(sessionId);

        if (cartItems.isEmpty()) {
            return "Cart already empty";
        }

        cartRepository.deleteAll(cartItems);

        return "Cart cleared successfully";
    }
}