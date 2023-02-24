package dev.nano.bank.service;

import dev.nano.bank.dto.TransferDto;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.exception.domain.InsufficientBalanceException;
import dev.nano.bank.exception.domain.TransactionException;

public interface TransferService extends TransactionBaseService<TransferDto> {
    void createTransfer(TransferDto transfer) throws AccountNotFoundException, TransactionException, InsufficientBalanceException;
}
