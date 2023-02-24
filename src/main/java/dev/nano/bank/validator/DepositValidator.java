package dev.nano.bank.validator;

import lombok.extern.slf4j.Slf4j;
import dev.nano.bank.dto.DepositDto;

import java.math.BigDecimal;

@Slf4j
public class DepositValidator {
    public static final int MAX_AMOUNT = 10_000;
    public static final int MIN_AMOUNT = 10;

    public static boolean isValid(DepositDto deposit) {
        if (deposit.getAmount() == null) {
            log.info("Amount is null");
            return false;
        } else if (deposit.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.info("Amount is 0");
            return false;
        } else if (deposit.getAmount().compareTo(BigDecimal.valueOf(MIN_AMOUNT)) < 0) {
            log.info("Min amount to deposit is 10");
            return false;
        } else if (deposit.getAmount().compareTo(BigDecimal.valueOf(MAX_AMOUNT)) > 0) {
            log.info("Max amount to deposit is 10 000");
            return false;
        }

        if (deposit.getReason() != null && !deposit.getReason().equals("")) {
            log.info("Reason is empty");
            return false;
        }
        return true;
    }
}
