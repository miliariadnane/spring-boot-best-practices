package dev.nano.bank.service.Impl;

import dev.nano.bank.domain.Account;
import dev.nano.bank.dto.AccountDto;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.repository.AccountRepository;
import dev.nano.bank.domain.User;
import dev.nano.bank.mapper.AccountMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl underTest;

    @Test
    @DisplayName("Test getAllAccounts success")
    void itShouldGetAllAccountsSuccessfully() throws AccountNotFoundException {
        // Given
        Account mockAccount1 = new Account(1L, "010000B025001001", "RIB1", BigDecimal.valueOf(200000L), new User());
        Account mockAccount2 = new Account(2L, "010000B025001002", "RIB1", BigDecimal.valueOf(150000L), new User());

        AccountDto accountDto1 = accountMapper.toDto(mockAccount1);
        AccountDto accountDto2 = accountMapper.toDto(mockAccount2);

        List<Account> accountList = List.of(mockAccount1, mockAccount2);
        List<AccountDto> accountDtos = Arrays.asList(accountDto1, accountDto2);

        given(accountRepository.findAll()).willReturn(accountList);
        given(accountMapper.toListDto(accountList)).willReturn(accountDtos);

        // When
        List<AccountDto> result = underTest.getAllAccounts();

        // Then
        then(accountRepository).should().findAll();
        then(accountMapper).should().toListDto(accountList);
        assertEquals(2, result.size());
        assertEquals(accountDtos, result);
    }

    @Test
    @DisplayName("Test getAllAccounts throw exception")
    void itShouldThrowExceptionWhenListAccountsIsEmpty() {
        // given
        given(accountRepository.findAll()).willReturn(List.of());

        // When
        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> underTest.getAllAccounts()
        );

        // Then
        verify(accountRepository, times(1)).findAll();
        assertEquals("No account found", exception.getMessage());
    }
}
