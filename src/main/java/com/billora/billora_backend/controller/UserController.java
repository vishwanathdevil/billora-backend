package com.billora.billora_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billora.billora_backend.entity.User;
import com.billora.billora_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🔥 Register User
    @PostMapping("/register")
public User register(@RequestBody User user) {

    if (user == null || user.getUsername() == null || user.getPassword() == null) {
        throw new RuntimeException("Invalid input");
    }

    // ✅ Use given role or set default role
    if(user.getRole() == null) {
        user.setRole("CUSTOMER");
    }

    return userRepository.save(user); // ✅ return full object
}

    // 🔥 Login User (FIXED)
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

    return existingUser; // ✅ JSON response
}
}