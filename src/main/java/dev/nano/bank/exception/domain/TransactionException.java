package dev.nano.bank.exception.domain;

public class TransactionException extends Exception {

  private static final long serialVersionUID = 1L;

  public TransactionException() {
  }

  public TransactionException(String message) {
    super(message);
  }
}
