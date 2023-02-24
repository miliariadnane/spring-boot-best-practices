package dev.nano.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class TransferDto extends TransactionDto {
    private String senderAccountNumber;

    public TransferDto(String senderAccountNumber, String receiverAccountNumber, String reason, BigDecimal amount, Date dateExecution) {
        super(receiverAccountNumber, reason, amount, dateExecution);
        this.senderAccountNumber = senderAccountNumber;
    }
}
