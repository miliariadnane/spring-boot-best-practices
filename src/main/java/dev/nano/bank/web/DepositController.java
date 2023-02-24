package dev.nano.bank.web;

import dev.nano.bank.dto.DepositDto;
import dev.nano.bank.service.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.exception.domain.TransactionException;
import dev.nano.bank.exception.domain.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deposits")
@Slf4j
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;

    @GetMapping("/list")
    public ResponseEntity<List<DepositDto>> getAllDeposits() throws TransactionNotFoundException {
        log.info("Retrieve list of all desposit transactions");
        return new ResponseEntity<>(
                depositService.getAllTransactions(),
                HttpStatus.OK
        );
    }

    @PostMapping("/deposit")
    public void createDeposit(DepositDto depositDto)
            throws AccountNotFoundException, TransactionException {
        log.info("Deposit an amount of money");
        depositService.depositMoney(depositDto);
    }
}
