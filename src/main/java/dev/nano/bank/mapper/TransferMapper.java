package dev.nano.bank.mapper;

import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.Transfer;
import dev.nano.bank.dto.TransferDto;
import dev.nano.bank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TransferMapper {
    private AccountRepository accountRepository;

    public Transfer toEntity(TransferDto transferDto) {
        Transfer transfer = new Transfer();
        transfer.setAmount(transferDto.getAmount());
        transfer.setReason(transferDto.getReason());
        transfer.setDateExecution(transferDto.getDateExecution());
        Optional<Account> senderAccount = accountRepository.findAccountByAccountNumber(transferDto.getSenderAccountNumber());
        Optional<Account> receiverAccount = accountRepository.findAccountByAccountNumber(transferDto.getReceiverAccountNumber());
        transfer.setSenderAccount(senderAccount.get());
        transfer.setReceiverAccount(receiverAccount.get());
        return transfer;
    }

    public TransferDto toDTO(Transfer transfer) {
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountNumber(transfer.getSenderAccount().getAccountNumber());
        transferDto.setReceiverAccountNumber(transfer.getReceiverAccount().getAccountNumber());
        transferDto.setDateExecution(transfer.getDateExecution());
        transferDto.setReason(transfer.getReason());
        transferDto.setAmount(transfer.getAmount());
        return transferDto;
    }


    public List<TransferDto> toListDto(List<Transfer> transfers) {
        List<TransferDto> transferDtos = new ArrayList<>();
        for (Transfer transfer : transfers) {
            transferDtos.add(toDTO(transfer));
        }
        return transferDtos;
    }
}
