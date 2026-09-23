package com.greenfield.loanorigination.repository;

import com.greenfield.loanorigination.domain.entity.LoanApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID>{

        Optional<LoanApplication> findByIdempotencyKey(String idempotencyKey);

        Page<LoanApplication> findByApplicant_ApplicantIdHash(String applicantIdHash, Pageable pageable);


}
