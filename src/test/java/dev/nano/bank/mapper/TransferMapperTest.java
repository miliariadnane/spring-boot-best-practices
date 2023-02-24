package dev.nano.bank.mapper;

import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.Transfer;
import dev.nano.bank.dto.TransferDto;
import dev.nano.bank.repository.AccountRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TransferMapperTest {
    @Mock
    private Transfer transfer;
    @Mock
    private TransferDto transferDto;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private Account senderAccount;
    @Mock
    private Account receiverAccount;
    @InjectMocks
    private TransferMapper transferMapper;

    @Test
    void testToEntity() {
        // Given
        BigDecimal expectedAmount = new BigDecimal(1000);
        String expectedReason = "testReason";
        Date expectedDateExecution = new Date();
        String expectedSenderAccountNumber = "010000B0250010AP";
        String expectedReceiverAccountNumber = "010555B0250010OP";
        given(transferDto.getAmount()).willReturn(expectedAmount);
        given(transferDto.getReason()).willReturn(expectedReason);
        given(transferDto.getDateExecution()).willReturn(expectedDateExecution);
        given(transferDto.getSenderAccountNumber()).willReturn(expectedSenderAccountNumber);
        given(transferDto.getReceiverAccountNumber()).willReturn(expectedReceiverAccountNumber);
        given(accountRepository.findAccountByAccountNumber(expectedSenderAccountNumber))
                .willReturn(Optional.of(senderAccount));
        given(accountRepository.findAccountByAccountNumber(expectedReceiverAccountNumber))
                .willReturn(Optional.of(receiverAccount));

        // When
        Transfer transfer = transferMapper.toEntity(transferDto);

        // Then
        assertThat(transfer.getAmount()).isEqualTo(expectedAmount);
        assertThat(transfer.getReason()).isEqualTo(expectedReason);
        assertThat(transfer.getDateExecution()).isEqualTo(expectedDateExecution);
        assertThat(transfer.getSenderAccount()).isEqualTo(senderAccount);
        assertThat(transfer.getReceiverAccount()).isEqualTo(receiverAccount);
    }

    @Test
    void testToDto() {
        // Given
        BigDecimal expectedAmount = new BigDecimal(20000);
        String expectedReason = "Monthly Salary";
        Date expectedDateExecution = new Date();
        String expectedSenderAccountNumber = "010000B0250010AP";
        String expectedReceiverAccountNumber = "010555B0250010OP";
        given(transfer.getAmount()).willReturn(expectedAmount);
        given(transfer.getReason()).willReturn(expectedReason);
        given(transfer.getDateExecution()).willReturn(expectedDateExecution);
        given(transfer.getSenderAccount()).willReturn(senderAccount);
        given(transfer.getReceiverAccount()).willReturn(receiverAccount);
        given(senderAccount.getAccountNumber()).willReturn(expectedSenderAccountNumber);
        given(receiverAccount.getAccountNumber()).willReturn(expectedReceiverAccountNumber);

        // When
        TransferDto transferDto = transferMapper.toDTO(transfer);

        // Then
        assertThat(transferDto.getSenderAccountNumber()).isEqualTo(expectedSenderAccountNumber);
        assertThat(transferDto.getReceiverAccountNumber()).isEqualTo(expectedReceiverAccountNumber);
        assertThat(transferDto.getDateExecution()).isEqualTo(expectedDateExecution);
        assertThat(transferDto.getReason()).isEqualTo(expectedReason);
        assertThat(transferDto.getAmount()).isEqualTo(expectedAmount);
    }

    @Test
    @Disabled
    void testToListDto() {
        // Given
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(transfer);

        // When
        List<TransferDto> transferDtos = transferMapper.toListDto(transfers);

        // Then
        assertThat(transferDtos).hasSize(1);
        assertThat(transferDtos.get(0)).isEqualToComparingFieldByField(transferMapper.toDTO(transfer));
    }
}
