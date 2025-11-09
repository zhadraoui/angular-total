package com.example.transaction.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.transaction.domain.Account;
import com.example.transaction.repository.AccountRepository;

@Component
public class ScenarioRunner {

    private static final Logger log = LoggerFactory.getLogger(ScenarioRunner.class);

    private final AccountRepository accountRepository;
    private final TransferService transferService;
    private final ReportingService reportingService;
    private final NonTransactionalService nonTransactionalService;

    public ScenarioRunner(AccountRepository accountRepository,
                          TransferService transferService,
                          ReportingService reportingService,
                          NonTransactionalService nonTransactionalService) {
        this.accountRepository = accountRepository;
        this.transferService = transferService;
        this.reportingService = reportingService;
        this.nonTransactionalService = nonTransactionalService;
    }

    public void runAllScenarios() {
        reset();
        log.info("=== REQUIRED propagation ===");
        transferService.transferRequired("Alice", "Bob", 25);

        log.info("=== REQUIRES_NEW for audit ===");
        transferService.transferWithAudit("Alice", "Bob", 10);

        log.info("=== REQUIRES_NEW bonus with independent rollback ===");
        try {
            transferService.transferWithRequiresNewBonus("Alice", "Bob", 15);
        } catch (Exception ex) {
            log.warn("Bonus rolled back but outer transfer committed", ex);
        }

        log.info("=== SUPPORTS read-only reporting ===");
        reportingService.printAverageBalance();

        log.info("=== NOT_SUPPORTED blocking transactional context ===");
        nonTransactionalService.generateOutOfBandReport();

        log.info("=== TIMEOUT / ROLLBACK rules ===");
        try {
            transferService.transferWithTimeout("Alice", "Bob", 5);
        } catch (Exception ex) {
            log.warn("Timeout scenario rolled back", ex);
        }

        log.info("Final state: {}", List.copyOf(accountRepository.findAll()));
    }

    private void reset() {
        accountRepository.deleteAll();
        accountRepository.saveAll(List.of(
                new Account("Alice", 100),
                new Account("Bob", 20),
                new Account("Charlie", 50)));
    }
}
