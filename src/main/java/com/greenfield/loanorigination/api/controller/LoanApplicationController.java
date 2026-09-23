package com.greenfield.loanorigination.api.controller;

import com.greenfield.loanorigination.api.dto.LoanApplication.request.LoanApplicationRequest;
import com.greenfield.loanorigination.api.dto.LoanApplication.response.LoanApplicationResponse;
import com.greenfield.loanorigination.api.dto.LoanApplication.response.LoanApplicationSlicedResponse;
import com.greenfield.loanorigination.common.util.IdempotencyKeyGenerator;
import com.greenfield.loanorigination.domain.service.LoanApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan-applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping("/submit")
    public ResponseEntity<LoanApplicationResponse> submitLoanApplication(@RequestHeader(value = "idempotency-key", required = true) String idempotencyKey,
                                                                         @RequestBody @Valid LoanApplicationRequest loanApplicationRequest) {

        String key = idempotencyKey != null ? idempotencyKey : IdempotencyKeyGenerator.generateKey();
        return new ResponseEntity<>(loanApplicationService.submitLoanApplication(loanApplicationRequest, key), HttpStatus.CREATED);
    }
    /** Fetches a single application by id. */
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponse> getById(@PathVariable("id") @Valid UUID loanApplicationId) {
        return new ResponseEntity<>(loanApplicationService.getLoanApplicationById(loanApplicationId), HttpStatus.OK);
    }

    /** Lists a given applicant's applications, paginated. */
    @GetMapping("/page")
    public ResponseEntity<Page<LoanApplicationResponse>> findByApplicant(
            @RequestParam String applicantIdHash, @PageableDefault(size = 10, value = 10, sort = "submittedAt", direction = Sort.Direction.ASC) Pageable pageable){

        Pageable fixedSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("submittedAt").ascending());
        return new ResponseEntity<>(loanApplicationService.getLoanApplicationByApplicantId(applicantIdHash, pageable), HttpStatus.OK);
    }

    /** Lists a given applicant's applications, nextLink. */
    @GetMapping("/page-nextlink")
    public ResponseEntity<LoanApplicationSlicedResponse<LoanApplicationResponse>> findByApplicantNextLink(
            @RequestParam String applicantIdHash, Pageable pageable){
        Slice<LoanApplicationResponse> slice = loanApplicationService.getLoanApplicationByApplicantIdPageLink(applicantIdHash, pageable);
        List<LoanApplicationResponse> applicationData = slice.getContent();

        String nextLink = null;
        if(slice.hasNext()){
            nextLink = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("page", pageable.getPageNumber() + 1)
                    .replaceQueryParam("size", pageable.getPageSize())
                    .toUriString();
        }
        LoanApplicationSlicedResponse<LoanApplicationResponse> response = new LoanApplicationSlicedResponse<>(applicationData,slice.getNumber(), slice.getSize(), slice.hasNext(),nextLink);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /** Get CSRF Token Example **/
    @PostMapping("/update-loan")
    public ResponseEntity<HttpStatus> updateLoan() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /** Get CSRF Token Example **/
    @GetMapping("/get-csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest httpServletRequest) {

        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }
}
