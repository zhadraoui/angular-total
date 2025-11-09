package com.example.transaction;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.transaction.service.ScenarioRunner;

@SpringBootApplication
public class TransactionalDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionalDemoApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(ScenarioRunner scenarioRunner) {
        return args -> scenarioRunner.runAllScenarios();
    }
}
