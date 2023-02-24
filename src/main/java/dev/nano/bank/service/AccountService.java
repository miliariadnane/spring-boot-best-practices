package dev.nano.bank.service;

import dev.nano.bank.dto.AccountDto;
import dev.nano.bank.exception.domain.AccountNotFoundException;

import java.util.List;

public interface AccountService {
    List<AccountDto> getAllAccounts() throws AccountNotFoundException;
}
