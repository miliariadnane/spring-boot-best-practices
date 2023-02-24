package dev.nano.bank.web;

import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.Role;
import dev.nano.bank.dto.AccountDto;
import dev.nano.bank.mapper.AccountMapper;
import dev.nano.bank.service.AccountService;
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
class AccountControllerTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    @DisplayName("GET /api/v1/accounts/list - Success")
    void itShouldGetAllAccountsSuccessfully() throws Exception {
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

        AccountDto accountDto1 = accountMapper.toDto(mockAccount1);
        AccountDto accountDto2 = accountMapper.toDto(mockAccount2);

        List<Account> accountList = List.of(mockAccount1, mockAccount2);
        List<AccountDto> accountDtos = Arrays.asList(accountDto1, accountDto2);
        given(accountService.getAllAccounts()).willReturn(accountDtos);

        // When
        mockMvc.perform(get("/api/v1/accounts/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountNumber").value(accountList.get(0).getAccountNumber()))
                .andExpect(jsonPath("$[0].rib").value(accountList.get(0).getRib()))
                .andExpect(jsonPath("$[0].balance").value(accountList.get(0).getBalance()))
                .andExpect(jsonPath("$[0].user.id").value(accountList.get(0).getUser().getId()))
                .andExpect(jsonPath("$[1].accountNumber").value(accountList.get(1).getAccountNumber()))
                .andExpect(jsonPath("$[1].rib").value(accountList.get(1).getRib()))
                .andExpect(jsonPath("$[1].balance").value(accountList.get(1).getBalance()))
                .andExpect(jsonPath("$[1].user.id").value(accountList.get(1).getUser().getId()));
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

}
