package com.billora.billora_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ✅ SAVE BILL
    @PostMapping
    public Bill saveBill(@RequestBody Bill bill) {
        bill.setStatus("PENDING");
        return billRepository.save(bill);
    }

    // ✅ START PAYMENT
    @PutMapping("/{id}/start-payment")
    public Bill startPayment(@PathVariable Long id) {

        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        bill.setStatus("WAITING");

        Bill updated = billRepository.save(bill);

        messagingTemplate.convertAndSend("/topic/bills", updated);

        return updated;
    }

    @PutMapping("/{id}/pay/{mode}")
public Bill payBill(@PathVariable Long id, @PathVariable String mode) {

    Bill bill = billRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Bill not found"));

    bill.setStatus("PAID");
    bill.setPaymentMode(mode);

    Bill updated = billRepository.save(bill);

    messagingTemplate.convertAndSend("/topic/bills", updated);

    return updated;
}

    // ✅ GET BILL BY ID
    @GetMapping("/id/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    // ✅ GET BILLS BY USERNAME (🔥 FIX)
    @GetMapping("/user/{username}")
    public List<Bill> getBillsByUsername(@PathVariable String username) {
        return billRepository.findByUsername(username);
    }
}