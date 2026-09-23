package com.greenfield.loanorigination.domain.statemachine;

import com.greenfield.loanorigination.common.exception.InvalidStateTransitionException;
import com.greenfield.loanorigination.domain.entity.LoanStatus;
import org.springframework.stereotype.Component;

@Component
public class LoanStatusTransitionValidator {
    public boolean isValid(LoanStatus to, LoanStatus from) {
        if(LoanStatusTransition.isAllowed(to, from)) {
            return true;
        }else{
            throw new InvalidStateTransitionException(from, to);
        }
    }
}
