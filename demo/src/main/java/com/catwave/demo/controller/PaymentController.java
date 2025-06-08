package com.catwave.demo.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.catwave.demo.model.Member;
import com.catwave.demo.model.TransactionDto;
import com.catwave.demo.model.VietQrOrderResponse;
import com.catwave.demo.repository.MemRepo;
import com.catwave.demo.service.JwtService;
import com.catwave.demo.service.VietQrOrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PaymentController {
    @Autowired
    private MemRepo memRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired 
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VietQrOrderService vietQrOrderService;

    @PostMapping("/api/payment/transactions/sync")
    public ResponseEntity<?> syncTransactions(
        @RequestBody List<TransactionDto> transactions
    ) {
        // now `transactions` is a List<TransactionDto>
        int count = transactions.size();
        // do your processing...
        return ResponseEntity.ok(Map.of(
          "received", count,
          "message",  "Synced " + count + " transactions successfully"
        ));
    }

    @GetMapping("api/payment/connection/info")
    public Map<String,String> getConnectionInfo() {
        String username = "customer-catwave-user25309";
        String password = Base64.getEncoder().encodeToString(username.getBytes());
        // String password = "Y3VzdG9tZXItY2F0d2FZS11c2VyMjUzMDk=";
        return Map.of(
          "username", username,
          "password", password
        );
    }

    @PostMapping("/api/payment/create_qr")
    public ResponseEntity<VietQrOrderResponse> createQr(@RequestBody TransactionDto dto) throws Exception {
        VietQrOrderResponse qr = vietQrOrderService.generateQr(dto);
        return ResponseEntity.ok(qr);
    }

}
