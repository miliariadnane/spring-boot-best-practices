package dev.nano.bank.web;

import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.Transfer;
import dev.nano.bank.dto.TransferDto;
import dev.nano.bank.mapper.TransferMapper;
import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.Role;
import dev.nano.bank.service.TransferService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransferControllerTest {
    @MockBean
    private TransferService transferService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TransferMapper transferMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/transfers/list - Success")
    void itShouldGetAllTransfersSuccessfully() throws Exception {
        // Given
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

        Account mockAccount1 = new Account(1L, "010000B025001001", "RIB1", BigDecimal.valueOf(200000L), user1);
        Account mockAccount2 = new Account(2L, "010000B025001002", "RIB1", BigDecimal.valueOf(150000L), user1);
        Transfer mockTransfer1 = new Transfer(
                1L,
                BigDecimal.valueOf(100000L),
                new Date(),
                mockAccount1,
                mockAccount2,
                "The reason is ...."
        );

        TransferDto transferDto = transferMapper.toDTO(mockTransfer1);

        List<Transfer> transferList = List.of(mockTransfer1);
        List<TransferDto> transferDtoList = Arrays.asList(transferDto);
        given(transferService.getAllTransactions()).willReturn(transferDtoList);

        // When
        mockMvc.perform(get("/api/v1/transfers/list")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(jsonPath("$", hasSize(1)))
//                        .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                        .andExpect(jsonPath("$[0].amount").value(transferList.get(0).getAmount()))
                        .andExpect(jsonPath("$[0].reason").value(transferList.get(0).getReason()))
                        .andExpect(jsonPath("$[0].senderAccountNumber").value(transferList.get(0).getSenderAccount().getAccountNumber()))
                        .andExpect(jsonPath("$[0].receiverAccountNumber").value(transferList.get(0).getReceiverAccount().getAccountNumber()));

    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
