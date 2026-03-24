package com.billora.billora_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billora.billora_backend.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByUsername(String username);
}