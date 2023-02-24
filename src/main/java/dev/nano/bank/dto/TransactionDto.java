package dev.nano.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class TransactionDto {
    private String receiverAccountNumber;
    private String reason;
    private BigDecimal amount;
    private Date dateExecution;
}
