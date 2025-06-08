package com.catwave.demo.controller;

import com.catwave.demo.model.TransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {

    @PostMapping("/api/transactions/sync")
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
}
