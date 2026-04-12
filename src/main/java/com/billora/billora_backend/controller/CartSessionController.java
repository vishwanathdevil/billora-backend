package com.billora.billora_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billora.billora_backend.entity.CartSession;
import com.billora.billora_backend.repository.CartSessionRepository;

@RestController
@RequestMapping("/api/session")
@CrossOrigin("*")
public class CartSessionController {

    @Autowired
    private CartSessionRepository repo;

    // 🟢 CREATE SESSION
    @PostMapping("/create")
    public CartSession create(@RequestBody CartSession session) {
        return repo.save(session);
    }

    // 🟡 JOIN SESSION
    @GetMapping("/{id}")
    public CartSession join(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
    @PostMapping("/start")
public CartSession startSession(@RequestBody CartSession req) {

    CartSession session = repo.findById(req.getId()).orElseThrow();

    session.setStatus("ACTIVE");       // ✅ IMPORTANT
    session.setStoreId(req.getStoreId()); // ✅ IMPORTANT

    return repo.save(session);
}
}
