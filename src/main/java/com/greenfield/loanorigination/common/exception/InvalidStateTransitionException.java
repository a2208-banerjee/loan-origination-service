package com.greenfield.loanorigination.common.exception;

import com.greenfield.loanorigination.domain.entity.LoanStatus;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(LoanStatus from, LoanStatus to) {
        super("Cannot transition loan from %s to %s".formatted(from, to));
    }
}
