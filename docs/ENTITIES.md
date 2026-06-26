# Database Entities & Schema Documentation

## Table of Contents

- [Overview](#overview)
- [Database Design](#database-design)
- [Schema Diagram](#schema-diagram)
- [Authentication & User Management](#authentication--user-management)
- [Customer Management](#customer-management)
- [CRM Entities](#crm-entities)
- [Marketing Entities](#marketing-entities)
- [Indexes & Performance](#indexes--performance)
- [Data Relationships](#data-relationships)
- [Migration History](#migration-history)

## Overview

The Marketing Company Backend uses **PostgreSQL 16** as the primary relational database. The schema is designed with data integrity, performance, and scalability in mind, following database normalization principles while maintaining practical denormalization where appropriate for performance.

### Key Features

- **Flyway Migrations**: Version-controlled schema evolution
- **Referential Integrity**: Foreign key constraints ensure data consistency
- **Check Constraints**: Business rule enforcement at database level
- **Indexes**: Optimized for common query patterns
- **Soft Deletes**: `deleted_at` column for data retention
- **Audit Fields**: Timestamp tracking for all entities
- **Optimistic Locking**: Version column prevents concurrent update conflicts

### Naming Conventions

| Convention   | Example                                   |
| ------------ | ----------------------------------------- |
| Tables       | `snake_case` (e.g., `customer_companies`) |
| Columns      | `snake_case` (e.g., `company_name`)       |
| Primary Keys | `id` (BIGSERIAL)                          |
| Foreign Keys | `{table_name}_id` (e.g., `customer_id`)   |
| Indexes      | `idx_{table}_{columns}`                   |
| Constraints  | `ck_{table}_{constraint_name}`            |

## Database Design

### Standard Fields

Every entity table includes these audit fields:

```sql
id BIGSERIAL PRIMARY KEY,
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
deleted_at TIMESTAMP,
version INTEGER NOT NULL DEFAULT 1
```

- **id**: Auto-incrementing primary key
- **created_at**: Record creation timestamp
- **updated_at**: Last modification timestamp
- **deleted_at**: Soft delete timestamp (NULL = active)
- **version**: Optimistic locking version number

## Schema Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     AUTHENTICATION                           │
│  ┌─────────┐         ┌─────────────┐                        │
│  │  users  │────────<│ user_roles  │                        │
│  └────┬────┘         └─────────────┘                        │
│       │                                                      │
└───────┼──────────────────────────────────────────────────────┘
        │
┌───────┼──────────────────────────────────────────────────────┐
│       │              CUSTOMER MANAGEMENT                      │
│  ┌────▼────────────────┐                                     │
│  │ customer_companies  │                                     │
│  └──┬─────────────┬────┘                                     │
│     │             │                                           │
│  ┌──▼───────┐ ┌──▼──────────┐                               │
│  │contacts  │ │  addresses  │                               │
│  └──────────┘ └─────────────┘                               │
└───────┬──────────────────────────────────────────────────────┘
        │
┌───────┼──────────────────────────────────────────────────────┐
│       │                    CRM                                │
│  ┌────▼─────────────┐                                        │
│  │  opportunities   │                                        │
│  └──┬───────────┬───┘                                        │
│     │           │                                             │
│  ┌──▼──────┐ ┌─▼─────┐                                      │
│  │ quotes  │ │ deals │                                      │
│  └──┬──────┘ └───────┘                                      │
│     │                                                         │
│  ┌──▼───────────┐     ┌───────────┐    ┌──────────────┐    │
│  │ quote_items  │     │  tasks    │    │interactions  │    │
│  └──────────────┘     └───────────┘    └──────────────┘    │
└─────────────────────────────────────────────────────────────┘
        │
┌───────┼──────────────────────────────────────────────────────┐
│       │                 MARKETING                             │
│  ┌────▼──────────────────┐                                   │
│  │ marketing_campaigns   │                                   │
│  └──┬────────────────┬───┘                                   │
│     │                │                                        │
│  ┌──▼───────────┐ ┌──▼─────────────┐                        │
│  │ activities   │ │ campaign_metrics│                        │
│  └──────────────┘ └─────────────────┘                        │
│     │                                                         │
│  ┌──▼────────────┐  ┌──────────────┐  ┌─────────────┐      │
│  │ attributions  │  │   ab_tests   │  │   assets    │      │
│  └───────────────┘  └──────────────┘  └─────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

## Authentication & User Management

### users

Core user authentication table.

| Column              | Type         | Description                           |
| ------------------- | ------------ | ------------------------------------- |
| id                  | BIGSERIAL    | Primary key                           |
| email               | VARCHAR(255) | Unique email address                  |
| phone_number        | VARCHAR(20)  | Phone number (optional)               |
| gender              | VARCHAR(10)  | Gender (MALE, FEMALE, OTHER, UNKNOWN) |
| date_of_birth       | DATE         | Birth date (must be 18+)              |
| hashed_password     | VARCHAR(100) | BCrypt hashed password                |
| first_name          | VARCHAR(50)  | First name (min 2 chars)              |
| last_name           | VARCHAR(50)  | Last name (min 2 chars)               |
| status              | VARCHAR(20)  | Account status                        |
| last_login_at       | TIMESTAMP    | Last login timestamp                  |
| password_changed_at | TIMESTAMP    | Last password change                  |

**Status Values:**

- `ACTIVE`: Normal active account
- `INACTIVE`: Temporarily disabled
- `PENDING_ACTIVATION`: Awaiting email verification
- `SUSPENDED`: Suspended by admin
- `DELETED`: Soft deleted account

**Constraints:**

- Email format validation: RFC 5322 compliant
- Phone format: International format with 10-20 digits
- Age validation: Must be 18+ years old
- Name length: Minimum 2 characters

**Indexes:**

```sql
CREATE UNIQUE INDEX idx_users_email ON users(email) WHERE deleted_at IS NULL;
CREATE INDEX idx_users_status ON users(status) WHERE deleted_at IS NULL;
```

### user_roles

User role assignments (many-to-many).

| Column     | Type        | Description          |
| ---------- | ----------- | -------------------- |
| user_id    | BIGINT      | Foreign key to users |
| role       | VARCHAR(20) | Role name            |
| created_at | TIMESTAMP   | Assignment timestamp |

**Role Values:**

- `ADMIN`: System administrator
- `MANAGER`: Marketing/Sales manager
- `SALES_REP`: Sales representative
- `MARKETER`: Marketing specialist
- `ANALYST`: Data analyst
- `USER`: Basic user access

### auth_sessions

Active authentication sessions (for token management).

| Column        | Type         | Description                 |
| ------------- | ------------ | --------------------------- |
| id            | VARCHAR(255) | Session ID (UUID)           |
| user_id       | BIGINT       | Foreign key to users        |
| refresh_token | VARCHAR(500) | JWT refresh token           |
| access_token  | VARCHAR(500) | JWT access token (optional) |
| expires_at    | TIMESTAMP    | Session expiration          |
| ip_address    | VARCHAR(45)  | Client IP address           |
| user_agent    | VARCHAR(500) | Client user agent           |
| is_active     | BOOLEAN      | Session active flag         |

## Customer Management

### customer_companies

Primary customer/company information.

| Column                  | Type          | Description                  |
| ----------------------- | ------------- | ---------------------------- |
| id                      | BIGSERIAL     | Primary key                  |
| company_name            | VARCHAR(255)  | Company display name         |
| legal_name              | VARCHAR(255)  | Legal entity name            |
| tax_id                  | VARCHAR(50)   | Tax ID (unique)              |
| website                 | VARCHAR(500)  | Company website              |
| founding_year           | INTEGER       | Year founded (1500-current)  |
| industry_code           | VARCHAR(20)   | Industry classification code |
| industry_name           | VARCHAR(255)  | Industry name                |
| sector                  | VARCHAR(100)  | Economic sector              |
| company_size            | VARCHAR(20)   | Size category                |
| employee_count          | INTEGER       | Number of employees          |
| annual_revenue_amount   | NUMERIC(15,2) | Annual revenue               |
| annual_revenue_currency | CHAR(3)       | Currency code (ISO 4217)     |
| revenue_range           | VARCHAR(20)   | Revenue range category       |
| status                  | VARCHAR(20)   | Relationship status          |
| is_public_company       | BOOLEAN       | Publicly traded              |
| is_startup              | BOOLEAN       | Is a startup                 |
| target_market           | VARCHAR(1000) | Target market description    |
| mission_statement       | VARCHAR(2000) | Company mission              |

**Company Size Values:**

- `MICRO`: < 10 employees
- `SMALL`: 10-50 employees
- `MEDIUM`: 50-250 employees
- `LARGE`: 250-1000 employees
- `ENTERPRISE`: > 1000 employees

**Status Values:**

- `LEAD`: Potential customer
- `PROSPECT`: QUALIFICATION lead
- `ACTIVE`: Active customer
- `INACTIVE`: Inactive customer
- `CHURNED`: Lost customer
- `SUSPENDED`: Account suspended

**Constraints:**

- Tax ID uniqueness (if provided)
- Founding year: 1500 to current year
- Employee count: Non-negative
- Revenue amount: Non-negative

### contact_persons

Contact people within customer companies.

| Column              | Type         | Description                       |
| ------------------- | ------------ | --------------------------------- |
| id                  | BIGSERIAL    | Primary key                       |
| customer_company_id | BIGINT       | Foreign key to customer_companies |
| first_name          | VARCHAR(100) | First name                        |
| last_name           | VARCHAR(100) | Last name                         |
| email               | VARCHAR(255) | Email address                     |
| phone               | VARCHAR(20)  | Phone number                      |
| position            | VARCHAR(100) | Job title                         |
| department          | VARCHAR(100) | Department                        |
| is_decision_maker   | BOOLEAN      | Has decision authority            |
| is_primary_contact  | BOOLEAN      | Primary contact flag              |
| is_active           | BOOLEAN      | Contact is active                 |

### company_addresses

Company addresses (headquarters, billing, etc.).

| Column              | Type         | Description                       |
| ------------------- | ------------ | --------------------------------- |
| id                  | BIGSERIAL    | Primary key                       |
| customer_company_id | BIGINT       | Foreign key to customer_companies |
| address_type        | VARCHAR(50)  | Address type                      |
| street              | VARCHAR(255) | Street address                    |
| city                | VARCHAR(100) | City                              |
| state               | VARCHAR(100) | State/Province                    |
| postal_code         | VARCHAR(20)  | Postal/ZIP code                   |
| country             | VARCHAR(100) | Country                           |
| is_primary          | BOOLEAN      | Primary address flag              |

**Address Types:**

- `HEADQUARTERS`: Main office
- `BILLING`: Billing address
- `SHIPPING`: Shipping address
- `BRANCH`: Branch office

### contracts

Customer contracts and agreements.

| Column              | Type          | Description                       |
| ------------------- | ------------- | --------------------------------- |
| id                  | BIGSERIAL     | Primary key                       |
| customer_company_id | BIGINT        | Foreign key to customer_companies |
| contract_number     | VARCHAR(50)   | Unique contract number            |
| contract_type       | VARCHAR(50)   | Contract type                     |
| start_date          | DATE          | Contract start date               |
| end_date            | DATE          | Contract end date                 |
| monthly_fee         | NUMERIC(10,2) | Monthly fee amount                |
| total_value         | NUMERIC(15,2) | Total contract value              |
| payment_terms       | VARCHAR(50)   | Payment terms                     |
| auto_renewal        | BOOLEAN       | Auto-renewal flag                 |
| renewal_notice_days | INTEGER       | Days notice for renewal           |
| status              | VARCHAR(50)   | Contract status                   |

## CRM Entities

### opportunities

Sales opportunities in the pipeline.

| Column              | Type          | Description                       |
| ------------------- | ------------- | --------------------------------- |
| id                  | BIGSERIAL     | Primary key                       |
| customer_company_id | BIGINT        | Foreign key to customer_companies |
| title               | VARCHAR(255)  | Opportunity title                 |
| amount              | NUMERIC(15,2) | Estimated value                   |
| stage               | VARCHAR(50)   | Pipeline stage                    |
| expected_close_date | DATE          | Expected close date               |

**Stage Values:**

- `PROSPECTING`: Initial contact
- `QUALIFICATION`: Qualifying opportunity
- `PROPOSAL`: Proposal submitted
- `NEGOTIATION`: In negotiation
- `CLOSED_WON`: Successfully closed
- `CLOSED_LOST`: Lost opportunity

### services_packages

Service offerings and packages.

| Column           | Type          | Description          |
| ---------------- | ------------- | -------------------- |
| id               | BIGSERIAL     | Primary key          |
| name             | VARCHAR(100)  | Service name         |
| description      | TEXT          | Detailed description |
| price            | NUMERIC(10,2) | Base price           |
| service_type     | VARCHAR(50)   | Service category     |
| deliverables     | TEXT          | What's included      |
| estimated_hours  | INTEGER       | Estimated hours      |
| complexity       | VARCHAR(20)   | Complexity level     |
| is_recurring     | BOOLEAN       | Recurring service    |
| frequency        | VARCHAR(50)   | Billing frequency    |
| project_duration | INTEGER       | Duration in days     |
| active           | BOOLEAN       | Service is active    |

**Service Types:**

- `SOCIAL_MEDIA`: Social media management
- `SEO`: Search engine optimization
- `CONTENT_MARKETING`: Content creation
- `PPC`: Pay-per-click advertising
- `EMAIL_MARKETING`: Email campaigns
- `ANALYTICS`: Analytics and reporting
- `STRATEGY`: Marketing strategy

**Complexity Levels:**

- `LOW`: Simple, straightforward
- `MEDIUM`: Moderate complexity
- `HIGH`: Complex requirements
- `VERY_HIGH`: Highly complex

### quotes

Price quotations for opportunities.

| Column              | Type          | Description                       |
| ------------------- | ------------- | --------------------------------- |
| id                  | BIGSERIAL     | Primary key                       |
| opportunity_id      | BIGINT        | Foreign key to opportunities      |
| customer_company_id | BIGINT        | Foreign key to customer_companies |
| quote_number        | VARCHAR(50)   | Unique quote number               |
| status              | VARCHAR(50)   | Quote status                      |
| valid_until         | DATE          | Expiration date                   |
| subtotal            | NUMERIC(15,2) | Subtotal amount                   |
| discount_amount     | NUMERIC(15,2) | Discount applied                  |
| tax_amount          | NUMERIC(15,2) | Tax amount                        |
| total_amount        | NUMERIC(15,2) | Total amount                      |
| notes               | TEXT          | Additional notes                  |

**Status Values:**

- `DRAFT`: Being prepared
- `SENT`: Sent to customer
- `VIEWED`: Customer viewed
- `ACCEPTED`: Customer accepted
- `REJECTED`: Customer rejected
- `EXPIRED`: Quote expired

### quote_items

Line items in a quote.

| Column              | Type          | Description                      |
| ------------------- | ------------- | -------------------------------- |
| id                  | BIGSERIAL     | Primary key                      |
| quote_id            | BIGINT        | Foreign key to quotes            |
| service_package_id  | BIGINT        | Foreign key to services_packages |
| description         | VARCHAR(500)  | Item description                 |
| quantity            | INTEGER       | Quantity                         |
| unit_price          | NUMERIC(10,2) | Price per unit                   |
| discount_percentage | NUMERIC(5,2)  | Discount %                       |
| total_price         | NUMERIC(15,2) | Line total                       |

### deals

Closed/signed contracts from won opportunities.

| Column              | Type          | Description                       |
| ------------------- | ------------- | --------------------------------- |
| id                  | BIGSERIAL     | Primary key                       |
| customer_id         | BIGINT        | Foreign key to customer_companies |
| opportunity_id      | BIGINT        | Foreign key to opportunities      |
| deal_status         | VARCHAR(50)   | Deal status                       |
| final_amount        | NUMERIC(15,2) | Final agreed amount               |
| start_date          | DATE          | Contract start                    |
| end_date            | DATE          | Contract end                      |
| campaign_manager_id | BIGINT        | Assigned manager                  |
| deliverables        | TEXT          | Deliverables                      |
| terms               | TEXT          | Terms and conditions              |

**Deal Status Values:**

- `DRAFT`: Being prepared
- `IN_NEGOTIATION`: Under negotiation
- `SIGNED`: Contract signed
- `PAID`: Payment received
- `IN_PROGRESS`: Work in progress
- `COMPLETED`: Successfully completed
- `CANCELLED`: Contract cancelled

### tasks

CRM tasks and to-dos.

| Column              | Type         | Description          |
| ------------------- | ------------ | -------------------- |
| id                  | BIGSERIAL    | Primary key          |
| title               | VARCHAR(255) | Task title           |
| description         | TEXT         | Detailed description |
| due_date            | TIMESTAMP    | Due date/time        |
| status              | VARCHAR(50)  | Task status          |
| priority            | VARCHAR(50)  | Priority level       |
| customer_id         | BIGINT       | Related customer     |
| opportunity_id      | BIGINT       | Related opportunity  |
| assigned_to_user_id | BIGINT       | Assigned user        |

**Status Values:**

- `PENDING`: Not started
- `IN_PROGRESS`: Being worked on
- `COMPLETED`: Finished
- `CANCELLED`: Cancelled
- `BLOCKED`: Blocked by dependency

**Priority Values:**

- `LOW`: Low priority
- `MEDIUM`: Medium priority
- `HIGH`: High priority
- `URGENT`: Urgent/critical

### interactions

Customer interaction tracking.

| Column             | Type         | Description                       |
| ------------------ | ------------ | --------------------------------- |
| id                 | BIGSERIAL    | Primary key                       |
| customer_id        | BIGINT       | Foreign key to customer_companies |
| type               | VARCHAR(50)  | Interaction type                  |
| date_time          | TIMESTAMP    | When it occurred                  |
| description        | TEXT         | Description                       |
| outcome            | VARCHAR(500) | Outcome/result                    |
| feedback_type      | VARCHAR(20)  | Feedback sentiment                |
| channel_preference | VARCHAR(50)  | Preferred channel                 |

**Interaction Types:**

- `EMAIL`: Email communication
- `CALL`: Phone call
- `MEETING`: In-person meeting
- `DEMO`: Product demo
- `PRESENTATION`: Sales presentation
- `FOLLOW_UP`: Follow-up contact
- `SUPPORT`: Support request

**Feedback Types:**

- `POSITIVE`: Positive feedback
- `NEUTRAL`: Neutral feedback
- `NEGATIVE`: Negative feedback
- `COMPLAINT`: Customer complaint

## Marketing Entities

### marketing_campaigns

Marketing campaign management.

| Column                       | Type          | Description         |
| ---------------------------- | ------------- | ------------------- |
| id                           | BIGSERIAL     | Primary key         |
| name                         | VARCHAR(100)  | Campaign name       |
| description                  | VARCHAR(1000) | Description         |
| campaign_type                | VARCHAR(50)   | Campaign type       |
| status                       | VARCHAR(30)   | Campaign status     |
| total_budget                 | NUMERIC(15,2) | Total budget        |
| spent_amount                 | NUMERIC(15,2) | Amount spent        |
| budget_allocations           | JSONB         | Budget breakdown    |
| start_date                   | DATE          | Start date          |
| end_date                     | DATE          | End date            |
| target_audience_demographics | JSONB         | Target demographics |
| target_locations             | JSONB         | Target locations    |
| target_interests             | JSONB         | Target interests    |
| primary_goal                 | VARCHAR(200)  | Main objective      |
| success_criteria             | JSONB         | Success metrics     |

**Campaign Types:**

- `AWARENESS`: Brand awareness
- `CONVERSION`: Drive conversions
- `RETENTION`: Customer retention
- `REACTIVATION`: Re-engage customers
- `BRANDING`: Brand building

**Status Values:**

- `DRAFT`: Being planned
- `PLANNED`: Ready to launch
- `ACTIVE`: Currently running
- `PAUSED`: Temporarily paused
- `COMPLETED`: Finished
- `CANCELLED`: Cancelled

### marketing_channels

Marketing channels/platforms.

| Column                      | Type          | Description       |
| --------------------------- | ------------- | ----------------- |
| id                          | BIGSERIAL     | Primary key       |
| name                        | VARCHAR(100)  | Channel name      |
| channel_type                | VARCHAR(50)   | Channel type      |
| description                 | TEXT          | Description       |
| default_cost_per_click      | NUMERIC(10,2) | Default CPC       |
| default_cost_per_impression | NUMERIC(10,4) | Default CPM       |
| is_active                   | BOOLEAN       | Channel is active |

**Channel Types:**

- `PAID`: Paid advertising
- `ORGANIC`: Organic reach
- `SOCIAL`: Social media
- `DIRECT`: Direct traffic
- `EMAIL`: Email marketing
- `REFERRAL`: Referral traffic

### campaign_activities

Activities within campaigns.

| Column               | Type          | Description                        |
| -------------------- | ------------- | ---------------------------------- |
| id                   | BIGSERIAL     | Primary key                        |
| campaign_id          | BIGINT        | Foreign key to marketing_campaigns |
| name                 | VARCHAR(200)  | Activity name                      |
| activity_type        | VARCHAR(50)   | Activity type                      |
| description          | TEXT          | Description                        |
| channel_id           | BIGINT        | Channel used                       |
| scheduled_date       | TIMESTAMP     | Scheduled time                     |
| execution_date       | TIMESTAMP     | Actual execution time              |
| status               | VARCHAR(30)   | Activity status                    |
| budget               | NUMERIC(10,2) | Activity budget                    |
| target_audience_json | JSONB         | Target audience                    |

**Activity Types:**

- `EMAIL`: Email blast
- `SOCIAL_POST`: Social media post
- `AD_CAMPAIGN`: Ad campaign
- `WEBINAR`: Webinar event
- `CONTENT`: Content publication
- `EVENT`: Marketing event

### campaign_metrics

Campaign performance metrics.

| Column      | Type          | Description                        |
| ----------- | ------------- | ---------------------------------- |
| id          | BIGSERIAL     | Primary key                        |
| campaign_id | BIGINT        | Foreign key to marketing_campaigns |
| metric_date | DATE          | Metric date                        |
| impressions | BIGINT        | Total impressions                  |
| clicks      | INTEGER       | Total clicks                       |
| conversions | INTEGER       | Total conversions                  |
| cost        | NUMERIC(10,2) | Cost                               |
| revenue     | NUMERIC(10,2) | Revenue generated                  |
| channel_id  | BIGINT        | Channel                            |

**Calculated Metrics:**

- **CTR** (Click-Through Rate): `clicks / impressions * 100`
- **Conversion Rate**: `conversions / clicks * 100`
- **CPC** (Cost Per Click): `cost / clicks`
- **ROI**: `(revenue - cost) / cost * 100`

### campaign_attributions

Attribution tracking for conversions.

| Column               | Type          | Description                        |
| -------------------- | ------------- | ---------------------------------- |
| id                   | BIGSERIAL     | Primary key                        |
| campaign_id          | BIGINT        | Foreign key to marketing_campaigns |
| customer_id          | BIGINT        | Customer who converted             |
| touchpoint_type      | VARCHAR(50)   | Touchpoint type                    |
| touchpoint_timestamp | TIMESTAMP     | When it occurred                   |
| channel_id           | BIGINT        | Channel used                       |
| conversion_value     | NUMERIC(10,2) | Conversion value                   |
| attribution_model    | VARCHAR(50)   | Attribution model used             |
| attributed_revenue   | NUMERIC(10,2) | Attributed revenue                 |

**Attribution Models:**

- `FIRST_TOUCH`: First interaction
- `LAST_TOUCH`: Last interaction
- `LINEAR`: Equal credit
- `TIME_DECAY`: Decay over time
- `POSITION_BASED`: U-shaped

### marketing_assets

Marketing creative assets.

| Column      | Type          | Description                        |
| ----------- | ------------- | ---------------------------------- |
| id          | BIGSERIAL     | Primary key                        |
| campaign_id | BIGINT        | Foreign key to marketing_campaigns |
| name        | VARCHAR(200)  | Asset name                         |
| asset_type  | VARCHAR(50)   | Asset type                         |
| file_url    | VARCHAR(1000) | File location                      |
| file_size   | BIGINT        | File size (bytes)                  |
| format      | VARCHAR(50)   | File format                        |
| dimensions  | VARCHAR(50)   | Dimensions                         |
| tags        | JSONB         | Tags array                         |

**Asset Types:**

- `IMAGE`: Image file
- `VIDEO`: Video file
- `DOCUMENT`: Document
- `AUDIO`: Audio file
- `TEMPLATE`: Template file

### ab_tests

A/B testing experiments.

| Column              | Type         | Description                        |
| ------------------- | ------------ | ---------------------------------- |
| id                  | BIGSERIAL    | Primary key                        |
| campaign_id         | BIGINT       | Foreign key to marketing_campaigns |
| name                | VARCHAR(200) | Test name                          |
| description         | TEXT         | Test description                   |
| start_date          | DATE         | Test start                         |
| end_date            | DATE         | Test end                           |
| status              | VARCHAR(30)  | Test status                        |
| success_metric      | VARCHAR(50)  | Success metric                     |
| minimum_sample_size | INTEGER      | Min sample size                    |
| confidence_level    | NUMERIC(5,2) | Confidence level                   |
| winning_variant_id  | BIGINT       | Winning variant                    |

## Indexes & Performance

### Primary Indexes

All tables have primary key indexes on `id`:

```sql
PRIMARY KEY (id)  -- Automatically creates index
```

### Foreign Key Indexes

Foreign keys automatically create indexes:

```sql
FOREIGN KEY (customer_company_id) REFERENCES customer_companies(id)
```

### Custom Indexes (V7 Migration)

```sql
-- User lookups
CREATE INDEX idx_users_email ON users(email) WHERE deleted_at IS NULL;
CREATE INDEX idx_users_status ON users(status);

-- Customer queries
CREATE INDEX idx_customer_companies_status ON customer_companies(status);
CREATE INDEX idx_customer_companies_industry ON customer_companies(industry_code);
CREATE INDEX idx_customer_companies_size ON customer_companies(company_size);

-- CRM queries
CREATE INDEX idx_opportunities_stage ON opportunities(stage);
CREATE INDEX idx_opportunities_customer ON opportunities(customer_company_id);
CREATE INDEX idx_opportunities_close_date ON opportunities(expected_close_date);

CREATE INDEX idx_deals_status ON deals(deal_status);
CREATE INDEX idx_deals_customer ON deals(customer_id);
CREATE INDEX idx_deals_dates ON deals(start_date, end_date);

CREATE INDEX idx_tasks_assigned_to ON tasks(assigned_to_user_id);
CREATE INDEX idx_tasks_due_date ON tasks(due_date);
CREATE INDEX idx_tasks_status ON tasks(status);

-- Marketing queries
CREATE INDEX idx_campaigns_status ON marketing_campaigns(status);
CREATE INDEX idx_campaigns_dates ON marketing_campaigns(start_date, end_date);
CREATE INDEX idx_campaign_metrics_date ON campaign_metrics(metric_date);
CREATE INDEX idx_campaign_metrics_campaign ON campaign_metrics(campaign_id);
```

## Data Relationships

### One-to-Many Relationships

```
users (1) ──────────> (N) tasks
users (1) ──────────> (N) deals
customer_companies (1) ──> (N) contact_persons
customer_companies (1) ──> (N) company_addresses
customer_companies (1) ──> (N) opportunities
customer_companies (1) ──> (N) interactions
opportunities (1) ─────> (N) quotes
quotes (1) ───────────> (N) quote_items
marketing_campaigns (1) ──> (N) campaign_activities
marketing_campaigns (1) ──> (N) campaign_metrics
```

### Many-to-Many Relationships

```
users (N) ←─── user_roles ───→ (N) roles
opportunities (N) ←─── opportunity_services ───→ (N) services_packages
deals (N) ←─── deal_services ───→ (N) services_packages
campaigns (N) ←─── campaign_channels ───→ (N) marketing_channels
```

## Migration History

| Version | File                                            | Description                   |
| ------- | ----------------------------------------------- | ----------------------------- |
| V1      | `V1__users_schema.sql`                          | User authentication and roles |
| V2      | `V2__customer_companies_schema.sql`             | Customer management           |
| V3      | `V3__opportunities_service_package_schema.sql`  | Opportunities and services    |
| V4      | `V4__crm_deals_schema.sql`                      | Deals and contracts           |
| V5      | `V5__crm_quotes_schema.sql`                     | Quotes and quote items        |
| V6      | `V6__crm_tasks_interactions_schema.sql`         | Tasks and interactions        |
| V7      | `V7__crm_indexes.sql`                           | Performance indexes           |
| V8      | `V8__insert_crm_demo_data.sql`                  | Sample CRM data               |
| V9      | `V9__comment_views.sql`                         | Database views                |
| V10     | `V10__marketing_management_table.sql`           | Marketing tables              |
| V11     | `V11__inset_marketing_management_demo_data.sql` | Sample marketing data         |

### Running Migrations

```bash
# Check current version
./gradlew flywayInfo

# Run pending migrations
./gradlew flywayMigrate

# Validate migrations
./gradlew flywayValidate

# Repair failed migration (CAUTION)
./gradlew flywayRepair
```

### Creating New Migrations

1. Create file: `src/main/resources/db/migration/V{version}__{description}.sql`
2. Write SQL DDL statements
3. Test locally before deploying
4. Never modify existing migrations

Example:

```sql
-- V12__add_customer_preferences.sql

ALTER TABLE customer_companies
ADD COLUMN communication_preferences JSONB;

CREATE INDEX idx_customer_preferences
ON customer_companies USING gin(communication_preferences);
```

## Database Backup

```bash
# Full backup
docker-compose exec postgres pg_dump \
  -U postgres \
  marketing_company_db > backup.sql

# Schema only
docker-compose exec postgres pg_dump \
  -U postgres \
  --schema-only \
  marketing_company_db > schema.sql

# Data only
docker-compose exec postgres pg_dump \
  -U postgres \
  --data-only \
  marketing_company_db > data.sql

# Specific table
docker-compose exec postgres pg_dump \
  -U postgres \
  -t customer_companies \
  marketing_company_db > customers.sql
```

## Query Examples

### Find Active Campaigns with Metrics

```sql
SELECT
    c.id,
    c.name,
    c.status,
    c.total_budget,
    c.spent_amount,
    SUM(m.impressions) as total_impressions,
    SUM(m.clicks) as total_clicks,
    SUM(m.conversions) as total_conversions,
    (SUM(m.revenue) - SUM(m.cost)) / NULLIF(SUM(m.cost), 0) * 100 as roi
FROM marketing_campaigns c
LEFT JOIN campaign_metrics m ON c.id = m.campaign_id
WHERE c.status = 'ACTIVE'
  AND c.deleted_at IS NULL
GROUP BY c.id, c.name, c.status, c.total_budget, c.spent_amount
ORDER BY roi DESC;
```

### Customer Opportunity Pipeline

```sql
SELECT
    cc.company_name,
    COUNT(o.id) as total_opportunities,
    SUM(CASE WHEN o.stage = 'CLOSED_WON' THEN o.amount ELSE 0 END) as won_amount,
    SUM(CASE WHEN o.stage IN ('PROSPECTING', 'QUALIFICATION', 'PROPOSAL', 'NEGOTIATION')
        THEN o.amount ELSE 0 END) as pipeline_value
FROM customer_companies cc
LEFT JOIN opportunities o ON cc.id = o.customer_company_id
WHERE cc.status = 'ACTIVE'
  AND cc.deleted_at IS NULL
GROUP BY cc.id, cc.company_name
HAVING COUNT(o.id) > 0
ORDER BY pipeline_value DESC;
```

---

For more information about the application architecture, see [ARCHITECTURE.md](ARCHITECTURE.md).
