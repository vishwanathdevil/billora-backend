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

    // ADD
    @PostMapping
    public Cart add(@RequestBody Cart cart) {
        cart.setStatus("PENDING");
        cart.setCompleted(false);
        return cartRepository.save(cart);
    }

    // GET MAIN CART (ONLY COMPLETED + MAIN)
    @GetMapping("/main/{sessionId}")
    public List<Cart> mainCart(@PathVariable Long sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .stream()
                .filter(c -> c.isCompleted() || "MAIN".equals(c.getRole()))
                .toList();
    }

    // GET CHILD CART (OWN ONLY)
    @GetMapping("/child/{sessionId}/{owner}")
    public List<Cart> childCart(@PathVariable Long sessionId, @PathVariable String owner) {
        return cartRepository.findBySessionIdAndOwner(sessionId, owner)
                .stream()
                .filter(c -> !c.isCompleted())
                .toList();
    }

    // COMPLETE CHILD CART
    @PutMapping("/complete/{sessionId}/{owner}")
    public String complete(@PathVariable Long sessionId, @PathVariable String owner) {

        List<Cart> items = cartRepository.findBySessionIdAndOwner(sessionId, owner);

        for (Cart c : items) {
            c.setCompleted(true);
            cartRepository.save(c);
        }

        return "Moved to main cart";
    }

    // UPDATE QTY
    @PutMapping("/update/{id}/{qty}")
    public Cart update(@PathVariable Long id, @PathVariable int qty) {
        Cart c = cartRepository.findById(id).get();
        c.setQuantity(qty);
        return cartRepository.save(c);
    }

    // DELETE ITEM
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        cartRepository.deleteById(id);
        return "Deleted";
    }

    // CLEAR AFTER PAYMENT
    @DeleteMapping("/session/{sessionId}")
    public String clear(@PathVariable Long sessionId) {
        cartRepository.deleteAll(cartRepository.findBySessionId(sessionId));
        return "Cleared";
    }
}