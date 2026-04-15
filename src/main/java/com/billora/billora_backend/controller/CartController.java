package com.billora.billora_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billora.billora_backend.entity.Cart;
import com.billora.billora_backend.repository.CartRepository;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    // ===============================
    // ADD ITEM (FIXED ✅)
    // ===============================
    @PostMapping
    public Cart add(@RequestBody Cart cart) {

        if (cart.getRole() == null) {
            cart.setRole("CHILD"); // default
        }

        cart.setStatus("PENDING");
        cart.setCompleted(false);

        return cartRepository.save(cart);
    }

    // ===============================
    // GET MAIN CART
    // ===============================
    @GetMapping("/main/{sessionId}")
    public List<Cart> mainCart(@PathVariable Long sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .stream()
                .filter(c -> c.isCompleted())
                .toList();
    }

    // ===============================
    // GET CHILD CART
    // ===============================
    @GetMapping("/child/{sessionId}/{owner}")
    public List<Cart> childCart(@PathVariable Long sessionId, @PathVariable String owner) {

        if (owner == null || owner.equals("null")) {
            return List.of();
        }

        return cartRepository.findBySessionIdAndOwner(sessionId, owner)
                .stream()
                .filter(c -> !c.isCompleted())
                .toList();
    }

    // ===============================
    // COMPLETE CHILD CART
    // ===============================
    @PutMapping("/complete/{sessionId}/{owner}")
    public String complete(@PathVariable Long sessionId, @PathVariable String owner) {

        List<Cart> items = cartRepository.findBySessionIdAndOwner(sessionId, owner);

        for (Cart c : items) {
            c.setCompleted(true);
            cartRepository.save(c);
        }

        return "Moved to main cart";
    }

    // ===============================
    // UPDATE QTY
    // ===============================
    @PutMapping("/update/{id}/{qty}")
    public Cart update(@PathVariable Long id, @PathVariable int qty) {
        Cart c = cartRepository.findById(id).get();
        c.setQuantity(qty);
        return cartRepository.save(c);
    }

    // ===============================
    // DELETE ITEM
    // ===============================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        cartRepository.deleteById(id);
        return "Deleted";
    }

    // ===============================
    // CLEAR CART
    // ===============================
    @DeleteMapping("/session/{sessionId}")
    public String clear(@PathVariable Long sessionId) {
        cartRepository.deleteAll(cartRepository.findBySessionId(sessionId));
        return "Cleared";
    }
}