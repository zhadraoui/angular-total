package com.example.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void writeAuditEntry(String from, String to, double amount) {
        log.info("Audit entry persisted: {} -> {} : {}", from, to, amount);
    }
}
