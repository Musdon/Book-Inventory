package com.musdon.bookinventory.repository;

import com.musdon.bookinventory.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
}
