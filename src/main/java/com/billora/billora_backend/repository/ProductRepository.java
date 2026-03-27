package com.billora.billora_backend.repository;

import com.billora.billora_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    
    Product findByCode(String code);
}