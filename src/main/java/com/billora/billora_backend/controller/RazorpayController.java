package com.billora.billora_backend.controller;

import com.billora.billora_backend.entity.Bill;
import com.billora.billora_backend.entity.CartItem;
import com.billora.billora_backend.entity.Product;
import com.billora.billora_backend.repository.BillRepository;
import com.billora.billora_backend.repository.ProductRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class RazorpayController {

    private static final String KEY = "rzp_test_SYKrnMrPo4MNDv";
    private static final String SECRET = "m6CNw09oCNSsWeRvZXS8D8QR"; // 🔥 keep real secret

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ===============================
    // ✅ START PAYMENT
    // ===============================
    @PostMapping("/start/{billId}")
    public void startPayment(@PathVariable Long billId) {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        bill.setStatus("PAYMENT_PENDING");
        billRepository.save(bill);

        // 🔔 notify frontend
        messagingTemplate.convertAndSend("/topic/bills", bill);
    }

    // ===============================
    // ✅ CREATE ORDER
    // ===============================
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data)
            throws RazorpayException {

        int amount = Integer.parseInt(data.get("amount").toString());

        RazorpayClient client = new RazorpayClient(KEY, SECRET);

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100);
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = client.orders.create(options);

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));

        return response;
    }

    // ===============================
    // ✅ VERIFY PAYMENT (FINAL VERSION)
    // ===============================
    @PostMapping("/verify")
    public Map<String, Object> verifyPayment(@RequestBody Map<String, String> data) {

        Map<String, Object> response = new HashMap<>();

        try {

            String orderId = data.get("razorpay_order_id");
            String paymentId = data.get("razorpay_payment_id");
            String signature = data.get("razorpay_signature");
            Long billId = Long.parseLong(data.get("billId"));

            // 🔐 VERIFY SIGNATURE
            String generatedSignature = hmacSHA256(orderId + "|" + paymentId, SECRET);

            if (!generatedSignature.equals(signature)) {
                throw new RuntimeException("Payment verification failed ❌");
            }

            // 🔍 FETCH BILL
            Bill bill = billRepository.findById(billId)
                    .orElseThrow(() -> new RuntimeException("Bill not found"));

            // 🚫 PREVENT DOUBLE PAYMENT
            if ("PAID".equals(bill.getStatus())) {
                response.put("message", "Already paid");
                return response;
            }

            // 🔄 UPDATE BILL
            bill.setStatus("PAID");
            bill.setPaymentId(paymentId);
            bill.setPaymentMode("UPI");

            billRepository.save(bill);

            // ===============================
            // 🔥 STOCK REDUCTION
            // ===============================
            if (bill.getItems() != null) {

                for (CartItem item : bill.getItems()) {

                    Product product = productRepository
                            .findByNameAndStoreId(item.getName(), bill.getStoreId());

                    if (product != null) {

                        int stock = product.getStock();

                        if (stock >= item.getQuantity()) {
                            product.setStock(stock - item.getQuantity());
                            productRepository.save(product);
                        } else {
                            throw new RuntimeException(
                                    "Insufficient stock for " + item.getName());
                        }
                    }
                }
            }

            // 🔔 WEBSOCKET UPDATE
            messagingTemplate.convertAndSend("/topic/bills", bill);

            response.put("status", "success");
            return response;

        } catch (Exception e) {
            response.put("status", "failed");
            response.put("error", e.getMessage());
            return response;
        }
    }

    // ===============================
    // 🔐 SIGNATURE METHOD
    // ===============================
    private String hmacSHA256(String data, String key) throws Exception {

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");

        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes());

        StringBuilder hex = new StringBuilder();

        for (byte b : hash) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1) hex.append('0');
            hex.append(s);
        }

        return hex.toString();
    }

    // ===============================
    // ✅ TEST API
    // ===============================
    @GetMapping("/test")
    public String testApi() {
        return "WORKING";
    }
}