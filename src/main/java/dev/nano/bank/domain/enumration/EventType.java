package dev.nano.bank.domain.enumration;

public enum EventType {

  TRANSFER("transfer"),
  DEPOSIT("money deposit");

  private String type;

  EventType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
