-- ============================================================================
-- CRM OPPORTUNITIES FIELD UPDATES
-- ============================================================================
-- Purpose: Update and enhance opportunities table fields
-- Dependencies: V3 (opportunities table must exist)
-- ============================================================================

-- Add new columns if they don't exist
ALTER TABLE opportunities
ADD COLUMN IF NOT EXISTS expected_close_quarter VARCHAR(10),
ADD COLUMN IF NOT EXISTS lead_source VARCHAR(100),
ADD COLUMN IF NOT EXISTS competitor_analysis TEXT;

-- Add index for expected_close_quarter for quarterly reporting
CREATE INDEX IF NOT EXISTS idx_opportunities_close_quarter
ON opportunities(expected_close_quarter)
WHERE expected_close_quarter IS NOT NULL;

-- Add index for lead_source for marketing attribution
CREATE INDEX IF NOT EXISTS idx_opportunities_lead_source
ON opportunities(lead_source)
WHERE lead_source IS NOT NULL;

-- Update existing opportunities to set expected_close_quarter based on expected_close_date
UPDATE opportunities
SET expected_close_quarter = CONCAT(
    EXTRACT(YEAR FROM expected_close_date),
    '-Q',
    EXTRACT(QUARTER FROM expected_close_date)
)
WHERE expected_close_date IS NOT NULL
AND expected_close_quarter IS NULL;

-- Add comments to new columns
COMMENT ON COLUMN opportunities.expected_close_quarter IS 'Expected close quarter in format YYYY-Q# (e.g., 2026-Q1)';
COMMENT ON COLUMN opportunities.lead_source IS 'Source of the lead (e.g., Website, Referral, Marketing Campaign)';
COMMENT ON COLUMN opportunities.competitor_analysis IS 'Notes about competing vendors and comparison';
