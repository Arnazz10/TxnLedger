package com.arnav.txnledger.service;

import com.arnav.txnledger.entity.Account;
import com.arnav.txnledger.entity.Transaction;
import com.arnav.txnledger.entity.TransactionType;
import com.arnav.txnledger.repository.AccountRepository;
import com.arnav.txnledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final Map<String, Lock> accountLocks = new ConcurrentHashMap<>();

    private Lock getLockForAccount(String accountNumber) {
        return accountLocks.computeIfAbsent(accountNumber, k -> new ReentrantLock());
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        // To prevent deadlocks, always lock the accounts in a consistent order (e.g., lexicographical)
        String firstAccount = fromAccountNumber.compareTo(toAccountNumber) < 0 ? fromAccountNumber : toAccountNumber;
        String secondAccount = fromAccountNumber.compareTo(toAccountNumber) < 0 ? toAccountNumber : fromAccountNumber;

        Lock lock1 = getLockForAccount(firstAccount);
        Lock lock2 = getLockForAccount(secondAccount);

        lock1.lock();
        try {
            lock2.lock();
            try {
                log.info("Executing transfer of {} from {} to {}", amount, fromAccountNumber, toAccountNumber);
                
                Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                        .orElseThrow(() -> new RuntimeException("Source account not found"));
                Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                        .orElseThrow(() -> new RuntimeException("Destination account not found"));

                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new RuntimeException("Insufficient balance");
                }

                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));

                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);

                Transaction transaction = Transaction.builder()
                        .sourceAccount(fromAccount)
                        .destinationAccount(toAccount)
                        .amount(amount)
                        .type(TransactionType.TRANSFER)
                        .description("Transfer from " + fromAccountNumber + " to " + toAccountNumber)
                        .build();

                transactionRepository.save(transaction);
                
                log.info("Transfer successful");
            } finally {
                lock2.unlock();
            }
        } finally {
            lock1.unlock();
        }
    }

    @Transactional(readOnly = true)
    public java.util.List<Transaction> getStatement(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findBySourceAccountOrDestinationAccountOrderByTimestampDesc(account, account);
    }
}
