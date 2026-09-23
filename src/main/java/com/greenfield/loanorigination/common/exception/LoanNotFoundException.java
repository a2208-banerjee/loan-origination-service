package com.greenfield.loanorigination.common.exception;

import java.util.UUID;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(UUID loanApplicationId) {
        super("Loan Not Found for Loan Id %s ".formatted(loanApplicationId));
    }
}
