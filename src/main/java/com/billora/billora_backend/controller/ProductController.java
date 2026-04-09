package com.billora.billora_backend.controller;

import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.billora.billora_backend.entity.Product;
import com.billora.billora_backend.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // ===============================
    // ADD PRODUCT
    // ===============================
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {

        if (product.getStock() == 0) {
            product.setStock(100);
        }

        return productRepository.save(product);
    }

    // ===============================
    // GET PRODUCTS
    // ===============================
    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) Long storeId) {
        return (storeId != null)
                ? productRepository.findByStoreId(storeId)
                : productRepository.findAll();
    }

    // ===============================
    // SCAN PRODUCT (AUTO FETCH)
    // ===============================
    @GetMapping("/{code}")
    public ResponseEntity<?> getProduct(
            @PathVariable String code,
            @RequestParam Long storeId) {

        try {

            String normalizedCode = code.replaceFirst("^0+", "");

            // 1️⃣ LOCAL DB
            Product product = productRepository.findByCodeAndStoreId(code, storeId);

            if (product == null) {
                product = productRepository.findByCodeAndStoreId(normalizedCode, storeId);
            }

            // 2️⃣ EXTERNAL API
            if (product == null) {
                product = fetchFromOpenFoodFacts(code, storeId);
            }

            // 3️⃣ NOT FOUND
            if (product == null) {
                return ResponseEntity.status(404).body("Product not found");
            }

            // 4️⃣ STOCK CHECK
            if (product.getStock() <= 0) {
                return ResponseEntity.status(400).body("Out of stock ❌");
            }

            return ResponseEntity.ok(product);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error");
        }
    }

    // ===============================
    // 🔥 SINGLE CLEAN API METHOD
    // ===============================
    private Product fetchFromOpenFoodFacts(String code, Long storeId) {

        try {
            String url = "https://world.openfoodfacts.org/api/v0/product/" + code + ".json";

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !Integer.valueOf(1).equals(response.get("status"))) {
                return null;
            }

            Map<String, Object> productData =
                    (Map<String, Object>) response.get("product");

            if (productData == null) return null;

            String name = (String) productData.get("product_name");

            Product p = new Product();
            p.setCode(code);
            p.setName(name != null && !name.isEmpty() ? name : "Unknown Product");
            p.setPrice(10);   // default price
            p.setStoreId(storeId);
            p.setStock(100);  // default stock

            return productRepository.save(p);

        } catch (Exception e) {
            return null;
        }
    }

    // ===============================
    // UPDATE PRODUCT
    // ===============================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updated) {

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        p.setName(updated.getName());
        p.setPrice(updated.getPrice());
        p.setCode(updated.getCode());
        p.setStock(updated.getStock());

        return ResponseEntity.ok(productRepository.save(p));
    }

    // ===============================
    // DELETE
    // ===============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}