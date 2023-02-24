package dev.nano.bank.service;

import dev.nano.bank.domain.enumration.EventType;

public interface AuditService {
    void audit(EventType eventType, String message);
}
