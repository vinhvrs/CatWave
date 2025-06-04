package com.catwave.demo.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import com.catwave.demo.model.Member;
import com.catwave.demo.model.TransactionDto;
import com.catwave.demo.repository.MemRepo;
import com.catwave.demo.service.JwtService;

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

    @PostMapping("/api/payment/token_generate")
    public ResponseEntity<?> tokenGenerate(@RequestHeader("Authorization") String authHeader, @RequestParam(value="grant_type", defaultValue="client_credentials") String grantType) {
        if (!"client_credentials".equals(grantType)) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "unsupported_grant_type"));
        }

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "invalid_client"));
        }

        // Decode Basic credentials
        String base64Creds = authHeader.substring(6);
        String decoded    = new String(Base64.getDecoder().decode(base64Creds), StandardCharsets.UTF_8);
        String[] parts    = decoded.split(":", 2);
        if (parts.length != 2) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_client"));
        }
        String clientId = parts[0], clientSecret = parts[1];

        // Lookup your member record by username
        Member m = memRepo.findByUsername(clientId);
        if (m == null || !passwordEncoder.matches(clientSecret, m.getPassword())) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "invalid_client"));
        }

        // Issue a JWT
        String jwt = jwtService.generateToken(clientId);
        return ResponseEntity.ok(Map.of(
          "access_token", jwt,
          "token_type",   "bearer",
          "expires_in",   String.valueOf(60 * 60)
        ));
    }

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
        return Map.of(
          "username", username,
          "password", password
        );
    }

}
