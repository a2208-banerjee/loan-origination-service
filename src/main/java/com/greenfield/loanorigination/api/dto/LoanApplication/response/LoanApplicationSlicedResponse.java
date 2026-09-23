package com.greenfield.loanorigination.api.dto.LoanApplication.response;

import java.util.List;

public record LoanApplicationSlicedResponse<T>(List<T> content,
                                               int page,
                                               int size,
                                               boolean hasNext,
                                               String nextLink) {
}
