package com.greenfield.loanorigination.api.dto.LoanApplication.request;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record LoanApplicationRequest(
        @NotBlank
        String applicantIdHash,
        @NotBlank
        String fullName,
        @NotBlank @Email
        String email,
        @NotNull @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal annualIncome,
        @NotNull @DecimalMin(value = "500.0", inclusive = false)
        BigDecimal requestedAmount,
        @Min(6) @Max(36)
        int termMonths
) {
}
