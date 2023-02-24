package dev.nano.bank.repository;

import dev.nano.bank.domain.Transfer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TransferRepository extends TransactionBaseRepository<Transfer> {
    @Query(value = "SELECT count(t) FROM Transfer t " +
            "WHERE t.receiverAccount.rib = :rib AND t.dateExecution >= :startDate AND t.dateExecution < :endDate"
    )
    int countTransactionsByRibPerDay(
            @Param("rib") String rib,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate
    );
}
