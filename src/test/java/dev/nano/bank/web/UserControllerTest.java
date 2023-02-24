package dev.nano.bank.web;

import dev.nano.bank.domain.User;
import dev.nano.bank.domain.enumration.Role;
import dev.nano.bank.service.UserService;
import org.junit.jupiter.api.Disabled;
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

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Disabled
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/users/list - Success")
    void itShouldGetAllUsersSuccessfully() throws Exception {
        // Given
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

        doReturn(Arrays.asList(mockUser1, mockUser2)).when(userService).getAllUsers();

        // Execute the GET request
        mockMvc.perform(get("/api/v1/users/list"))

                // Validate response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))

                // Validate the response fields
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].id").value(is(1)))
                .andExpect(jsonPath("$[1].id").value(is(2)))
                .andExpect(jsonPath("$[0].role").value(is(mockUser1.getRole())))
                .andExpect(jsonPath("$[1].role").value(is(mockUser2.getRole())))
                .andExpect(jsonPath("$[0].username").value(is(mockUser1.getUsername())))
                .andExpect(jsonPath("$[1].username").value(is(mockUser2.getUsername())))
                .andExpect(jsonPath("$[0].firstName").value(is(mockUser1.getFirstname())))
                .andExpect(jsonPath("$[1].firstName").value(is(mockUser2.getFirstname())))
                .andExpect(jsonPath("$[0].lastName").value(is(mockUser1.getLastname())))
                .andExpect(jsonPath("$[1].lastName").value(is(mockUser2.getLastname())));
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

}
