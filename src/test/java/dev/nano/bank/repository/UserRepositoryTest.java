package dev.nano.bank.repository;

import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void itShouldSelectUserByUsernameIfPresent() {
        // Given
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
        underTest.save(user);

        // When
        User userByUsername = underTest.findUserByUsername(username);

        // then
        assertThat(userByUsername).isNotNull();
        assertThat(userByUsername.getUsername()).isEqualTo(username);
    }

    @Test
    void willThrowExceptionIfUserNotPresentByUsername() {
        // Given
        String username = "nanodev";

        // When
        User userByUsername = underTest.findUserByUsername(username);

        // Then
        assertThat(userByUsername).isNull();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

}
