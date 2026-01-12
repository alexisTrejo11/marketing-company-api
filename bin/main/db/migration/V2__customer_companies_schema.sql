-- ===========================================
-- Customer Companies Table Migration
-- ===========================================
CREATE TABLE IF NOT EXISTS customer_companies (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    legal_name VARCHAR(255),
    tax_id VARCHAR(50),
    website VARCHAR(500),
    founding_year INTEGER,
    industry_code VARCHAR(20) NOT NULL,
    industry_name VARCHAR(255),
    sector VARCHAR(100),
    company_size VARCHAR(20),
    employee_count INTEGER,
    annual_revenue_amount NUMERIC(15,2),
    annual_revenue_currency CHAR(3),
    revenue_range VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    is_public_company BOOLEAN DEFAULT FALSE,
    is_startup BOOLEAN DEFAULT FALSE,
    target_market VARCHAR(1000),
    mission_statement VARCHAR(2000),

    -- Audit Fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    -- Constraints for unique fields
    CONSTRAINT uq_customer_companies_tax_id UNIQUE (tax_id),

    -- Constraints for validation
    CONSTRAINT ck_company_size_valid
        CHECK (company_size IS NULL OR
               company_size IN ('MICRO', 'SMALL', 'MEDIUM', 'LARGE', 'ENTERPRISE')),

    CONSTRAINT ck_status_valid
        CHECK (status IN ('LEAD', 'PROSPECT', 'ACTIVE', 'INACTIVE', 'CHURNED', 'SUSPENDED')),

    CONSTRAINT ck_founding_year_valid
        CHECK (founding_year IS NULL OR
               (founding_year >= 1500 AND founding_year <= EXTRACT(YEAR FROM CURRENT_DATE))),

    CONSTRAINT ck_employee_count_valid
        CHECK (employee_count IS NULL OR employee_count >= 0),

    CONSTRAINT ck_annual_revenue_valid
        CHECK (annual_revenue_amount IS NULL OR annual_revenue_amount >= 0),

    CONSTRAINT ck_currency_valid
        CHECK (annual_revenue_currency IS NULL OR
               annual_revenue_currency ~ '^[A-Z]{3}$'),

    CONSTRAINT ck_revenue_range_valid
        CHECK (revenue_range IS NULL OR
               revenue_range IN ('UNDER_100K', 'BETWEEN_100K_1M', 'BETWEEN_1M_10M', 'BETWEEN_10M_100M', 'OVER_100M', 'UNKNOWN')),

    CONSTRAINT ck_website_valid
        CHECK (website IS NULL OR website ~ '^https?://[^\s/$.?#].[^\s]*$'),

    CONSTRAINT ck_tax_id_format
        CHECK (tax_id IS NULL OR LENGTH(tax_id) >= 5)

    -- Constraints referencing deleted columns (contract_dates, monthly_fee, billing_email, payment_method) have been removed.
);

-- ===========================================
-- Contact Persons Table
-- ===========================================
CREATE TABLE IF NOT EXISTS contact_persons (
    id BIGSERIAL PRIMARY KEY,
    -- Changed from VARCHAR(36) to BIGINT to match customer_companies.id
    company_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    position VARCHAR(100),
    department VARCHAR(100),
    is_decision_maker BOOLEAN DEFAULT FALSE,
    is_primary_contact BOOLEAN DEFAULT FALSE,
    notes VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT fk_contact_persons_company
        FOREIGN KEY (company_id)
        REFERENCES customer_companies(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_contact_names_length
        CHECK (LENGTH(first_name) >= 2 AND LENGTH(last_name) >= 2),

    CONSTRAINT ck_contact_email_format
        CHECK (email IS NULL OR
               email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),

    CONSTRAINT ck_contact_phone_format
        CHECK (phone IS NULL OR phone ~ '^\+?[0-9\s\-\(\)]{7,20}$'),

    CONSTRAINT ck_contact_department_valid
        CHECK (department IS NULL OR
               department IN ('SALES', 'MARKETING', 'IT', 'FINANCE', 'HR', 'OPERATIONS', 'EXECUTIVE', 'OTHER'))

);

-- ===========================================
-- TABLE FOR KEY PRODUCTS (ElementCollection)
-- ===========================================
CREATE TABLE IF NOT EXISTS company_key_products (
    -- Changed from VARCHAR(36) to BIGINT to match customer_companies.id
    company_id BIGINT NOT NULL,
    product VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_company_key_products PRIMARY KEY (company_id, product),
    CONSTRAINT fk_company_key_products_company
        FOREIGN KEY (company_id)
        REFERENCES customer_companies(id)
        ON DELETE CASCADE
);

-- ===========================================
-- TABLE FOR COMPETITORS (ElementCollection)
-- ===========================================
CREATE TABLE IF NOT EXISTS company_competitors (
    -- Changed from VARCHAR(36) to BIGINT to match customer_companies.id
    company_id BIGINT NOT NULL,
    competitor_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_company_competitors PRIMARY KEY (company_id, competitor_url),
    CONSTRAINT fk_company_competitors_company
        FOREIGN KEY (company_id)
        REFERENCES customer_companies(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_competitor_url_format
        CHECK (competitor_url ~ '^https?://[^\s/$.?#].[^\s]*$')
);


-- ===========================================
-- INDEXES FOR CUSTOMER_COMPANIES
-- ===========================================

-- INDEX FOR SEARCH FIELDS
CREATE INDEX IF NOT EXISTS idx_companies_name_search
    ON customer_companies USING gin (to_tsvector('english', company_name));

CREATE INDEX IF NOT EXISTS idx_companies_legal_name
    ON customer_companies(legal_name)
    WHERE legal_name IS NOT NULL;

-- INDEX FOR STATUS AND DATES
CREATE INDEX IF NOT EXISTS idx_companies_status_date
    ON customer_companies(status, created_at DESC)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_companies_industry_sector
    ON customer_companies(industry_code, sector);

CREATE INDEX IF NOT EXISTS idx_companies_size_revenue
    ON customer_companies(company_size, revenue_range);

CREATE INDEX IF NOT EXISTS idx_companies_employee_range
    ON customer_companies(employee_count)
    WHERE employee_count IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_companies_revenue_amount
    ON customer_companies(annual_revenue_amount DESC)
    WHERE annual_revenue_amount IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_companies_founding_year
    ON customer_companies(founding_year DESC)
    WHERE founding_year IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_companies_public_startup
    ON customer_companies(is_public_company, is_startup);

-- Indexes referencing deleted contract or active status columns have been removed.

-- INDEX FOR AUDIT FIELDS
CREATE INDEX IF NOT EXISTS idx_companies_created_date
    ON customer_companies(DATE(created_at));

CREATE INDEX IF NOT EXISTS idx_companies_updated_date
    ON customer_companies(DATE(updated_at));

CREATE INDEX IF NOT EXISTS idx_companies_deleted
    ON customer_companies(deleted_at)
    WHERE deleted_at IS NOT NULL;

-- Index for searching multiple text fields
CREATE INDEX IF NOT EXISTS idx_companies_fulltext_search
    ON customer_companies USING GIN (
        to_tsvector('english',
            coalesce(company_name, '') || ' ' ||
            coalesce(legal_name, '') || ' ' ||
            coalesce(industry_name, '') || ' ' ||
            coalesce(sector, '') || ' ' ||
            coalesce(target_market, '')
        )
    );

-- Functional Index for Email Domain Search (REMOVED: Billing Email deleted)

-- ===========================================
-- INDEXES FOR CONTACT_PERSONS
-- ===========================================

-- Main Indexes
CREATE INDEX IF NOT EXISTS idx_contacts_company
    ON contact_persons(company_id);

CREATE INDEX IF NOT EXISTS idx_contacts_email
    ON contact_persons(email)
    WHERE email IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_contacts_name
    ON contact_persons(LOWER(first_name || ' ' || last_name));

CREATE INDEX IF NOT EXISTS idx_contacts_decision_maker
    ON contact_persons(company_id)
    WHERE is_decision_maker = TRUE;

CREATE INDEX IF NOT EXISTS idx_contacts_primary_contact
    ON contact_persons(company_id)
    WHERE is_primary_contact = TRUE;

CREATE INDEX IF NOT EXISTS idx_contacts_position_dept
    ON contact_persons(position, department)
    WHERE position IS NOT NULL;

-- Index for combined search
CREATE INDEX IF NOT EXISTS idx_contacts_full_search
    ON contact_persons USING GIN (
        to_tsvector('english',
            coalesce(first_name, '') || ' ' ||
            coalesce(last_name, '') || ' ' ||
            coalesce(position, '') || ' ' ||
            coalesce(department, '')
        )
    );

-- ===========================================
-- INDEXES FOR KEY PRODUCTS AND COMPETITORS
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_key_products_company
    ON company_key_products(company_id);

CREATE INDEX IF NOT EXISTS idx_competitors_company
    ON company_competitors(company_id);

-- Index for full-text search on key products
CREATE INDEX IF NOT EXISTS idx_key_products_search
    ON company_key_products USING gin (to_tsvector('english', product));
