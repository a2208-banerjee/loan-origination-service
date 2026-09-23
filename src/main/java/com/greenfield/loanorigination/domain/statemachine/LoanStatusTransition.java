package com.greenfield.loanorigination.domain.statemachine;

import com.greenfield.loanorigination.domain.entity.LoanStatus;

import java.util.Map;
import java.util.Set;

public final class LoanStatusTransition {

    public final static Map<LoanStatus, Set<LoanStatus>> ALLOWED_LOAN_STATUS = Map.of(
            LoanStatus.SUBMITTED, Set.of(LoanStatus.CREDIT_CHECK),
            LoanStatus.CREDIT_CHECK, Set.of(LoanStatus.APPROVED, LoanStatus.REJECTED, LoanStatus.MANUAL_REVIEW),
            LoanStatus.MANUAL_REVIEW, Set.of(LoanStatus.APPROVED, LoanStatus.REJECTED),
            LoanStatus.APPROVED, Set.of(LoanStatus.DISBURSED),
            LoanStatus.REJECTED, Set.of(),
            LoanStatus.DISBURSED, Set.of()
    );
    private LoanStatusTransition() {}

    public static boolean isAllowed(LoanStatus from, LoanStatus to){
        return ALLOWED_LOAN_STATUS.getOrDefault(from, Set.of()).contains(to);
    }
}
