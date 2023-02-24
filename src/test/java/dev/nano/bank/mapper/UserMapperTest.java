package dev.nano.bank.mapper;

import dev.nano.bank.domain.User;
import dev.nano.bank.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    @Mock
    private User user;

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void testToDto() {
        // Given
        String expectedUsername = "nano23";
        String expectedFirstname = "na";
        String expectedLastname = "no";
        String expectedGender = "MALE";
        Date expectedBirthdate = new Date();
        given(user.getUsername()).willReturn(expectedUsername);
        given(user.getFirstname()).willReturn(expectedFirstname);
        given(user.getLastname()).willReturn(expectedLastname);
        given(user.getGender()).willReturn(expectedGender);
        given(user.getBirthdate()).willReturn(expectedBirthdate);

        // When
        UserDto userDto = userMapper.toDto(user);

        // Then
        assertThat(userDto.getUsername()).isEqualTo(expectedUsername);
        assertThat(userDto.getFirstname()).isEqualTo(expectedFirstname);
        assertThat(userDto.getLastname()).isEqualTo(expectedLastname);
        assertThat(userDto.getGender()).isEqualTo(expectedGender);
        assertThat(userDto.getBirthdate()).isEqualTo(expectedBirthdate);
    }

    @Test
    void testToListDto() {
        // Given
        List<User> users = new ArrayList<>();
        users.add(user);

        // When
        List<UserDto> userDtos = userMapper.toListDto(users);

        // Then
        assertThat(userDtos).hasSize(1);
        assertThat(userDtos.get(0)).isEqualToComparingFieldByField(userMapper.toDto(user));
    }
}
