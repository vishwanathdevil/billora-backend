package com.billora.billora_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billora.billora_backend.entity.Bill;
import com.billora.billora_backend.repository.BillRepository;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    // ✅ 1. SAVE BILL (Customer Checkout)
    @PostMapping
    public Bill saveBill(@RequestBody Bill bill) {
        bill.setStatus("PENDING"); // 🔥 important for cashier
        return billRepository.save(bill);
    }

    // ✅ 2. GET ALL BILLS (Cashier Dashboard)
    @GetMapping
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    // ✅ 3. GET BILLS BY USERNAME (Customer History)
    @GetMapping("/{username}")
    public List<Bill> getBillsByUsername(@PathVariable String username) {
        return billRepository.findByUsername(username);
    }

    // ✅ 4. GET BILL BY ID (QR / Payment Page)
    @GetMapping("/id/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    // ✅ 5. MARK BILL AS PAID (Cashier Action)
    @PutMapping("/{id}/pay/{mode}")
public Bill markAsPaid(@PathVariable Long id, @PathVariable String mode) {

    Bill bill = billRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Bill not found"));

    bill.setStatus("PAID");
    bill.setPaymentMode(mode);

    return billRepository.save(bill);
}
}