package com.billora.billora_backend.controller;

import com.billora.billora_backend.entity.Product;
import com.billora.billora_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // ✅ Add product
    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // ✅ Get all products (with store filter)
    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) Long storeId) {
        if (storeId != null) {
            return productRepository.findByStoreId(storeId);
        } else {
            return productRepository.findAll();
        }
    }

    // ✅ Get product by barcode + storeId
    @GetMapping("/{code}")
    public ResponseEntity<?> getProduct(
            @PathVariable String code,
            @RequestParam Long storeId) {

        String normalizedCode = code.replaceFirst("^0+", "");

        // 1️⃣ Try DB
        Product product = productRepository.findByCodeAndStoreId(code, storeId);

        if (product == null) {
            product = productRepository.findByCodeAndStoreId(normalizedCode, storeId);
        }

        // 2️⃣ If not found → call external API
        if (product == null) {
            product = fetchFromOpenFoodFacts(code, storeId);
        }

        if (product == null) {
            return ResponseEntity.status(404).body("Product not found");
        }

        return ResponseEntity.ok(product);
    }

    // ✅ Delete product
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    // ✅ Update product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updated) {

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        p.setName(updated.getName());
        p.setPrice(updated.getPrice());
        p.setCode(updated.getCode());

        return productRepository.save(p);
    }

    // ✅ 🔥 FETCH FROM OPEN FOOD FACTS (NEW METHOD)
    public Product fetchFromOpenFoodFacts(String code, Long storeId) {
        try {
            String url = "https://world.openfoodfacts.org/api/v0/product/" + code + ".json";

            RestTemplate restTemplate = new RestTemplate();
            Map response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.get("status").equals(1)) {
                return null;
            }

            Map productData = (Map) response.get("product");

            String name = (String) productData.get("product_name");

            Product p = new Product();
            p.setCode(code);
            p.setName(name != null ? name : "Unknown Product");
            p.setPrice(50.0); // temporary price
            p.setStoreId(storeId);

            return productRepository.save(p);

        } catch (Exception e) {
            return null;
        }
    }
}