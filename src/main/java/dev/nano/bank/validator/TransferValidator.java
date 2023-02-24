package dev.nano.bank.validator;

import dev.nano.bank.dto.TransferDto;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class TransferValidator {
    public static final int MAX_AMOUNT = 10_000;
    public static final int MIN_AMOUNT = 10;

    public static boolean isValid(TransferDto transfer) {
        if (transfer.getAmount() == null) {
            log.info("Amount is null");
            return false;
        } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("Amount is 0");
            return false;
        } else if (transfer.getAmount().compareTo(BigDecimal.valueOf(MIN_AMOUNT)) < 0) {
            log.info("Min amount to send a transfer is 10");
            return false;
        } else if (transfer.getAmount().compareTo(BigDecimal.valueOf(MAX_AMOUNT)) > 0) {
            log.info("Max amount to send a transfer is 10 000");
            return false;
        }

        if (transfer.getReason() != null && !transfer.getReason().equals("")) {
            log.info("Reason is empty");
            return false;
        }
        return true;
    }
}
