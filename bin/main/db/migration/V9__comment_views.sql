-- ============================================================================
-- COMMENT VIEWS - Add descriptive comments to database objects
-- ============================================================================
-- Purpose: Document database tables, columns, and views with descriptive comments
-- Dependencies: V1-V8 (all tables must exist)
-- ============================================================================

COMMENT ON TABLE users IS 'System users with authentication and audit fields';
COMMENT ON COLUMN users.email IS 'User email address (unique identifier for login)';
COMMENT ON COLUMN users.hashed_password IS 'BCrypt hashed password used for authentication';
COMMENT ON COLUMN users.status IS 'User account status: ACTIVE, INACTIVE, PENDING_ACTIVATION, SUSPENDED, DELETED';
COMMENT ON TABLE user_roles IS 'Mapping of users to roles (one user can have multiple roles)';
COMMENT ON COLUMN user_roles.role IS 'Role assigned to a user: ADMIN, SALES_MANAGER, SALES_REP, MARKETING_MANAGER';

COMMENT ON TABLE customer_companies IS 'B2B customer organizations and their information';
COMMENT ON COLUMN customer_companies.company_name IS 'Display name of the customer company';
COMMENT ON COLUMN customer_companies.legal_name IS 'Legal registered name of the company';
COMMENT ON COLUMN customer_companies.industry_code IS 'Internal industry code';
COMMENT ON COLUMN customer_companies.industry_name IS 'Human-readable industry name';
COMMENT ON COLUMN customer_companies.employee_count IS 'Number of employees';
COMMENT ON COLUMN customer_companies.annual_revenue_amount IS 'Estimated annual revenue amount';
COMMENT ON COLUMN customer_companies.annual_revenue_currency IS 'Currency code for revenue amount (ISO 4217)';

COMMENT ON TABLE opportunities IS 'Sales opportunities tracking potential deals';
COMMENT ON COLUMN opportunities.title IS 'Short descriptive title for the opportunity';
COMMENT ON COLUMN opportunities.amount IS 'Monetary amount associated with the opportunity';
COMMENT ON COLUMN opportunities.stage IS 'Current sales stage in the pipeline (PROSPECTING, QUALIFICATION, PROPOSAL, NEGOTIATION, CLOSED_WON, CLOSED_LOST)';
COMMENT ON COLUMN opportunities.expected_close_date IS 'Anticipated date for closing the opportunity';

COMMENT ON TABLE services_packages IS 'Predefined service offerings and packages';
COMMENT ON COLUMN services_packages.name IS 'Human-friendly package name';
COMMENT ON COLUMN services_packages.price IS 'Base price for the service/package';
COMMENT ON COLUMN services_packages.service_type IS 'Type/category of the service (SOCIAL_MEDIA, SEO, PPC, EMAIL_MARKETING, ANALYTICS, STRATEGY, etc.)';

COMMENT ON TABLE deals IS 'Closed or active business deals with customers';
COMMENT ON COLUMN deals.deal_status IS 'Deal lifecycle status (DRAFT, IN_NEGOTIATION, SIGNED, PAID, IN_PROGRESS, COMPLETED, CANCELLED)';
COMMENT ON COLUMN deals.final_amount IS 'Final agreed amount for the deal';
COMMENT ON COLUMN deals.start_date IS 'Deal start date';
COMMENT ON COLUMN deals.end_date IS 'Deal end or renewal date';
COMMENT ON COLUMN deals.customer_id IS 'Reference to the customer_company that owns the deal';

COMMENT ON TABLE deal_service_packages IS 'Services included in each deal with pricing and quantities';
COMMENT ON COLUMN deal_service_packages.quantity IS 'Number of service units purchased';
COMMENT ON COLUMN deal_service_packages.unit_price IS 'Unit price for the service at time of sale';

COMMENT ON TABLE quotes IS 'Price quotes generated for customers';
COMMENT ON COLUMN quotes.status IS 'DRAFT, SENT, ACCEPTED, REJECTED, EXPIRED';
COMMENT ON COLUMN quotes.valid_until IS 'Quote expiration date';

COMMENT ON TABLE quote_items IS 'Individual line items within a quote';
COMMENT ON COLUMN quote_items.unit_price IS 'Unit price for the quoted service/package';
COMMENT ON COLUMN quote_items.total IS 'Total price for the line item before discounts';

COMMENT ON TABLE tasks IS 'Work tasks associated with deals, opportunities, or customers';
COMMENT ON COLUMN tasks.title IS 'Short title describing the task';
COMMENT ON COLUMN tasks.status IS 'Task status: PENDING, IN_PROGRESS, COMPLETED, CANCELLED, BLOCKED';
COMMENT ON COLUMN tasks.priority IS 'Task priority: LOW, MEDIUM, HIGH, URGENT';

COMMENT ON TABLE interactions IS 'Customer interaction history log';
COMMENT ON COLUMN interactions.type IS 'Type: EMAIL, CALL, MEETING, DEMO, PRESENTATION, FOLLOW_UP, SUPPORT, NOTE';
COMMENT ON COLUMN interactions.date_time IS 'Timestamp for when the interaction occurred';
COMMENT ON COLUMN interactions.outcome IS 'Result or summary of the interaction';
COMMENT ON COLUMN interactions.feedback_type IS 'Optional feedback category: POSITIVE, NEUTRAL, NEGATIVE, COMPLAINT';
