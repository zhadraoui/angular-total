package com.example.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NonTransactionalService {

    private static final Logger log = LoggerFactory.getLogger(NonTransactionalService.class);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void generateOutOfBandReport() {
        log.info("Generating report outside transactional context");
    }
}
