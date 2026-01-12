-- ===========================================
-- OPPORTUNITIES INDEXES
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_opportunities_customer
    ON opportunities(customer_company_id);

CREATE INDEX IF NOT EXISTS idx_opportunities_stage
    ON opportunities(stage, expected_close_date);

CREATE INDEX IF NOT EXISTS idx_opportunities_amount
    ON opportunities(amount DESC) WHERE amount IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_opportunities_close_date
    ON opportunities(expected_close_date) WHERE expected_close_date IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_opportunities_active
    ON opportunities(stage)
    WHERE stage NOT IN ('CLOSED_WON', 'CLOSED_LOST')
    AND deleted_at IS NULL;

-- ===========================================
-- SERVICES_PACKAGES INDEXES
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_services_packages_type
    ON services_packages(service_type, active);

CREATE INDEX IF NOT EXISTS idx_services_packages_price
    ON services_packages(price) WHERE active = TRUE;

CREATE INDEX IF NOT EXISTS idx_services_packages_complexity
    ON services_packages(complexity, estimated_hours);

CREATE INDEX IF NOT EXISTS idx_services_packages_recurring
    ON services_packages(is_recurring, frequency) WHERE active = TRUE;

CREATE INDEX IF NOT EXISTS idx_services_packages_active
    ON services_packages(active) WHERE active = TRUE;

CREATE INDEX IF NOT EXISTS idx_services_packages_fulltext
    ON services_packages USING GIN(
        to_tsvector('english', -- Changed from 'spanish'
            coalesce(name, '') || ' ' ||
            coalesce(description, '')
        )
    ) WHERE active = TRUE;

-- ===========================================
-- DEALS INDEXES
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_deals_customer
    ON deals(customer_id, deal_status);

CREATE INDEX IF NOT EXISTS idx_deals_opportunity
    ON deals(opportunity_id);

CREATE INDEX IF NOT EXISTS idx_deals_status_date
    ON deals(deal_status, start_date DESC);

CREATE INDEX IF NOT EXISTS idx_deals_campaign_manager
    ON deals(campaign_manager_id) WHERE campaign_manager_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_deals_amount
    ON deals(final_amount DESC);

-- ===========================================
-- DEAL_SERVICE_PACKAGES INDEXES
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_deal_services_deal
    ON deal_service_packages(deal_id);

CREATE INDEX IF NOT EXISTS idx_deal_services_service
    ON deal_service_packages(service_package_id);

-- ===========================================
-- QUOTES INDEXES
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_quotes_customer
    ON quotes(customer_company_id, status);

CREATE INDEX IF NOT EXISTS idx_quotes_opportunity
    ON quotes(opportunity_id) WHERE opportunity_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_quotes_status_date
    ON quotes(status, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_quotes_valid_until
    ON quotes(valid_until) WHERE status NOT IN ('ACCEPTED', 'REJECTED', 'EXPIRED');


-- REMOVED idx_quotes_expiring_soon: Condition uses non-immutable CURRENT_DATE.

-- ===========================================
-- QUOTE_ITEMS INDEXES
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_quote_items_quote
    ON quote_items(quote_id);

CREATE INDEX IF NOT EXISTS idx_quote_items_service
    ON quote_items(service_package_id);

-- ===========================================
-- TASKS INDEXES
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_tasks_assigned_to
    ON tasks(assigned_to_user_id, status)
    WHERE assigned_to_user_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_tasks_customer
    ON tasks(customer_id, due_date) WHERE customer_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_tasks_opportunity
    ON tasks(opportunity_id, priority) WHERE opportunity_id IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_tasks_status_priority
    ON tasks(status, priority, due_date);

CREATE INDEX IF NOT EXISTS idx_tasks_due_date
    ON tasks(due_date)
    WHERE status IN ('PENDING', 'IN_PROGRESS')
    AND due_date IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_tasks_fulltext
    ON tasks USING GIN(
        to_tsvector('english', -- Changed from 'spanish'
            coalesce(title, '') || ' ' ||
            coalesce(description, '')
        )
    );

-- ===========================================
-- INTERACTIONS INDEXES
-- ===========================================
CREATE INDEX IF NOT EXISTS idx_interactions_customer
    ON interactions(customer_id, date_time DESC);

CREATE INDEX IF NOT EXISTS idx_interactions_type_date
    ON interactions(type, date_time DESC);

CREATE INDEX IF NOT EXISTS idx_interactions_feedback
    ON interactions(feedback_type) WHERE feedback_type IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_interactions_channel
    ON interactions(channel_preference) WHERE channel_preference IS NOT NULL;




-- ===========================================
-- Extra Indexes for Performance Optimization
-- ===========================================
-- Index for customer revenue analysis
CREATE INDEX IF NOT EXISTS idx_customers_revenue_status
ON customer_companies(revenue_range, status, company_size)
WHERE deleted_at IS NULL;

-- Index for opportunity pipeline analysis
CREATE INDEX IF NOT EXISTS idx_opportunities_pipeline
ON opportunities(stage, expected_close_date, amount)
WHERE deleted_at IS NULL AND stage NOT IN ('CLOSED_WON', 'CLOSED_LOST');

-- Index for deal value analysis
CREATE INDEX IF NOT EXISTS idx_deals_value_analysis
ON deals(deal_status, final_amount, start_date)
WHERE deleted_at IS NULL;

-- Index for task prioritization
CREATE INDEX IF NOT EXISTS idx_tasks_team_priority
ON tasks(assigned_to_user_id, priority, due_date, status)
WHERE deleted_at IS NULL AND status IN ('PENDING', 'IN_PROGRESS');

-- Index for interaction timeline
CREATE INDEX IF NOT EXISTS idx_interactions_customer_timeline
ON interactions(customer_id, date_time DESC, type)
WHERE deleted_at IS NULL;
