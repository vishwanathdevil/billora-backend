package com.billora.billora_backend.controller;

import org.springframework.web.bind.annotation.*;
import com.razorpay.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class RazorpayController {

    private static final String KEY = "rzp_test_SYKrnMrPo4MNDv";
    private static final String SECRET = "m6CNw09oCNSsWeRvZXS8D8QR";

    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {

        int amount = (int) data.get("amount");

        RazorpayClient client = new RazorpayClient(KEY, SECRET);

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // in paise
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_11");

        Order order = client.orders.create(options);

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));

        return response;
    }
}