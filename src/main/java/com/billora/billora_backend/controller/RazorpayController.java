package com.billora.billora_backend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billora.billora_backend.entity.Bill;
import com.billora.billora_backend.repository.BillRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")

public class RazorpayController {

    private static final String KEY = "rzp_test_SYKrnMrPo4MNDv";
    private static final String SECRET = "m6CNw09oCNSsWeRvZXS8D8QR";

    @Autowired
private BillRepository billRepository;

@Autowired
private SimpMessagingTemplate messagingTemplate;

    // ✅ CREATE ORDER
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {

        int amount = Integer.parseInt(data.get("amount").toString());

        RazorpayClient client = new RazorpayClient(KEY, SECRET);

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // in paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = client.orders.create(options);

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));

        return response;
    }

    // ✅ VERIFY PAYMENT (VERY IMPORTANT)
    @PostMapping("/verify")
public Map<String, Object> verifyPayment(@RequestBody Map<String, String> data) {

    String orderId = data.get("razorpay_order_id");
    String paymentId = data.get("razorpay_payment_id");
    String signature = data.get("razorpay_signature");
    String billIdStr = data.get("billId");

    Map<String, Object> response = new HashMap<>();

    try {
        String generatedSignature = hmacSHA256(orderId + "|" + paymentId, SECRET);

        if (generatedSignature.equals(signature)) {

            Long billId = Long.parseLong(billIdStr);

            Bill bill = billRepository.findById(billId)
                    .orElseThrow(() -> new RuntimeException("Bill not found"));

            bill.setStatus("PAID");
            bill.setPaymentId(paymentId);
            bill.setPaymentMode("UPI");

            billRepository.save(bill);

            // 🔥 REALTIME UPDATE
            messagingTemplate.convertAndSend("/topic/bills", bill);

            response.put("status", "success");

        } else {
            response.put("status", "failed");
        }

    } catch (Exception e) {
        response.put("status", "error");
    }

    return response;
}

    // ✅ HMAC SHA256 SIGNATURE GENERATOR
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
}