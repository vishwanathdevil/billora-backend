package com.billora.billora_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billora.billora_backend.entity.Cart;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    List<Cart> findByOwner(String owner);
    
    List<Cart> findBySessionId(Long sessionId);
    
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Modifying
    void deleteBySessionId(Long sessionId);
}