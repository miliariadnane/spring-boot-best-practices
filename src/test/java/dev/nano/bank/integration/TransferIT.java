package dev.nano.bank.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nano.bank.domain.Transfer;
import dev.nano.bank.service.AuditService;
import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.EventType;
import dev.nano.bank.domain.enumration.Role;
import dev.nano.bank.dto.TransferDto;
import dev.nano.bank.repository.AccountRepository;
import dev.nano.bank.repository.TransferRepository;
import dev.nano.bank.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class TransferIT {
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditService auditService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    private final String senderAccountNumber = "010000B025001001";
    private final String receiverAccountNumber = "010000B025001002";
    private final String reason = "salary";
    private final BigDecimal amount = BigDecimal.valueOf(5000L);

    @Test
    void itShouldSendTransferWithSuccess() throws Exception {
        // Given
        TransferDto transferDto = new TransferDto(senderAccountNumber, receiverAccountNumber, reason, amount, new Date());

        String role_super_admin = Role.ROLE_SUPER_ADMIN.toString();
        User user1 = new User(
                1L,
                "nano1",
                passwordEncoder.encode("password123"),
                "MALE",
                "na",
                "no",
                new Date(),
                role_super_admin,
                getRoleEnumName(role_super_admin).getAuthorities(),
                true
        );
        userRepository.save(user1);

        Account sender = new Account(1L, "010000B025001001", "RIB1", BigDecimal.valueOf(20000L), user1);
        accountRepository.save(sender);
        Account receiver = new Account(2L, "010000B025001002", "RIB2", BigDecimal.valueOf(15000L), user1);
        accountRepository.save(receiver);

        // When
        ResultActions resultActions =  mockMvc.perform(post("/api/v1/transfers/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)));

        // Then
        resultActions.andExpect(status().isOk());

        List<Transfer> transfers = transferRepository.findAll();

        assertThat(transfers).hasSize(2);
        Transfer transfer = transfers.get(1);
        assertThat(transfer.getSenderAccount().getAccountNumber()).isEqualTo(senderAccountNumber);
        assertThat(transfer.getReceiverAccount().getAccountNumber()).isEqualTo(receiverAccountNumber);
        assertThat(transfer.getReason()).isEqualTo(reason);
        assertThat(transfer.getAmount()).isEqualTo(amount);

        Account senderAfterTransfer = accountRepository.findAccountByAccountNumber("010000B025001001").get();
        BigDecimal expectedSenderBalance = BigDecimal.valueOf(15000L);
        assertThat(senderAfterTransfer.getBalance()).isEqualTo(expectedSenderBalance);

        Account receiverAfterTransfer = accountRepository.findAccountByAccountNumber("010000B025001002").get();
        BigDecimal expectedReceiverBalance = BigDecimal.valueOf(20000L);
        assertThat(receiverAfterTransfer.getBalance()).isEqualTo(expectedReceiverBalance);

        verify(auditService).audit(
                EventType.TRANSFER,
                eq("Transfer from 010000B025001001 To 010000B025001002 Amount 5000 Reason salary")
        );
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
