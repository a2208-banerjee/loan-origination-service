package com.greenfield.loanorigination.domain.service.impl;

import com.greenfield.loanorigination.api.dto.LoanApplication.request.LoanApplicationRequest;
import com.greenfield.loanorigination.api.dto.LoanApplication.response.LoanApplicationResponse;
import com.greenfield.loanorigination.api.mapper.LoanApplicationMapper;
import com.greenfield.loanorigination.common.exception.DuplicateApplicationException;
import com.greenfield.loanorigination.common.exception.LoanNotFoundException;
import com.greenfield.loanorigination.domain.entity.LoanApplication;
import com.greenfield.loanorigination.domain.entity.LoanStatus;
import com.greenfield.loanorigination.domain.service.LoanApplicationService;
import com.greenfield.loanorigination.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {


    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationMapper loanApplicationMapper;

    @Override
    @Transactional

    public LoanApplicationResponse submitLoanApplication(LoanApplicationRequest loanApplicationRequest, String idempotencyKey) {
        loanApplicationRepository.findByIdempotencyKey(idempotencyKey)
                .ifPresent(existingLoanApplication -> {
                   throw new DuplicateApplicationException(idempotencyKey, existingLoanApplication.getId());
                });

        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationRequest);
        loanApplication.setIdempotencyKey(idempotencyKey);
        //loanApplication.setId(UUID.randomUUID());
        loanApplication.setStatus(LoanStatus.SUBMITTED);
        loanApplication.setSubmittedAt(Instant.now());

        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);

        return loanApplicationMapper.toResponse(savedApplication);
    }

    @Override
    public LoanApplicationResponse getLoanApplicationById(UUID loanApplicationId) {
       return loanApplicationRepository.findById(loanApplicationId)
                .map(loanApplicationMapper::toResponse)
                .orElseThrow(() ->  new LoanNotFoundException(loanApplicationId));

    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanApplicationResponse> getLoanApplicationByApplicantId(String applicantIdHash, Pageable pageable) {

        return loanApplicationRepository.findByApplicant_ApplicantIdHash(applicantIdHash, pageable)
                .map(loanApplicationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<LoanApplicationResponse> getLoanApplicationByApplicantIdPageLink(String status, Pageable pageable) {
        return loanApplicationRepository.findByApplicant_ApplicantIdHash(status, pageable)
                .map(loanApplicationMapper::toResponse);

    }
}
