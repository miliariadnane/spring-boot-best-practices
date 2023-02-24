package dev.nano.bank.service.Impl;

import dev.nano.bank.domain.Transfer;
import dev.nano.bank.dto.TransferDto;
import dev.nano.bank.repository.AccountRepository;
import dev.nano.bank.repository.TransferRepository;
import dev.nano.bank.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.enumration.EventType;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.exception.domain.InsufficientBalanceException;
import dev.nano.bank.exception.domain.TransactionException;
import dev.nano.bank.exception.domain.TransactionNotFoundException;
import dev.nano.bank.mapper.TransferMapper;
import dev.nano.bank.service.TransferService;
import dev.nano.bank.validator.TransferValidator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;
    private final AccountRepository accountRepository;
    private final AuditService auditService;

    @Override
    public List<TransferDto> getAllTransactions() throws TransactionNotFoundException {

        log.info("Get all transfers");
        List<Transfer> transferList = transferRepository.findAll();
        if (transferList.isEmpty()) {
            log.info("No transfer found");
            throw new TransactionNotFoundException("No transfer found");
        } else {
            return transferMapper.toListDto(transferList);
        }
    }

    @Override
    public void createTransfer(TransferDto transfer) throws AccountNotFoundException, TransactionException, InsufficientBalanceException {


        Account sender = accountRepository.findAccountByAccountNumber(transfer.getSenderAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        Account receiver = accountRepository.findAccountByAccountNumber(transfer.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        if(!TransferValidator.isValid(transfer)) {
            throw new TransactionException("Invalid transfer");
        }

        if (sender.getBalance().intValue() - transfer.getAmount().intValue() < 0) {
            log.info("Insufficient balance");
            throw new InsufficientBalanceException("Insufficient balance for the user");
        }

        sender.setBalance(sender.getBalance().subtract(transfer.getAmount()));
        accountRepository.save(sender);

        receiver.setBalance(new BigDecimal(receiver.getBalance().intValue() + transfer.getAmount().intValue()));

        accountRepository.save(receiver);

        transfer.setReceiverAccountNumber(receiver.getAccountNumber());
        transfer.setSenderAccountNumber(sender.getAccountNumber());

        transferRepository.save(transferMapper.toEntity(transfer));

        auditService.audit(EventType.TRANSFER,
                """
                Transfer from %s To %s
                Amount %s
                Reason %s
                """.formatted(transfer.getSenderAccountNumber(), transfer.getReceiverAccountNumber(),
                transfer.getAmount(), transfer.getReason()));
    }
}
