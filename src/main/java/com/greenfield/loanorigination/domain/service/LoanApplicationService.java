package com.greenfield.loanorigination.domain.service;

import com.greenfield.loanorigination.api.dto.LoanApplication.request.LoanApplicationRequest;
import com.greenfield.loanorigination.api.dto.LoanApplication.response.LoanApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface LoanApplicationService {

    LoanApplicationResponse submitLoanApplication(LoanApplicationRequest loanApplicationRequest, String idempotencyKey);

    LoanApplicationResponse getLoanApplicationById(UUID loanApplicationId);

    Page<LoanApplicationResponse> getLoanApplicationByApplicantId(String applicantIdHash, Pageable pageable);

    Slice<LoanApplicationResponse> getLoanApplicationByApplicantIdPageLink(String status, Pageable pageable);
}
