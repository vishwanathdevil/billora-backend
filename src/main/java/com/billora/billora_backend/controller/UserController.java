package com.billora.billora_backend.controller;

import com.billora.billora_backend.entity.User;
import com.billora.billora_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🔥 Register User
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return "Invalid input";
        }

        userRepository.save(user);
        return "User registered successfully";
    }

    // 🔥 Login User (FIXED)
    @PostMapping("/login")
public String login(@RequestBody User user) {

    try {

        System.out.println("LOGIN HIT: " + user);

        if (user == null) {
            return "Invalid input";
        }

        String username = user.getUsername();
        String password = user.getPassword();

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        if (username == null || password == null) {
            return "Invalid input";
        }

        // 🔥 FIX: Use safe query
        User existingUser = userRepository.findAll()
                .stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst()
                .orElse(null);

        System.out.println("DB User: " + existingUser);

        if (existingUser == null) {
            return "User not found";
        }

        if (!password.equals(existingUser.getPassword())) {
            return "Invalid password";
        }

        return "Login successful";

    } catch (Exception e) {
        e.printStackTrace();
        return "Server error: " + e.getMessage(); // 🔥 shows real error
    }
}
}