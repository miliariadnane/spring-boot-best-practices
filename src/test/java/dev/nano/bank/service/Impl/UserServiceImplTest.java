package dev.nano.bank.service.Impl;

import dev.nano.bank.exception.domain.UserNotFoundException;
import dev.nano.bank.repository.UserRepository;
import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.Role;
import dev.nano.bank.dto.UserDto;
import dev.nano.bank.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserServiceImpl underTest;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    @DisplayName("Test getAllUsers success")
    void itShouldGetAllUsersSuccessfully() throws UserNotFoundException {
        // given
        String role_super_admin = Role.ROLE_SUPER_ADMIN.toString();
        String role_user = Role.ROLE_USER.toString();

        User mockUser1 = new User(
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
        User mockUser2 = new User(
                2L,
                "nano2",
                passwordEncoder.encode("password123"),
                "MALE",
                "na",
                "no",
                new Date(),
                role_user,
                getRoleEnumName(role_super_admin).getAuthorities(),
                true
        );

        List<User> users = Arrays.asList(mockUser1, mockUser2);

        UserDto userDto1 = userMapper.toDto(mockUser1);
        UserDto userDto2 = userMapper.toDto(mockUser2);

        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        given(userRepository.findAll()).willReturn(users);
        given(userMapper.toListDto(users)).willReturn(userDtos);

        // When
        List<UserDto> result = underTest.getAllUsers();

        // Then
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toListDto(users);
        assertEquals(2, result.size());
        assertEquals(userDtos, result);
    }

    @Test
    @DisplayName("Test getAllUsers throw exception")
    void itShouldThrowExceptionWhenListUsersIsEmpty() {
        // given
        given(userRepository.findAll()).willReturn(List.of());

        // When
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> underTest.getAllUsers()
        );

        // Then
        verify(userRepository, times(1)).findAll();
        assertEquals("No user found", exception.getMessage());
    }

    @Test
    @DisplayName("Test findUserByUsername success")
    void itShouldFindUserByUsernameSuccessfully() {
        // given
        String username = "nano1";
        User user = new User();
        given(userRepository.findUserByUsername(username)).willReturn(user);

        // when
        User result = underTest.findUserByUsername(username);

        // then
        verify(userRepository, times(1)).findUserByUsername(username);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Test findUserByUsername throw exception")
    void itShouldThrowExceptionWhenUserNotFoundByUsername() {
        // given
        String username = "nano1";
        given(userRepository.findUserByUsername(username)).willReturn(null);

        // when
        User result = underTest.findUserByUsername(username);

        // then
        verify(userRepository, times(1)).findUserByUsername(username);
        assertNull(result);
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
