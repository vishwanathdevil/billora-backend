package com.billora.billora_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// import com.billora.billora_backend.config.JwtUtil;
import com.billora.billora_backend.dto.AuthResponse;
import com.billora.billora_backend.entity.User;
import com.billora.billora_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🔐 🔥 CHANGE THIS SECRET (VERY IMPORTANT)
    private static final String ADMIN_SECRET = "vishwa_super_secret_key_98765";

    // ===============================
    // 🔥 REGISTER (CUSTOMER ONLY)
    // ===============================
    @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody User user) {

    if (user == null ||
        user.getUsername() == null ||
        user.getPassword() == null ||
        user.getUsername().trim().isEmpty() ||
        user.getPassword().trim().isEmpty()) {

        return ResponseEntity.badRequest().body("Invalid input");
    }

    // ✅ CHECK DUPLICATE USERNAME
    User existing = userRepository.findByUsername(user.getUsername());

    if (existing != null) {
        return ResponseEntity
                .badRequest()
                .body("Username already exists ❌");
    }

    // ✅ FORCE CUSTOMER ROLE
    user.setRole("CUSTOMER");

    User saved = userRepository.save(user);

    return ResponseEntity.ok(saved);
}

    // ===============================
    // 🔥 LOGIN
    // ===============================
    @Autowired
// private JwtUtil jwtUtil;

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

    return ResponseEntity.ok(
        new AuthResponse("billora-token", existingUser)
    );
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