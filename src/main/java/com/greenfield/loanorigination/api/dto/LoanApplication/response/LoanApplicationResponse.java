package com.greenfield.loanorigination.api.dto.LoanApplication.response;

import com.greenfield.loanorigination.domain.entity.LoanStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LoanApplicationResponse(
        UUID id,
        String fullName,
        BigDecimal requestedAmount,
        int termMonths,
        LoanStatus status,
        String decisionReason,
        Instant submittedAt,
        Instant decidedAt
) {
}
