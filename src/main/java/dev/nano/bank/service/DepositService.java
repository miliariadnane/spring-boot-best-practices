package dev.nano.bank.service;

import dev.nano.bank.dto.DepositDto;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.exception.domain.TransactionException;

public interface DepositService extends TransactionBaseService<DepositDto> {
    void depositMoney(DepositDto deposit) throws AccountNotFoundException, TransactionException;
}
