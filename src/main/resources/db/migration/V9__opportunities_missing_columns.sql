-- ===========================================
-- Align opportunities table with OpportunityEntity
-- ===========================================
ALTER TABLE opportunities
    ADD COLUMN IF NOT EXISTS loss_reason_value VARCHAR(100),
    ADD COLUMN IF NOT EXISTS loss_reason_details VARCHAR(500),
    ADD COLUMN IF NOT EXISTS next_steps VARCHAR(1000),
    ADD COLUMN IF NOT EXISTS next_steps_due_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS probability INTEGER,
    ADD COLUMN IF NOT EXISTS is_overdue BOOLEAN NOT NULL DEFAULT FALSE;

-- Backfill overdue flag for existing open opportunities
UPDATE opportunities
SET is_overdue = CASE
    WHEN stage IN ('CLOSED_WON', 'CLOSED_LOST') THEN FALSE
    WHEN expected_close_date IS NOT NULL AND expected_close_date < CURRENT_DATE THEN TRUE
    ELSE FALSE
END;
