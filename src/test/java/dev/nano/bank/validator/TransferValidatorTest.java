package dev.nano.bank.validator;

import dev.nano.bank.dto.TransferDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class TransferValidatorTest {
    @Mock
    private TransferDto transfer;

    @Test
    void itShouldReturnFalseWhenAmountIsNull() {
        given(transfer.getAmount()).willReturn(null);
        assertThat(TransferValidator.isValid(transfer)).isFalse();
    }

    @Test
    void itShouldReturnFalseWhenAmountIs0() {
        given(transfer.getAmount()).willReturn(BigDecimal.valueOf(0));
        assertThat(TransferValidator.isValid(transfer)).isFalse();
    }

    @Test
    void itShouldReturnFalseWhenAmountIsLessThan10() {
        given(transfer.getAmount()).willReturn(BigDecimal.valueOf(9));
        assertThat(TransferValidator.isValid(transfer)).isFalse();
    }

    @Test
    void itShouldReturnFalseWhenAmountIsGreaterThanMax() {
        given(transfer.getAmount()).willReturn(BigDecimal.valueOf(TransferValidator.MAX_AMOUNT + 1));
        assertThat(TransferValidator.isValid(transfer)).isFalse();
    }

    @Test
    void itShouldReturnFalseWhenReasonIsEmpty() {
        lenient().when(transfer.getReason()).thenReturn("");
        assertThat(TransferValidator.isValid(transfer)).isFalse();
    }

    @Test
    void itShouldReturnTrueWhenTransferIsValid() {
        given(transfer.getAmount()).willReturn(BigDecimal.valueOf(100));
        given(transfer.getReason()).willReturn("valid reason");
        assertThat(TransferValidator.isValid(transfer)).isTrue();
    }
}
