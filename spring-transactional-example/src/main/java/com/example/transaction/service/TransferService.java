package com.example.transaction.service;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.domain.Account;
import com.example.transaction.repository.AccountRepository;

@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    private final AccountRepository accountRepository;
    private final AuditService auditService;
    private final BonusService bonusService;

    public TransferService(AccountRepository accountRepository,
                           AuditService auditService,
                           BonusService bonusService) {
        this.accountRepository = accountRepository;
        this.auditService = auditService;
        this.bonusService = bonusService;
    }

    @Transactional
    public void transferRequired(String from, String to, double amount) {
        withdraw(from, amount);
        deposit(to, amount);
    }

    @Transactional
    public void transferWithAudit(String from, String to, double amount) {
        withdraw(from, amount);
        deposit(to, amount);
        auditService.writeAuditEntry(from, to, amount);
    }

    @Transactional(timeout = 1, rollbackFor = TimeoutException.class)
    public void transferWithTimeout(String from, String to, double amount) throws TimeoutException {
        withdraw(from, amount);
        simulateLongProcessing();
        deposit(to, amount);
    }

    @Transactional
    public void transferWithRequiresNewBonus(String from, String to, double amount) {
        withdraw(from, amount);
        deposit(to, amount);
        bonusService.applyBonusWithRequiresNew(to);
    }

    private void withdraw(String owner, double amount) {
        Account account = accountRepository.findByOwner(owner)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + owner));
        if (account.getBalance() < amount) {
            throw new IllegalStateException("Not enough funds for " + owner);
        }
        account.setBalance(account.getBalance() - amount);
        log.info("Withdrew {} from {} (balance: {})", amount, owner, account.getBalance());
    }

    private void deposit(String owner, double amount) {
        Account account = accountRepository.findByOwner(owner)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + owner));
        account.setBalance(account.getBalance() + amount);
        log.info("Deposited {} to {} (balance: {})", amount, owner, account.getBalance());
    }

    private void simulateLongProcessing() throws TimeoutException {
        try {
            Thread.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        throw new TimeoutException("Transfer took too long");
    }

    public static class TimeoutException extends Exception {
        public TimeoutException(String message) {
            super(message);
        }
    }
}
