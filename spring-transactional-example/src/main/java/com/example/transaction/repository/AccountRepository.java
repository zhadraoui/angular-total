package com.example.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByOwner(String owner);
}
