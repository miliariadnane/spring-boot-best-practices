package dev.nano.bank.mapper;

import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.Deposit;
import dev.nano.bank.dto.DepositDto;
import dev.nano.bank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DepositMapper {
    private AccountRepository accountRepository;

    public Deposit toEntity(DepositDto depositDto) {
        Deposit deposit = new Deposit();
        deposit.setAmount(depositDto.getAmount());
        deposit.setReason(depositDto.getReason());
        deposit.setDateExecution(depositDto.getDateExecution());
        Optional<Account> receiverAccount = accountRepository.findAccountByAccountNumber(depositDto.getReceiverAccountNumber());
        deposit.setSenderName(depositDto.getSenderName());
        deposit.setReceiverAccount(receiverAccount.get());
        return deposit;
    }

    public DepositDto toDTO(Deposit deposit) {
        DepositDto depositDto = new DepositDto();
        depositDto.setReceiverAccountNumber(deposit.getReceiverAccount().getAccountNumber());
        depositDto.setSenderName(deposit.getSenderName());
        depositDto.setDateExecution(deposit.getDateExecution());
        depositDto.setReason(deposit.getReason());
        depositDto.setAmount(deposit.getAmount());
        return depositDto;
    }

    public List<DepositDto> toListDto(List<Deposit> deposits) {
        List<DepositDto> depositDtoList = new ArrayList<>();
        for (Deposit deposit : deposits) {
            depositDtoList.add(toDTO(deposit));
        }
        return depositDtoList;
    }
}
