-- ===========================================
-- Marketing Schema (channels, campaigns, targets, and related tables)
-- ===========================================

-- -------------------------------------------
-- Marketing Channels
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS marketing_channels (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    channel_type VARCHAR(50) NOT NULL,
    description TEXT,
    default_cost_per_click NUMERIC(10, 2),
    default_cost_per_impression NUMERIC(10, 4),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT ck_marketing_channels_type
        CHECK (channel_type IN ('PAID', 'ORGANIC', 'SOCIAL', 'DIRECT', 'EMAIL', 'REFERRAL'))
);

CREATE INDEX IF NOT EXISTS idx_channel_type ON marketing_channels (channel_type);
CREATE INDEX IF NOT EXISTS idx_channel_active ON marketing_channels (is_active);

-- -------------------------------------------
-- Marketing Campaigns
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS marketing_campaigns (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    campaign_type VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    total_budget NUMERIC(15, 2) NOT NULL,
    spent_amount NUMERIC(15, 2) DEFAULT 0,
    budget_allocations JSONB,
    start_date DATE NOT NULL,
    end_date DATE,
    target_audience_demographics JSONB,
    target_locations JSONB,
    target_interests JSONB,
    primary_goal VARCHAR(200),
    secondary_goals JSONB,
    success_metrics JSONB,
    primary_channel_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_marketing_campaigns_channel
        FOREIGN KEY (primary_channel_id) REFERENCES marketing_channels(id),

    CONSTRAINT ck_marketing_campaigns_type
        CHECK (campaign_type IN ('AWARENESS', 'CONVERSION', 'RETENTION', 'REACTIVATION', 'BRANDING')),

    CONSTRAINT ck_marketing_campaigns_status
        CHECK (status IN ('DRAFT', 'PLANNED', 'ACTIVE', 'PAUSED', 'COMPLETED', 'CANCELLED'))
);

CREATE INDEX IF NOT EXISTS idx_campaigns_status_date ON marketing_campaigns (status, start_date);
CREATE INDEX IF NOT EXISTS idx_campaigns_type_budget ON marketing_campaigns (campaign_type, total_budget);
CREATE INDEX IF NOT EXISTS idx_campaigns_channel ON marketing_campaigns (primary_channel_id);

-- -------------------------------------------
-- Campaign Targets
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS campaign_targets (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    metric_name VARCHAR(100) NOT NULL,
    metric_type VARCHAR(30) NOT NULL,
    target_value NUMERIC(15, 4) NOT NULL,
    current_value NUMERIC(15, 4) NOT NULL DEFAULT 0,
    measurement_unit VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'NOT_STARTED',
    status_change_reason VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_campaign_targets_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE,

    CONSTRAINT uk_campaign_targets_campaign_metric_name
        UNIQUE (campaign_id, metric_name),

    CONSTRAINT ck_campaign_targets_metric_type
        CHECK (metric_type IN ('COUNT', 'CURRENCY', 'PERCENTAGE', 'DURATION', 'COST', 'RATIO', 'SCORE')),

    CONSTRAINT ck_campaign_targets_status
        CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'ACHIEVED', 'FAILED')),

    CONSTRAINT ck_campaign_targets_target_value
        CHECK (target_value > 0),

    CONSTRAINT ck_campaign_targets_current_value
        CHECK (current_value >= 0)
);

CREATE INDEX IF NOT EXISTS idx_targets_campaign_status ON campaign_targets (campaign_id, status);
CREATE INDEX IF NOT EXISTS idx_targets_campaign_metric_type ON campaign_targets (campaign_id, metric_type);
CREATE INDEX IF NOT EXISTS idx_targets_deleted_at ON campaign_targets (deleted_at) WHERE deleted_at IS NULL;

-- -------------------------------------------
-- Campaign Metrics
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS campaign_metrics (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    metric_type VARCHAR(30) NOT NULL,
    description TEXT,
    current_value NUMERIC(15, 4) DEFAULT 0,
    target_value NUMERIC(15, 4),
    measurement_unit VARCHAR(50),
    calculation_formula VARCHAR(500),
    data_source VARCHAR(200),
    last_calculated_date TIMESTAMP,
    is_automated BOOLEAN DEFAULT FALSE,
    is_target_achieved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_campaign_metrics_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE,

    CONSTRAINT ck_campaign_metrics_type
        CHECK (metric_type IN ('COUNT', 'CURRENCY', 'PERCENTAGE', 'DURATION', 'COST', 'RATIO', 'SCORE'))
);

CREATE INDEX IF NOT EXISTS idx_metrics_campaign_target ON campaign_metrics (campaign_id, is_target_achieved, metric_type);

-- -------------------------------------------
-- Marketing Assets
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS marketing_assets (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    asset_type VARCHAR(50) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    url VARCHAR(500) NOT NULL,
    file_size_kb INTEGER,
    mime_type VARCHAR(100),
    views_count INTEGER DEFAULT 0,
    clicks_count INTEGER DEFAULT 0,
    conversions_count INTEGER DEFAULT 0,
    status VARCHAR(30) NOT NULL,
    is_primary_asset BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_marketing_assets_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE,

    CONSTRAINT ck_marketing_assets_type
        CHECK (asset_type IN ('LANDING_PAGE', 'AD_CREATIVE', 'EMAIL_TEMPLATE', 'WHITEPAPER', 'VIDEO', 'IMAGE', 'DOCUMENT', 'SOCIAL_POST')),

    CONSTRAINT ck_marketing_assets_status
        CHECK (status IN ('DRAFT', 'READY', 'ACTIVE', 'ARCHIVED'))
);

CREATE INDEX IF NOT EXISTS idx_assets_campaign_type ON marketing_assets (campaign_id, asset_type, status);

-- -------------------------------------------
-- Campaign Activities
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS campaign_activities (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    activity_type VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    planned_start_date TIMESTAMP NOT NULL,
    planned_end_date TIMESTAMP NOT NULL,
    actual_start_date TIMESTAMP,
    actual_end_date TIMESTAMP,
    planned_cost NUMERIC(10, 2) NOT NULL,
    actual_cost NUMERIC(10, 2),
    cost_overrun_percentage NUMERIC(5, 2),
    assigned_to_user_id BIGINT,
    delivery_channel VARCHAR(50) NOT NULL,
    success_criteria VARCHAR(500),
    target_audience VARCHAR(500),
    dependencies JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_campaign_activities_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE,

    CONSTRAINT fk_campaign_activities_user
        FOREIGN KEY (assigned_to_user_id) REFERENCES users(id),

    CONSTRAINT ck_campaign_activities_type
        CHECK (activity_type IN ('CONTENT_CREATION', 'AD_SETUP', 'EMAIL_BLAST', 'SOCIAL_POST', 'EVENT', 'WEBINAR', 'ANALYSIS', 'OPTIMIZATION')),

    CONSTRAINT ck_campaign_activities_status
        CHECK (status IN ('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'BLOCKED'))
);

CREATE INDEX IF NOT EXISTS idx_activities_campaign_status ON campaign_activities (campaign_id, status, planned_start_date);
CREATE INDEX IF NOT EXISTS idx_activities_assigned_user ON campaign_activities (assigned_to_user_id, status);

-- -------------------------------------------
-- Campaign Interactions
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS campaign_interactions (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    interaction_type VARCHAR(50) NOT NULL,
    interaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    channel_id BIGINT,
    utm_source VARCHAR(100),
    utm_medium VARCHAR(100),
    utm_campaign VARCHAR(100),
    utm_content VARCHAR(100),
    utm_term VARCHAR(100),
    device_type VARCHAR(50),
    device_os VARCHAR(50),
    browser VARCHAR(100),
    country_code VARCHAR(2),
    city VARCHAR(100),
    deal_id BIGINT,
    conversion_value NUMERIC(10, 2),
    conversion_date TIMESTAMP,
    conversion_notes VARCHAR(1000),
    is_conversion BOOLEAN DEFAULT FALSE,
    landing_page_url VARCHAR(500),
    referrer_url VARCHAR(500),
    session_id VARCHAR(100),
    properties JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_campaign_interactions_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE,

    CONSTRAINT fk_campaign_interactions_customer
        FOREIGN KEY (customer_id) REFERENCES customer_companies(id),

    CONSTRAINT fk_campaign_interactions_channel
        FOREIGN KEY (channel_id) REFERENCES marketing_channels(id),

    CONSTRAINT ck_campaign_interactions_type
        CHECK (interaction_type IN ('AD_CLICK', 'AD_VIEW', 'EMAIL_OPEN', 'EMAIL_CLICK', 'LANDING_PAGE_VISIT', 'FORM_SUBMIT', 'SOCIAL_ENGAGEMENT', 'WEBINAR_REGISTRATION', 'WHITEPAPER_DOWNLOAD'))
);

CREATE INDEX IF NOT EXISTS idx_interactions_campaign_customer ON campaign_interactions (campaign_id, customer_id, interaction_date);
CREATE INDEX IF NOT EXISTS idx_interactions_conversion ON campaign_interactions (campaign_id, is_conversion, interaction_date);
CREATE INDEX IF NOT EXISTS idx_interactions_utm ON campaign_interactions (utm_source, utm_medium, utm_campaign);
CREATE INDEX IF NOT EXISTS idx_interactions_date_type ON campaign_interactions (interaction_date, interaction_type);

-- -------------------------------------------
-- Campaign Attribution
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS campaign_attribution (
    id BIGSERIAL PRIMARY KEY,
    deal_id BIGINT NOT NULL,
    campaign_id BIGINT NOT NULL,
    attribution_model VARCHAR(50) NOT NULL,
    attribution_percentage NUMERIC(5, 2) NOT NULL,
    attributed_revenue NUMERIC(15, 2) NOT NULL DEFAULT 0,
    touch_timestamps TIMESTAMP[],
    touch_count INTEGER DEFAULT 0,
    first_touch_weight NUMERIC(5, 2) DEFAULT 0,
    last_touch_weight NUMERIC(5, 2) DEFAULT 0,
    linear_weight NUMERIC(5, 2) DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_campaign_attribution_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE,

    CONSTRAINT ck_campaign_attribution_model
        CHECK (attribution_model IN ('FIRST_TOUCH', 'LAST_TOUCH', 'LINEAR', 'TIME_DECAY', 'CUSTOM'))
);

CREATE INDEX IF NOT EXISTS idx_attribution_deal ON campaign_attribution (deal_id, attribution_percentage);
CREATE INDEX IF NOT EXISTS idx_attribution_campaign_revenue ON campaign_attribution (campaign_id, attributed_revenue);

-- -------------------------------------------
-- A/B Tests
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS ab_tests (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    test_name VARCHAR(200) NOT NULL,
    hypothesis VARCHAR(500),
    test_type VARCHAR(50) NOT NULL,
    primary_metric VARCHAR(100) NOT NULL,
    confidence_level NUMERIC(4, 3) DEFAULT 0.950,
    required_sample_size INTEGER,
    control_variant VARCHAR(100) NOT NULL,
    treatment_variants JSONB NOT NULL,
    winning_variant VARCHAR(100),
    statistical_significance NUMERIC(4, 3),
    is_completed BOOLEAN DEFAULT FALSE,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_ab_tests_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id) ON DELETE CASCADE,

    CONSTRAINT ck_ab_tests_type
        CHECK (test_type IN ('SPLIT_URL', 'MULTIVARIATE', 'BANDIT'))
);

CREATE INDEX IF NOT EXISTS idx_ab_tests_campaign_status ON ab_tests (campaign_id, is_completed, start_date);
