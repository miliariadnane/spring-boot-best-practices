package dev.nano.bank.service.Impl;

import dev.nano.bank.domain.enumration.EventType;
import dev.nano.bank.repository.AuditRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {
    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    @DisplayName("Test auditTransfer success")
    void itShouldAuditTransferSuccessfully() {
        // Given
        String message = "Transfer audit message";
        EventType eventType = EventType.TRANSFER;

        // When
        auditService.audit(eventType, message);

        // Then
        verify(auditRepository).save(
                argThat(audit -> audit.getEventType() == eventType && audit.getMessage().equals(message))
        );
    }

    @Test
    @DisplayName("Test auditDeposit success")
    void itShouldAuditDepositSuccessfully() {
        // Given
        String message = "Deposit audit message";
        EventType eventType = EventType.DEPOSIT;

        // When
        auditService.audit(eventType, message);

        // Then
        verify(auditRepository).save(
                argThat(audit -> audit.getEventType() == eventType && audit.getMessage().equals(message))
        );
    }
}
