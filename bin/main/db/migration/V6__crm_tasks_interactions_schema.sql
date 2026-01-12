-- ===========================================
-- TASKS TABLE
-- ===========================================
CREATE TABLE IF NOT EXISTS tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    customer_id BIGINT,
    opportunity_id BIGINT,
    assigned_to_user_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_tasks_customer
        FOREIGN KEY (customer_id)
        REFERENCES customer_companies(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_tasks_opportunity
        FOREIGN KEY (opportunity_id)
        REFERENCES opportunities(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_tasks_assigned_to
        FOREIGN KEY (assigned_to_user_id)
        REFERENCES users(id)
        ON DELETE SET NULL,

    CONSTRAINT ck_task_status_valid
        CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'BLOCKED')),

    CONSTRAINT ck_task_priority_valid
        CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT'))


);

-- ===========================================
-- INTERACTIONS TABLE
-- ===========================================
CREATE TABLE IF NOT EXISTS interactions (
    id BIGSERIAL PRIMARY KEY,
    -- Changed from VARCHAR(36) to BIGINT to reference customer_companies.id
    customer_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    description TEXT,
    outcome VARCHAR(500) NOT NULL,
    feedback_type VARCHAR(20),
    channel_preference VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_interactions_customer
        FOREIGN KEY (customer_id)
        REFERENCES customer_companies(id)
        ON DELETE CASCADE,

    CONSTRAINT ck_interaction_type_valid
        CHECK (type IN ('EMAIL', 'CALL', 'MEETING', 'DEMO', 'PRESENTATION', 'FOLLOW_UP', 'SUPPORT')),

    CONSTRAINT ck_feedback_type_valid
        CHECK (feedback_type IS NULL OR
               feedback_type IN ('POSITIVE', 'NEUTRAL', 'NEGATIVE', 'COMPLAINT')),

    CONSTRAINT ck_date_time_not_future
        CHECK (date_time <= CURRENT_TIMESTAMP + INTERVAL '1 hour'),

    CONSTRAINT ck_channel_preference_valid
        CHECK (channel_preference IS NULL OR
               channel_preference IN ('EMAIL', 'PHONE', 'IN_PERSON', 'VIDEO_CALL', 'CHAT'))
);