package dev.nano.bank.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DepositDto extends TransactionDto {
    private String senderName;
}
