package com.billora.billora_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public User login(@RequestBody User user) {

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new RuntimeException("Invalid input");
        }

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return existingUser;
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