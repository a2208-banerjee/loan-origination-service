package com.greenfield.loanorigination.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "loan_application")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private Applicant applicant;

    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "term_months", nullable = false)
    private int termMonths;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "decision_reason")
    private String decisionReason;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Column(name = "decided_at")
    private Instant decidedAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Version // To solve the OptimisticLockingFailureException issue or LostUpdate issue, we can add a version field to the entity and annotate it with @Version. This will allow JPA to handle optimistic locking for us.
    @Column(name = "version")
    private long version;
}
