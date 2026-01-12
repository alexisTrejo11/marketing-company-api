-- ===========================================
-- QUOTES TABLE
-- ===========================================
CREATE TABLE IF NOT EXISTS quotes (
    id BIGSERIAL PRIMARY KEY,
    customer_company_id BIGINT NOT NULL,
    opportunity_id BIGINT,
    valid_until DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_quotes_customer_company
        FOREIGN KEY (customer_company_id)
        REFERENCES customer_companies(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_quotes_opportunity
        FOREIGN KEY (opportunity_id)
        REFERENCES opportunities(id)
        ON DELETE SET NULL,

    CONSTRAINT ck_quote_status_valid
        CHECK (status IN ('DRAFT', 'SENT', 'ACCEPTED', 'REJECTED', 'EXPIRED'))

);

-- ===========================================
-- QUOTE ITEMS TABLE
-- ===========================================
CREATE TABLE IF NOT EXISTS quote_items (
    id BIGSERIAL PRIMARY KEY,
    quote_id BIGINT NOT NULL,
    service_package_id BIGINT NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    total NUMERIC(10,2) NOT NULL,
    discount_percentage NUMERIC(5,2),
    discount NUMERIC(10,2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_quote_items_quote
        FOREIGN KEY (quote_id)
        REFERENCES quotes(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_quote_items_service_package
        FOREIGN KEY (service_package_id)
        REFERENCES services_packages(id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_unit_price_valid
        CHECK (unit_price >= 0),

    CONSTRAINT ck_total_valid
        CHECK (total >= 0),

    CONSTRAINT ck_discount_percentage_valid
        CHECK (discount_percentage IS NULL OR
               (discount_percentage >= 0 AND discount_percentage <= 100)),

    CONSTRAINT ck_item_discount_valid
        CHECK (discount IS NULL OR discount >= 0)
);
