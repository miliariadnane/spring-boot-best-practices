package dev.nano.bank.exception.domain;

public class TransactionNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public TransactionNotFoundException() {
  }

  public TransactionNotFoundException(String message) {
    super(message);
  }
}
