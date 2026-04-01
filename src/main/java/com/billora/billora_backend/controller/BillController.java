package com.billora.billora_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.billora.billora_backend.entity.Bill;
import com.billora.billora_backend.repository.BillRepository;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    // ✅ SAVE BILL
    @PostMapping
    public Bill saveBill(@RequestBody Bill bill) {
        bill.setStatus("PENDING");
        return billRepository.save(bill);
    }

    // ✅ GET ALL BILLS
    @GetMapping
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    // ✅ GET BILLS BY USERNAME
    @GetMapping("/user/{username}")
    public List<Bill> getBillsByUsername(@PathVariable String username) {
        return billRepository.findByUsername(username);
    }

    // ✅ GET BILL BY ID
    @GetMapping("/id/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    // ✅ MARK BILL AS PAID (UPI / CASH)
    @PutMapping("/{id}/pay/{mode}")
    public Bill markAsPaid(@PathVariable Long id, @PathVariable String mode) {

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        bill.setStatus("PAID");
        bill.setPaymentMode(mode);

        return billRepository.save(bill);
    }
}