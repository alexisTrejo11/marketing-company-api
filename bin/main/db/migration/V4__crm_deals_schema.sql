-- ===========================================
-- DEALS TABLE (AGREEMENTS/CONTRACTS)
-- ===========================================
CREATE TABLE IF NOT EXISTS deals (
    id BIGSERIAL PRIMARY KEY,
    deal_status VARCHAR(50) NOT NULL,
    final_amount NUMERIC(15,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    campaign_manager_id BIGINT,
    deliverables TEXT,
    terms TEXT,
    customer_id BIGINT NOT NULL,
    opportunity_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_deals_campaign_manager
        FOREIGN KEY (campaign_manager_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_deals_customer
        FOREIGN KEY (customer_id)
        REFERENCES customer_companies(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_deals_opportunity
        FOREIGN KEY (opportunity_id)
        REFERENCES opportunities(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_deal_status_valid
        CHECK (deal_status IN ('DRAFT', 'IN_NEGOTIATION', 'SIGNED', 'PAID', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),

    CONSTRAINT ck_final_amount_valid
        CHECK (final_amount > 0),

    CONSTRAINT ck_deal_dates_valid
        CHECK (start_date <= COALESCE(end_date, start_date)),

    CONSTRAINT ck_end_date_after_start
        CHECK (end_date IS NULL OR end_date >= start_date),

    CONSTRAINT uq_deal_opportunity UNIQUE (opportunity_id)
);

-- ===========================================
-- DEAL-SERVICE PACKAGES RELATION TABLE (ManyToMany)
-- ===========================================
CREATE TABLE IF NOT EXISTS deal_service_packages (
    deal_id BIGINT NOT NULL,
    service_package_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER DEFAULT 1,
    unit_price NUMERIC(10,2),

    CONSTRAINT pk_deal_service_packages PRIMARY KEY (deal_id, service_package_id),

    CONSTRAINT fk_deal_service_packages_deal
        FOREIGN KEY (deal_id)
        REFERENCES deals(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_deal_service_packages_service
        FOREIGN KEY (service_package_id)
        REFERENCES services_packages(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_quantity_valid
        CHECK (quantity > 0),

    CONSTRAINT ck_unit_price_valid
        CHECK (unit_price IS NULL OR unit_price >= 0)
);