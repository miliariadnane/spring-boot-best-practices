package dev.nano.bank.web;

import dev.nano.bank.dto.AccountDto;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/list")
    public ResponseEntity<List<AccountDto>> getAllAccounts() throws AccountNotFoundException {
        log.info("Retrieve list of all accounts");
        return new ResponseEntity<>(
                accountService.getAllAccounts(),
                HttpStatus.OK
        );
    }
}
