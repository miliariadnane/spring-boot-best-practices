package dev.nano.bank.service.Impl;

import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.Deposit;
import dev.nano.bank.dto.DepositDto;
import dev.nano.bank.repository.AccountRepository;
import dev.nano.bank.service.AuditService;
import dev.nano.bank.validator.DepositValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.nano.bank.domain.enumration.EventType;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.exception.domain.TransactionException;
import dev.nano.bank.exception.domain.TransactionNotFoundException;
import dev.nano.bank.mapper.DepositMapper;
import dev.nano.bank.repository.DepositRepository;
import dev.nano.bank.service.DepositService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepositServiceImpl implements DepositService {

    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;
    private final AccountRepository accountRepository;
    private final AuditService auditService;

    @Override
    public List<DepositDto> getAllTransactions() throws TransactionNotFoundException {

        log.info("Get all deposits");
        List<Deposit> depositList = depositRepository.findAll();
        if (depositList.isEmpty()) {
            log.info("No transaction found");
            throw new TransactionNotFoundException("No transaction found");
        } else {
            return depositMapper.toListDto(depositList);
        }
    }

    @Override
    public void depositMoney(DepositDto deposit) throws AccountNotFoundException, TransactionException {

        Account receiver = accountRepository.findAccountByAccountNumber(deposit.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        if(!DepositValidator.isValid(deposit)) {
            throw new TransactionException("Invalid transfer");
        }

        receiver.setBalance(new BigDecimal(receiver.getBalance().intValue() + deposit.getAmount().intValue()));
        accountRepository.save(receiver);

        deposit.setSenderName(deposit.getSenderName());
        deposit.setReceiverAccountNumber(receiver.getAccountNumber());

        depositRepository.save(depositMapper.toEntity(deposit));

        auditService.audit(EventType.DEPOSIT,
                """
                Deposit from %s
                Amount %s
                Reason %s
                """.formatted(deposit.getSenderName(), deposit.getAmount(), deposit.getReason()));
    }
}
