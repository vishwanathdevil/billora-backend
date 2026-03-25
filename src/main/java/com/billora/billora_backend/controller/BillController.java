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

    @PostMapping
    public Bill saveBill(@RequestBody Bill bill) {
        return billRepository.save(bill);
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @GetMapping("/{username}")
    public List<Bill> getBillsByUsername(@PathVariable String username) {
        return billRepository.findByUsername(username);
    }

    @GetMapping("/id/{id}")
public Bill getBillById(@PathVariable Long id) {
    return billRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Bill not found"));
}
}






// @RestController
// @RequestMapping("/api/bills")
// @CrossOrigin(origins = "*") // allow frontend access
// public class BillController {

//     @Autowired
//     private BillRepository billRepository;

//     // ✅ 1. SAVE BILL
//     @PostMapping
//     public Bill saveBill(@RequestBody Bill bill) {
//         return billRepository.save(bill);
//     }

//     // ✅ 2. GET ALL BILLS (NEW - IMPORTANT)
//     @GetMapping
//     public List<Bill> getAllBills() {
//         return billRepository.findAll();
//     }

//     // ✅ 3. GET BILLS BY USERNAME
//     @GetMapping("/{username}")
//     public List<Bill> getBillsByUsername(@PathVariable String username) {
//         return billRepository.findByUsername(username);
//     }
// }