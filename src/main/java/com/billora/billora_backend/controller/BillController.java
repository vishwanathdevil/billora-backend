package com.billora.billora_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billora.billora_backend.entity.Bill;
import com.billora.billora_backend.repository.BillRepository;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin
public class BillController {

    @Autowired
    private BillRepository billRepository;

    // SAVE BILL
    @PostMapping
    public Bill saveBill(@RequestBody Bill bill) {
        return billRepository.save(bill);
    }

    // GET USER BILLS
    @GetMapping("/{username}")
    public List<Bill> getBills(@PathVariable String username) {
        return billRepository.findByUsername(username);
    }
}