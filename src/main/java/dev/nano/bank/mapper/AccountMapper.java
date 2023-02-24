package dev.nano.bank.mapper;

import dev.nano.bank.domain.Account;
import dev.nano.bank.dto.AccountDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMapper {
    public AccountDto toDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setBalance(account.getBalance());
        accountDto.setRib(account.getRib());
        accountDto.setUser(account.getUser());
        return accountDto;
    }

    public List<AccountDto> toListDto(List<Account> accounts) {
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountDtos.add(toDto(account));
        }
        return accountDtos;
    }
}
