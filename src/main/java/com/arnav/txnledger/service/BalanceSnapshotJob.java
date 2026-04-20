package com.arnav.txnledger.service;

import com.arnav.txnledger.entity.Account;
import com.arnav.txnledger.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceSnapshotJob {

    private final AccountRepository accountRepository;

    // Scheduled for every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void takeDailySnapshot() {
        log.info("Starting end-of-day balance snapshot...");
        List<Account> accounts = accountRepository.findAll();
        
        for (Account account : accounts) {
            log.info("Account: {}, Balance: {}", account.getAccountNumber(), account.getBalance());
            // In a real system, we would save this to a snapshot table
        }
        
        log.info("Snapshot complete for {} accounts.", accounts.size());
    }
}
