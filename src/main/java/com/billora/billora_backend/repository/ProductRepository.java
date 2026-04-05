package com.billora.billora_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billora.billora_backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStoreId(Long storeId);
    Product findByCode(String code);
    Product findByCodeAndStoreId(String code, Long storeId);
    Product findByNameAndStoreId(String name, Long storeId);
}