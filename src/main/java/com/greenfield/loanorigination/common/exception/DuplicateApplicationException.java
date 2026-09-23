package com.greenfield.loanorigination.common.exception;

import java.util.UUID;

public class DuplicateApplicationException extends RuntimeException {
    public DuplicateApplicationException(String idempotencyKey, UUID applicationId) {
        super("Duplicate submission of loan application for idempotency key %s and application id %s"
                .formatted(idempotencyKey, applicationId));
    }
}
