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
    private CartRepository cartRepository;

    // ===============================
    // ADD ITEM
    // ===============================
    @PostMapping
    public Cart add(@RequestBody Cart cart) {

        if (cart.getQuantity() <= 0) {
            cart.setQuantity(1);
        }

        return cartRepository.save(cart);
    }

    // ===============================
    // GET USER CART
    // ===============================
    @GetMapping("/user/{owner}")
    public List<Cart> getUserCart(@PathVariable String owner) {

        return cartRepository.findByOwner(owner);
    }

    // ===============================
    // UPDATE QUANTITY
    // ===============================
    @PutMapping("/update/{id}/{qty}")
    public Cart update(@PathVariable Long id,
                       @PathVariable int qty) {

        Cart cart = cartRepository.findById(id).orElseThrow();

        cart.setQuantity(qty);

        return cartRepository.save(cart);
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
    // CLEAR USER CART
    // ===============================
    @DeleteMapping("/user/{owner}")
    public String clearUserCart(@PathVariable String owner) {

        cartRepository.deleteAll(
                cartRepository.findByOwner(owner)
        );

        return "Cart cleared";
    }
}