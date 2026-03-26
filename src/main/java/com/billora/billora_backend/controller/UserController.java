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
public String register(@RequestBody User user) {
    System.out.println("Register API called: " + user.getUsername());

    userRepository.save(user);

    return "User registered successfully";
}

    // 🔥 Login User
    @PostMapping("/login")
public String login(@RequestBody User user) {

    User existingUser = userRepository.findByUsername(user.getUsername());

    if (existingUser == null) {
        return "User not found";
    }

    if (!existingUser.getPassword().equals(user.getPassword())) {
        return "Invalid password";
    }

    return "Login successful";
}
}