package dev.nano.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.JOINED)
@Data @AllArgsConstructor @NoArgsConstructor
public class TransactionBase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(precision = 16, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExecution;

    @ManyToOne
    private Account receiverAccount;

    @Column(length = 200)
    private String reason;

    public TransactionBase(BigDecimal amount, Date dateExecution, String reason, Account receiverAccount) {
        this.amount = amount;
        this.dateExecution = dateExecution;
        this.reason = reason;
        this.receiverAccount = receiverAccount;
    }
}
