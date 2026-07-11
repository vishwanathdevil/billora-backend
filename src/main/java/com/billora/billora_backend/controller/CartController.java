package com.billora.billora_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.billora.billora_backend.entity.Cart;
import com.billora.billora_backend.repository.CartRepository;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartRepository cartRepo;

    // ===============================
    // ADD TO CART
    // ===============================
    @PostMapping
    public Cart addToCart(@RequestBody Cart cart) {

        return cartRepo.save(cart);
    }

    // ===============================
    // GET USER CART
    // ===============================
    @GetMapping("/user/{username}")
    public List<Cart> getUserCart(
            @PathVariable String username
    ) {

        return cartRepo.findByOwner(username);
    }

    // ===============================
    // UPDATE QUANTITY
    // ===============================
    @PutMapping("/update/{id}/{qty}")
    public Cart updateQuantity(
            @PathVariable Long id,
            @PathVariable int qty
    ) {

        Cart cart = cartRepo.findById(id).orElseThrow();

        cart.setQuantity(qty);

        return cartRepo.save(cart);
    }

    // ===============================
    // DELETE ITEM
    // ===============================
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {

        cartRepo.deleteById(id);
    }

    // ===============================
    // CLEAR USER CART
    // ===============================
    @DeleteMapping("/user/{username}")
    public void clearUserCart(
            @PathVariable String username
    ) {

        List<Cart> items =
                cartRepo.findByOwner(username);

        cartRepo.deleteAll(items);
    }
    
    // ===============================
    // GET SESSION CART
    // ===============================
    @GetMapping("/session/{sessionId}")
    public List<Cart> getSessionCart(
            @PathVariable Long sessionId
    ) {
        return cartRepo.findBySessionId(sessionId);
    }
    
    // ===============================
    // CLEAR SESSION CART
    // ===============================
    @DeleteMapping("/session/{sessionId}")
    public void clearSessionCart(
            @PathVariable Long sessionId
    ) {
        cartRepo.deleteBySessionId(sessionId);
    }
}