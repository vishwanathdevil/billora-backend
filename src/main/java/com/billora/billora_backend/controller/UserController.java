package com.billora.billora_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billora.billora_backend.entity.User;
import com.billora.billora_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🔐 🔥 CHANGE THIS SECRET (VERY IMPORTANT)
    private static final String ADMIN_SECRET = "vishwa_super_secret_key_98765";

    // ===============================
    // 🔥 REGISTER (CUSTOMER ONLY)
    // ===============================
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            throw new RuntimeException("Invalid input");
        }

        // 🚫 FORCE CUSTOMER ROLE ONLY
        user.setRole("CUSTOMER");

        return userRepository.save(user);
    }

    // ===============================
    // 🔥 LOGIN
    // ===============================
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user) {

    if (user.getUsername() == null || user.getPassword() == null) {
        return ResponseEntity.badRequest().body("Invalid input");
    }

    User existingUser = userRepository.findByUsername(user.getUsername());

    if (existingUser == null) {
        return ResponseEntity.status(401).body("User not found");
    }

    if (!existingUser.getPassword().equals(user.getPassword())) {
        return ResponseEntity.status(401).body("Invalid password");
    }

    return ResponseEntity.ok(existingUser);
}

    // ===============================
    // 🔐 🔥 CREATE ADMIN / CASHIER (PROTECTED)
    // ===============================
    @PostMapping("/create")
    public User createUser(@RequestBody User user,
                           @RequestParam String secretKey) {

        // 🔒 CHECK SECRET KEY
        if (!ADMIN_SECRET.equals(secretKey)) {
            throw new RuntimeException("Unauthorized ❌");
        }

        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            throw new RuntimeException("Invalid input");
        }

        // 🚫 PREVENT RANDOM ROLE INJECTION
        if (user.getRole() == null ||
            !(user.getRole().equals("ADMIN") || user.getRole().equals("CASHIER"))) {
            throw new RuntimeException("Invalid role ❌");
        }

        return userRepository.save(user);
    }
}