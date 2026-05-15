package com.billora.billora_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billora.billora_backend.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findBySessionId(Long sessionId);

    List<Cart> findBySessionIdAndOwner(Long sessionId, String owner);

    // ✅ ADD THIS
    List<Cart> findByOwner(String owner);
}