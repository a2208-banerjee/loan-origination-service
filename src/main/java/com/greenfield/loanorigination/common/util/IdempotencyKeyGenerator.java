package com.greenfield.loanorigination.common.util;

import java.util.UUID;

public final class IdempotencyKeyGenerator {
    private IdempotencyKeyGenerator() {}
public static String generateKey(){
    return UUID.randomUUID().toString();
}
}
