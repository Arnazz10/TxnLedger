package com.arnav.txnledger.controller;

import com.arnav.txnledger.dto.TransferRequest;
import com.arnav.txnledger.entity.Transaction;
import com.arnav.txnledger.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccountNumber(), request.getToAccountNumber(), request.getAmount());
        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/statement/{accountNumber}")
    public ResponseEntity<List<Transaction>> getStatement(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getStatement(accountNumber));
    }
}
