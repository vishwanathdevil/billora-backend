package com.billora.billora_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.billora.billora_backend.entity.Product;
import com.billora.billora_backend.repository.ProductRepository;

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
        return (storeId != null)
                ? productRepository.findByStoreId(storeId)
                : productRepository.findAll();
    }

    // ✅ Get product by barcode + storeId
    @GetMapping("/{code}")
    public ResponseEntity<?> getProduct(
            @PathVariable String code,
            @RequestParam Long storeId) {

        try {
            String normalizedCode = code.replaceFirst("^0+", "");

            // 1️⃣ Try DB (original code)
            Product product = productRepository.findByCodeAndStoreId(code, storeId);

            // 2️⃣ Try normalized code
            if (product == null) {
                product = productRepository.findByCodeAndStoreId(normalizedCode, storeId);
            }

            // 3️⃣ Try external API
            if (product == null) {
                product = fetchFromOpenFoodFacts(code, storeId);
            }

            if (product == null) {
                return ResponseEntity.status(404).body("Product not found");
            }

            return ResponseEntity.ok(product);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error");
        }
    }

    // ✅ Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // ✅ Update product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updated) {

        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        p.setName(updated.getName());
        p.setPrice(updated.getPrice());
        p.setCode(updated.getCode());

        return ResponseEntity.ok(productRepository.save(p));
    }

    // ✅ FETCH FROM OPEN FOOD FACTS
    public Product fetchFromOpenFoodFacts(String code, Long storeId) {
        try {
            String url = "https://world.openfoodfacts.org/api/v0/product/" + code + ".json";

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !Integer.valueOf(1).equals(response.get("status"))) {
                return null;
            }

            Map<String, Object> productData = (Map<String, Object>) response.get("product");

            if (productData == null) return null;

            String name = (String) productData.get("product_name");

            Product p = new Product();
            p.setCode(code);
            p.setName(name != null && !name.isEmpty() ? name : "Unknown Product");
            p.setPrice(50.0); // 🔥 TODO: dynamic pricing later
            p.setStoreId(storeId);

            return productRepository.save(p);

        } catch (Exception e) {
            return null;
        }
    }
}