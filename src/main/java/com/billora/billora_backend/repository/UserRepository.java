package com.billora.billora_backend.repository;

import com.billora.billora_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔥 Find user by username & password (for login)
    User findByUsername(String username);
}