package dev.nano.bank.web;

import dev.nano.bank.dto.UserDto;
import dev.nano.bank.exception.domain.UserNotFoundException;
import dev.nano.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers() throws UserNotFoundException {
        log.info("Retrieve list of all users");
        return new ResponseEntity<>(
                userService.getAllUsers(),
                HttpStatus.OK
        );
    }
}
