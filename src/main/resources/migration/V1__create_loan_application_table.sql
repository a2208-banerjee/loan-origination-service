CREATE TABLE loan_application (
    id                  UUID PRIMARY KEY,
    applicant_id_hash   VARCHAR(128) NOT NULL,
    full_name           VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    annual_income       NUMERIC(14, 2),
    requested_amount    NUMERIC(14, 2) NOT NULL,
    term_months         INT NOT NULL,
    status              VARCHAR(32) NOT NULL,
    idempotency_key     VARCHAR(128) NOT NULL,
    decision_reason     VARCHAR(500),
    submitted_at        TIMESTAMPTZ NOT NULL,
    decided_at          TIMESTAMPTZ,
    last_modified_by    VARCHAR(255),
    version             BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uq_loan_idempotency_key UNIQUE (idempotency_key)
);

-- applicant lookups (e.g. "show me all of this customer's applications") are a common read path
CREATE INDEX idx_loan_application_applicant_id_hash ON loan_application (applicant_id_hash);

-- status is filtered on constantly by ops/underwriting dashboards
CREATE INDEX idx_loan_application_status ON loan_application (status);
