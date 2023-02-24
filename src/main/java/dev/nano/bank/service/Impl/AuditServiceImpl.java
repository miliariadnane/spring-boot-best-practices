package dev.nano.bank.service.Impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.nano.bank.domain.Audit;
import dev.nano.bank.domain.enumration.EventType;
import dev.nano.bank.repository.AuditRepository;
import dev.nano.bank.service.AuditService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class AuditServiceImpl implements AuditService {
    private AuditRepository auditRepository;

    @Override
    public void audit(EventType eventType, String message) {
        log.info("Audit of the event {}", EventType.TRANSFER);
        Audit audit = new Audit();
        audit.setEventType(eventType);
        audit.setMessage(message);
        auditRepository.save(audit);
    }
}
