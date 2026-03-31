package com.billora.billora_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billora.billora_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔥 Find user by username & password (for login)
    User findByUsername(String username);
}