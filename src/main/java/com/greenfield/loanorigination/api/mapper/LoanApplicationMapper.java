package com.greenfield.loanorigination.api.mapper;

import com.greenfield.loanorigination.api.dto.LoanApplication.request.LoanApplicationRequest;
import com.greenfield.loanorigination.api.dto.LoanApplication.response.LoanApplicationResponse;
import com.greenfield.loanorigination.domain.entity.Applicant;
import com.greenfield.loanorigination.domain.entity.LoanApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "idempotencyKey", ignore = true)
    @Mapping(target = "decisionReason", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "decidedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "applicant", source = ".", qualifiedByName = "applicantRequestToEntity")
    LoanApplication toEntity(LoanApplicationRequest loanApplicationRequest);

    @Named("applicantRequestToEntity")
    default Applicant applicantRequestToEntity(LoanApplicationRequest loanApplicationRequest) {
        Applicant applicant = new Applicant();
        applicant.setApplicantIdHash(loanApplicationRequest.applicantIdHash());
        applicant.setEmail(loanApplicationRequest.email());
        applicant.setAnnualIncome(loanApplicationRequest.annualIncome());
        applicant.setFullName(loanApplicationRequest.fullName());
        return applicant;
    }

    @Mapping(target = "fullName", source = "applicant.fullName")
    LoanApplicationResponse toResponse(LoanApplication loanApplication);
}
