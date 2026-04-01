package com.billora.billora_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.billora.billora_backend.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}