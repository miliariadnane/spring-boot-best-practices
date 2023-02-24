package dev.nano.bank.exception.domain;

public class AccountNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public AccountNotFoundException() {
  }

  public AccountNotFoundException(String message) {
    super(message);
  }
}
