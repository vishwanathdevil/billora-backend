package com.billora.billora_backend.controller;

import com.billora.billora_backend.entity.User;
import com.billora.billora_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🔥 Register User
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 🔥 Login User
    @PostMapping("/login")
public User login(@RequestBody User user) {
    User existingUser = userRepository
        .findByUsernameAndPassword(user.getUsername(), user.getPassword());

    if (existingUser != null) {
        return existingUser;
    } else {
        throw new RuntimeException("Invalid credentials");
    }
}
}