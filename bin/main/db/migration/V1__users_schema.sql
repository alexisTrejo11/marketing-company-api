-- ===========================================
-- Users Table
-- ===========================================
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    gender VARCHAR(10) NOT NULL,
    date_of_birth DATE,
    hashed_password VARCHAR(100) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    last_login_at TIMESTAMP,
    password_changed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1,

    -- Constraints Checks
    CONSTRAINT ck_users_gender
        CHECK (gender IN ('MALE', 'FEMALE', 'OTHER', 'UNKNOWN')),

    CONSTRAINT ck_users_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING_ACTIVATION', 'SUSPENDED', 'DELETED')),

    CONSTRAINT ck_users_email_format
        CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),

    CONSTRAINT ck_users_phone_format
        CHECK (phone_number IS NULL OR phone_number ~ '^\+?[0-9\s\-\(\)]{10,20}$'),

    CONSTRAINT ck_users_date_of_birth
        CHECK (date_of_birth IS NULL OR date_of_birth <= CURRENT_DATE),

    CONSTRAINT ck_users_age_adult
        CHECK (date_of_birth IS NULL OR
               EXTRACT(YEAR FROM AGE(date_of_birth)) >= 18),

    CONSTRAINT ck_users_names_length
        CHECK (LENGTH(first_name) >= 2 AND LENGTH(last_name) >= 2)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
