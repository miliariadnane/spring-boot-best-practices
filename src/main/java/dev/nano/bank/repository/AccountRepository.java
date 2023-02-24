package dev.nano.bank.repository;

import dev.nano.bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findAccountByAccountNumber(String accountNumber);
}
