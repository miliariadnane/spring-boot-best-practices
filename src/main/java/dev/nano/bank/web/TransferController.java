package dev.nano.bank.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.nano.bank.dto.TransferDto;
import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.exception.domain.InsufficientBalanceException;
import dev.nano.bank.exception.domain.TransactionException;
import dev.nano.bank.exception.domain.TransactionNotFoundException;
import dev.nano.bank.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfers")
@Slf4j
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @GetMapping("/list")
    public ResponseEntity<List<TransferDto>> getAllTransfers() throws TransactionNotFoundException {
        log.info("Retrieve list of all transfer transactions");
        return new ResponseEntity<>(
                transferService.getAllTransactions(),
                HttpStatus.OK
        );
    }

    @PostMapping("/send")
    public void createTransfer(@RequestBody TransferDto transfer)
            throws AccountNotFoundException, TransactionException, InsufficientBalanceException {
        log.info("Create a new transfer");
        transferService.createTransfer(transfer);
    }
}
