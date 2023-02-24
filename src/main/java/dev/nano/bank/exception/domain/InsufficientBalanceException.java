package dev.nano.bank.exception.domain;

public class InsufficientBalanceException extends Exception {

  private static final long serialVersionUID = 1L;

  public InsufficientBalanceException() {
  }

  public InsufficientBalanceException(String message) {
    super(message);
  }
}
