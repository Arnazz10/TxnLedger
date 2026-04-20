package com.arnav.txnledger.controller;

import com.arnav.txnledger.dto.TransferRequest;
import com.arnav.txnledger.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
