package dev.nano.bank.domain;

import lombok.Getter;
import lombok.Setter;
import dev.nano.bank.domain.enumration.EventType;

import javax.persistence.*;

@Entity
@Table(name = "audits")
@Getter @Setter
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(length = 100)
    private String message;

    @Enumerated(EnumType.STRING)
    private EventType eventType;
}
