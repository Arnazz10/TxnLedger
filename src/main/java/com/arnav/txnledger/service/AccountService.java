package com.arnav.txnledger.service;

import com.arnav.txnledger.entity.Account;
import com.arnav.txnledger.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(String ownerName, BigDecimal initialBalance) {
        String accountNumber = "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .ownerName(ownerName)
                .balance(initialBalance)
                .build();
        return accountRepository.save(account);
    }

    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
