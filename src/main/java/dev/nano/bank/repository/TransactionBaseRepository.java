package dev.nano.bank.repository;

import dev.nano.bank.domain.TransactionBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionBaseRepository<T extends TransactionBase> extends JpaRepository<T, Long> {
}
