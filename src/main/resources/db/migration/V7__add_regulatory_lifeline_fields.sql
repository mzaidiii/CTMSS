ALTER TABLE trails ADD COLUMN iec_approval_date DATE;
ALTER TABLE trails ADD COLUMN ctri_registration_number VARCHAR(255);
ALTER TABLE trails ADD COLUMN ctri_registration_date DATE;
ALTER TABLE trails ADD COLUMN regulatory_stage VARCHAR(50) NOT NULL DEFAULT 'DRAFT';