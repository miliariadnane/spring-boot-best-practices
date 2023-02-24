package dev.nano.bank.service;

import dev.nano.bank.exception.domain.UserNotFoundException;
import dev.nano.bank.domain.User;
import dev.nano.bank.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers() throws UserNotFoundException;
    User findUserByUsername(String username);
}
