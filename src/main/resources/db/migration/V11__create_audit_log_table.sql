CREATE TABLE audit_log (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           entity_name VARCHAR(100) NOT NULL,
                           entity_id UUID NOT NULL,
                           action VARCHAR(20) NOT NULL,
                           changed_by VARCHAR(255) NOT NULL,
                           old_value TEXT,
                           new_value TEXT,
                           timestamp TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_audit_entity ON audit_log(entity_name, entity_id);