package dev.nano.bank.repository;

import dev.nano.bank.domain.Deposit;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends TransactionBaseRepository<Deposit> {

}
