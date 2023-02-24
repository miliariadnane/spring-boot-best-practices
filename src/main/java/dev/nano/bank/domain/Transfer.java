package dev.nano.bank.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transfers")
@Data @AllArgsConstructor @NoArgsConstructor
public class Transfer extends TransactionBase implements Serializable {
  @ManyToOne
  private Account senderAccount;

  public Transfer(BigDecimal amount, Date dateExecution, Account senderAccount, Account receiverAccount, String reason) {
    super(amount, dateExecution, reason, receiverAccount);
    this.senderAccount = senderAccount;
  }

  public Transfer(Long id, BigDecimal amount, Date dateExecution, Account senderAccount, Account receiverAccount, String reason) {
    super(id, amount, dateExecution, receiverAccount, reason);
    this.senderAccount = senderAccount;
  }
}
