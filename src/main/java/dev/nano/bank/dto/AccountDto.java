package dev.nano.bank.dto;

import lombok.Getter;
import lombok.Setter;
import dev.nano.bank.domain.User;
import java.math.BigDecimal;

@Getter @Setter
public class AccountDto {
    private String accountNumber;
    private String rib;
    private BigDecimal balance;
    private User user;
}
