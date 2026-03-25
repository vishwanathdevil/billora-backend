package com.billora.billora_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.billora.billora_backend.entity.Cart;
public interface CartRepository extends JpaRepository<Cart, Long> {
    // No custom methods needed for basic CRUD
}