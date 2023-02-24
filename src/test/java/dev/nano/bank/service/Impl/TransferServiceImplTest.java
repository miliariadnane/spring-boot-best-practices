package dev.nano.bank.service.Impl;

import dev.nano.bank.domain.Transfer;
import dev.nano.bank.service.AuditService;
import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.EventType;
import dev.nano.bank.dto.TransferDto;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.exception.domain.InsufficientBalanceException;
import dev.nano.bank.exception.domain.TransactionException;
import dev.nano.bank.exception.domain.TransactionNotFoundException;
import dev.nano.bank.mapper.TransferMapper;
import dev.nano.bank.repository.AccountRepository;
import dev.nano.bank.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {
    @Mock
    private TransferRepository transferRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AuditService auditService;
    private TransferDto transferDto;
    private Account sender;
    private Account receiver;
    @Mock
    private TransferMapper transferMapper;
    @InjectMocks
    private TransferServiceImpl underTest;
    private final String senderAccountNumber = "010000B025001001";
    private final String receiverAccountNumber = "010000B025001002";
    private final String reason = "salary";
    private final BigDecimal amount = BigDecimal.valueOf(5000L);

    @BeforeEach
    void setUp() {
        underTest = new TransferServiceImpl(transferRepository, transferMapper, accountRepository, auditService);
        transferDto = new TransferDto(senderAccountNumber, receiverAccountNumber, reason, amount, new Date());
        sender = new Account(1L, "010000B025001001", "RIB1", BigDecimal.valueOf(200000L), new User());
        receiver = new Account(2L, "010000B025001002", "RIB2", BigDecimal.valueOf(150000L), new User());
    }

    @Test
    @DisplayName("Test getAllTransfers success")
    void itShouldGetAllTransfersSuccessfully() throws TransactionNotFoundException {
        // Given
        Transfer mockTransfer1 = new Transfer(
                1L,
                BigDecimal.valueOf(100000L),
                new Date(),
                new Account(),
                new Account(),
                "The reason is ...."
        );

        TransferDto transferDto1 = transferMapper.toDTO(mockTransfer1);

        List<Transfer> transfers = List.of(mockTransfer1);
        List<TransferDto> transferDtos = Arrays.asList(transferDto1);

        given(transferRepository.findAll()).willReturn(transfers);
        given(transferMapper.toListDto(transfers)).willReturn(transferDtos);

        // When
        List<TransferDto> result = underTest.getAllTransactions();

        // Then
        then(transferRepository).should().findAll();
        then(transferMapper).should().toListDto(transfers);
        assertEquals(1, result.size());
        assertEquals(transferDtos, result);
    }

    @Test
    @DisplayName("Test getAllTransfers throw Exception")
    void itShouldThrowExceptionWhenGetAllTransfers() {
        // Given
        given(transferRepository.findAll()).willReturn(List.of());

        // When
        TransactionNotFoundException exception = assertThrows(
                TransactionNotFoundException.class,
                () -> underTest.getAllTransactions()
        );

        // Then
        then(transferRepository).should().findAll();
        assertEquals("No transfer found", exception.getMessage());
    }

    @Test
    @DisplayName("Test createTransfer success")
    void itShouldSendTransferWhenSuccessful()
            throws TransactionException, InsufficientBalanceException, AccountNotFoundException {
        // given
        given(accountRepository.findAccountByAccountNumber("010000B025001001"))
                .willReturn(Optional.of(sender));
        given(accountRepository.findAccountByAccountNumber("010000B025001002"))
                .willReturn(Optional.of(receiver));
        given(transferMapper.toEntity(any(TransferDto.class))).willReturn(new Transfer());
        // when
        underTest.createTransfer(transferDto);
        // then
        then(transferRepository).should().save(any(Transfer.class));
    }

    @Test
    @DisplayName("Test saveAudit success while creating transfer")
    void itShouldSaveAuditWhenSuccessful()
            throws TransactionException, InsufficientBalanceException, AccountNotFoundException {
        // given
        given(accountRepository.findAccountByAccountNumber("010000B025001001"))
                .willReturn(Optional.of(sender));
        given(accountRepository.findAccountByAccountNumber("010000B025001002"))
                .willReturn(Optional.of(receiver));
        // when
        underTest.createTransfer(transferDto);
        // then
        then(auditService).should().audit(EventType.TRANSFER,
                """
                Transfer from %s To %s
                Amount %s
                Reason %s
                """.formatted(senderAccountNumber, receiverAccountNumber, amount, reason));
    }

    @Test
    @DisplayName("Test createTransfer throw Exception when receiver account not found")
    void itShouldThrowsAccountNotFoundExceptionSenderAccountNotFoundWhileSendTransfer() {
        // When
        // Then
        when(accountRepository.findAccountByAccountNumber(senderAccountNumber)).thenReturn(Optional.empty());
        assertThrows(
                AccountNotFoundException.class,
                () -> underTest.createTransfer(transferDto)
        );
    }

    @Test
    @DisplayName("Test createTransfer throw Exception when sender account not found")
    void itShouldThrowsAccountNotFoundExceptionReceiverAccountNotFoundWhileSendTransfer() {
        // When
        // Then
        lenient().when(accountRepository.findAccountByAccountNumber(receiverAccountNumber)).thenReturn(Optional.empty());
        assertThrows(
                AccountNotFoundException.class,
                () -> underTest.createTransfer(transferDto)
        );
    }
}
