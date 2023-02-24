package dev.nano.bank.exception;

import dev.nano.bank.exception.domain.AccountNotFoundException;
import dev.nano.bank.exception.domain.InsufficientBalanceException;
import lombok.extern.slf4j.Slf4j;
import dev.nano.bank.dto.HttpResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static dev.nano.bank.util.DateUtil.dateTimeFormatter;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ExceptionHandelingController extends ResponseEntityExceptionHandler implements ErrorController {

    public static final String INSUFFICIENT_BALANCE_EXCEPTION_MESSAGE = "No balance to transfer";
    public static final String ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE = "Account not found";
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(exception.getMessage())
                        .status(status)
                        .statusCode(status.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), status);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<HttpResponse<?>> handleInsufficientBalanceException(InsufficientBalanceException exception) {
        return createHttpErrorResponse(UNAVAILABLE_FOR_LEGAL_REASONS, INSUFFICIENT_BALANCE_EXCEPTION_MESSAGE, exception);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<HttpResponse<?>> handleAccountNotFoundException(AccountNotFoundException exception) {
        return createHttpErrorResponse(UNAUTHORIZED, ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE, exception);
    }

    private ResponseEntity<HttpResponse<?>> createHttpErrorResponse(HttpStatus httpStatus, String reason, Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(reason)
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), httpStatus);
    }
}
