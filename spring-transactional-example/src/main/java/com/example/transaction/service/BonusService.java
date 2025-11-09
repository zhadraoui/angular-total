package com.example.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.domain.Account;
import com.example.transaction.repository.AccountRepository;

@Service
public class BonusService {

    private static final Logger log = LoggerFactory.getLogger(BonusService.class);

    private final AccountRepository accountRepository;

    public BonusService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(propagation = Propagation.NESTED)
    public void applyBonusWithNested(String owner) {
        Account account = accountRepository.findByOwner(owner)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + owner));
        account.setBalance(account.getBalance() + 5);
        log.info("Bonus applied to {} (balance: {})", owner, account.getBalance());
        if (owner.startsWith("B")) {
            throw new IllegalStateException("Simulated failure for nested transaction");
        }
    }
}
