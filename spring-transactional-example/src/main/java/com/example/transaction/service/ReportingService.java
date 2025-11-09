package com.example.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.repository.AccountRepository;

@Service
public class ReportingService {

    private static final Logger log = LoggerFactory.getLogger(ReportingService.class);

    private final AccountRepository accountRepository;

    public ReportingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void printAverageBalance() {
        double avg = accountRepository.findAll().stream()
                .mapToDouble(account -> account.getBalance())
                .average()
                .orElse(0.0);
        log.info("Average balance is {}", avg);
    }
}
