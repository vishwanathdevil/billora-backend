package com.billora.billora_backend.repository;

import com.billora.billora_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStoreId(Long storeId);
    Product findByCode(String code);
    Product findByCodeAndStoreId(String code, Long storeId);
}