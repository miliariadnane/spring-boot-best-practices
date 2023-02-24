package dev.nano.bank.service;

import dev.nano.bank.exception.domain.TransactionNotFoundException;

import java.util.List;

public interface TransactionBaseService<T> {
    List<T> getAllTransactions() throws TransactionNotFoundException;
}
