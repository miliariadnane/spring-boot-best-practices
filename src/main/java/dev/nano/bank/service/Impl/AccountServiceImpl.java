package dev.nano.bank.service.Impl;

import dev.nano.bank.domain.Account;
import dev.nano.bank.dto.AccountDto;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.mapper.AccountMapper;
import dev.nano.bank.repository.AccountRepository;
import dev.nano.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    @Override
    public List<AccountDto> getAllAccounts() throws AccountNotFoundException {
        log.info("Get all accounts");
        List<Account> accountList = accountRepository.findAll();
        if (accountList.isEmpty()) {
            log.info("No account found");
            throw new AccountNotFoundException("No account found");
        } else {
            return accountMapper.toListDto(accountList);
        }
    }
}
