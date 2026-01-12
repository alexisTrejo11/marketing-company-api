-- ===========================================
-- OPPORTUNITIES TABLE
-- ===========================================
CREATE TABLE IF NOT EXISTS opportunities (
    id BIGSERIAL PRIMARY KEY,
    customer_company_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    amount NUMERIC(15,2),
    stage VARCHAR(50) NOT NULL,
    expected_close_date DATE,
    loss_reason_value VARCHAR(100),
    loss_reason_details VARCHAR(500),
    next_steps VARCHAR(1000),
    next_steps_due_date TIMESTAMP,
    probability INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    -- Constraints
    CONSTRAINT fk_opportunities_customer_company
        FOREIGN KEY (customer_company_id)
        REFERENCES customer_companies(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_opportunity_amount_valid
        CHECK (amount IS NULL OR amount >= 0),

    CONSTRAINT ck_opportunity_probability_valid
        CHECK (probability IS NULL OR (probability >= 0 AND probability <= 100)),

    CONSTRAINT ck_opportunity_stage_valid
        CHECK (stage IN ('PROSPECTING', 'QUALIFICATION', 'PROPOSAL', 'NEGOTIATION', 'CLOSED_WON', 'CLOSED_LOST'))
);

-- ===========================================
-- SERVICES / SERVICE PACKAGES TABLE
-- ===========================================
CREATE TABLE IF NOT EXISTS services_packages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    service_type VARCHAR(50) NOT NULL,
    deliverables TEXT,
    estimated_hours INTEGER NOT NULL,
    complexity VARCHAR(20) NOT NULL,
    is_recurring BOOLEAN NOT NULL DEFAULT FALSE,
    frequency VARCHAR(50),
    project_duration INTEGER,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    -- Constraints
    CONSTRAINT ck_service_price_valid
        CHECK (price >= 0),

    CONSTRAINT ck_estimated_hours_valid
        CHECK (estimated_hours > 0),

    CONSTRAINT ck_complexity_valid
        CHECK (complexity IN ('LOW', 'MEDIUM', 'HIGH', 'VERY_HIGH')),

    CONSTRAINT ck_service_type_valid
        CHECK (service_type IN ('SOCIAL_MEDIA', 'SEO', 'CONTENT_MARKETING', 'PPC', 'EMAIL_MARKETING', 'ANALYTICS', 'STRATEGY')),

    CONSTRAINT ck_frequency_valid
        CHECK (frequency IS NULL OR
               frequency IN ('WEEKLY', 'BIWEEKLY', 'MONTHLY', 'QUARTERLY', 'YEARLY', 'ONE_TIME')),

    CONSTRAINT ck_project_duration_valid
        CHECK (project_duration IS NULL OR project_duration > 0),

    CONSTRAINT ck_recurring_frequency_required
        CHECK (is_recurring = FALSE OR frequency IS NOT NULL)
);

-- ===========================================
-- MARKETING SERVICE KPIS TABLE (ElementCollection)
-- ===========================================
CREATE TABLE IF NOT EXISTS marketing_service_kpis (
    -- Changed from VARCHAR(36) to BIGINT to reference services_packages.id (BIGSERIAL)
    service_id BIGINT NOT NULL,
    kpi VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_marketing_service_kpis PRIMARY KEY (service_id, kpi),
    CONSTRAINT fk_marketing_service_kpis_service
        FOREIGN KEY (service_id)
        REFERENCES services_packages(id)
        ON DELETE CASCADE
);

-- ===========================================
-- MARKETING SERVICE PLATFORMS TABLE (ElementCollection)
-- ===========================================
CREATE TABLE IF NOT EXISTS marketing_service_platforms (
    -- Changed from VARCHAR(36) to BIGINT to reference services_packages.id (BIGSERIAL)
    service_id BIGINT NOT NULL,
    platform VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_marketing_service_platforms PRIMARY KEY (service_id, platform),
    CONSTRAINT fk_marketing_service_platforms_service
        FOREIGN KEY (service_id)
        REFERENCES services_packages(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_platform_valid
        CHECK (platform IN ('FACEBOOK', 'INSTAGRAM', 'TWITTER', 'LINKEDIN', 'TIKTOK', 'YOUTUBE', 'PINTEREST'))
);
