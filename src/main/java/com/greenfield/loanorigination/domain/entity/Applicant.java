package com.greenfield.loanorigination.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class Applicant {

    @Column(name = "applicant_id_hash", nullable = false)
    private String applicantIdHash;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "annual_income")
    private BigDecimal annualIncome;
}
