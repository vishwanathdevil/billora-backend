package com.billora.billora_backend.controller;

import com.billora.billora_backend.entity.Product;
import com.billora.billora_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")  // 🔥 CORS FIX
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 🔥 Add product (for testing)
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // 🔥 Get product by barcode
    @GetMapping("/{code}")
public ResponseEntity<?> getProduct(@PathVariable String code) {

    String normalizedCode = code.replaceFirst("^0+", "");

    Product product = productRepository.findByCode(normalizedCode);

    if (product == null) {
        return ResponseEntity.status(404).body("Product not found");
    }

    return ResponseEntity.ok(product);
}
}