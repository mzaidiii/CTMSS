ALTER TABLE adverse_events ADD COLUMN suspected_drug VARCHAR(255);
ALTER TABLE adverse_events ADD COLUMN reporting_tier VARCHAR(20);
ALTER TABLE adverse_events ADD COLUMN meddra_code_stub VARCHAR(50);
ALTER TABLE adverse_events ADD COLUMN who_drug_code_stub VARCHAR(50);
ALTER TABLE adverse_events ADD COLUMN causality_status VARCHAR(50) NOT NULL DEFAULT 'PENDING_REVIEW';
ALTER TABLE adverse_events ADD COLUMN causality_assessed_date DATE;