package com.billora.billora_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.billora.billora_backend.entity.Store;
import com.billora.billora_backend.repository.StoreRepository;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    // ✅ GET ALL STORES
    @GetMapping
    public List<Store> getStores() {
        return storeRepository.findAll();
    }

    // ✅ ADD STORE (optional)
    @PostMapping
    public Store addStore(@RequestBody Store store) {
        return storeRepository.save(store);
    }
}