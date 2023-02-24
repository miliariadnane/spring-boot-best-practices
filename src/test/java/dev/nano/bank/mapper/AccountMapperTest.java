package dev.nano.bank.mapper;

import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.User;
import dev.nano.bank.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountMapperTest {
    @Mock
    private Account account;

    @InjectMocks
    private AccountMapper accountMapper;

    @Test
    void testToDto() {
        // Given
        String expectedAccountNumber = "010000B0250010AP";
        String expectedRib = "RIB1";
        BigDecimal expectedBalance = new BigDecimal(1000);
        User expectedUser = new User();
        given(account.getAccountNumber()).willReturn(expectedAccountNumber);
        given(account.getRib()).willReturn(expectedRib);
        given(account.getBalance()).willReturn(expectedBalance);
        given(account.getUser()).willReturn(expectedUser);

        // When
        AccountDto accountDto = accountMapper.toDto(account);

        // Then
        assertThat(accountDto.getAccountNumber()).isEqualTo(expectedAccountNumber);
        assertThat(accountDto.getRib()).isEqualTo(expectedRib);
        assertThat(accountDto.getBalance()).isEqualTo(expectedBalance);
        assertThat(accountDto.getUser()).isEqualTo(expectedUser);
    }

    @Test
    void testToListDto() {
        // Given
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        // When
        List<AccountDto> accountDtos = accountMapper.toListDto(accounts);

        // Then
        assertThat(accountDtos).hasSize(1);
        assertThat(accountDtos.get(0)).isEqualToComparingFieldByField(accountMapper.toDto(account));
    }
}
