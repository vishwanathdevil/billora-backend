package com.billora.billora_backend.repository;

import com.billora.billora_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.code LIKE %:code%")
    Product findByCode(@Param("code") String code);
}