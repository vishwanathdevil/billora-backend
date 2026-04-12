package com.billora.billora_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billora.billora_backend.entity.CartSession;

public interface CartSessionRepository extends JpaRepository<CartSession, Long> {
}