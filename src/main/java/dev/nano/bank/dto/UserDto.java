package dev.nano.bank.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter @Getter
public class UserDto {
    private String username;
    private String gender;
    private String lastname;
    private String firstname;
    private Date birthdate;
}
