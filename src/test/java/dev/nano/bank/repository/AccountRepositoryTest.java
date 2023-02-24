package dev.nano.bank.repository;

import dev.nano.bank.domain.Account;
import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void itShouldSelectAccountByAccountNumber() {
        // Given
        String accountNumber = "010000B0250010AP";
        /* Create an instance of User */
        String role_super_admin = Role.ROLE_SUPER_ADMIN.toString();
        String username = "NANO23";
        User user = new User(
                1L,
                username,
                passwordEncoder.encode("password123"),
                "MALE",
                "na",
                "no",
                new Date(),
                role_super_admin,
                getRoleEnumName(role_super_admin).getAuthorities(),
                true
        );
        userRepository.save(user);
        Account account = new Account(2L, accountNumber, "RIB1", BigDecimal.valueOf(200000L), user);

        // When
        underTest.save(account);

        // Then
        Optional<Account> optionalAccount = underTest.findAccountByAccountNumber(accountNumber);
        assertThat(optionalAccount)
                .isPresent()
                .hasValueSatisfying(acc -> {
                    assertThat(acc).isEqualToComparingFieldByField(account);
                });
    }

    @Test
    void itShouldNotSelectAccountByAccountNumberWhenNumberDoesExists() {
        // Given
        String accountNumber = "010000B025001001";

        // When
        Optional<Account> optionalAccount = underTest.findAccountByAccountNumber(accountNumber);

        // Then
        assertThat(optionalAccount).isNotPresent();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
