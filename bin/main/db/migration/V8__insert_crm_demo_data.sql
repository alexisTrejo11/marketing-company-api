-- ============================================================================
-- CRM DEMO DATA INSERTION
-- ============================================================================
-- Purpose: Insert sample data for CRM modules (deals, opportunities, quotes, tasks, interactions)
-- Dependencies: V1-V7 (all CRM schemas must exist)
-- ============================================================================

-- Insert Users (10 users)
INSERT INTO users (email, phone_number, gender, date_of_birth, hashed_password, first_name, last_name, status, created_at, updated_at) VALUES
('john.smith@company.com', '+1-555-0101', 'MALE', '1980-05-15', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'John', 'Smith', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('sarah.johnson@company.com', '+1-555-0102', 'FEMALE', '1985-08-22', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'Sarah', 'Johnson', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('michael.williams@company.com', '+1-555-0103', 'MALE', '1988-03-10', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'Michael', 'Williams', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('emily.brown@company.com', '+1-555-0104', 'FEMALE', '1990-11-30', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'Emily', 'Brown', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('david.jones@company.com', '+1-555-0105', 'MALE', '1982-07-18', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'David', 'Jones', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jessica.davis@company.com', '+1-555-0106', 'FEMALE', '1987-12-05', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'Jessica', 'Davis', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('robert.miller@company.com', '+1-555-0107', 'MALE', '1991-04-25', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'Robert', 'Miller', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('lisa.wilson@company.com', '+1-555-0108', 'FEMALE', '1989-09-14', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'Lisa', 'Wilson', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('james.moore@company.com', '+1-555-0109', 'MALE', '1984-01-20', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'James', 'Moore', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jennifer.taylor@company.com', '+1-555-0110', 'FEMALE', '1986-06-08', '$2a$10$xQy8V5kJZwV5kJZwV5kJZe', 'Jennifer', 'Taylor', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert User Roles (mapping users to roles)
INSERT INTO user_roles (user_id, role, created_at) VALUES
(1, 'ADMIN', CURRENT_TIMESTAMP),
(2, 'SALES_MANAGER', CURRENT_TIMESTAMP),
(3, 'SALES_REP', CURRENT_TIMESTAMP),
(4, 'SALES_REP', CURRENT_TIMESTAMP),
(5, 'MARKETING_MANAGER', CURRENT_TIMESTAMP),
(6, 'MARKETING_MANAGER', CURRENT_TIMESTAMP),
(7, 'SALES_REP', CURRENT_TIMESTAMP),
(8, 'SALES_REP', CURRENT_TIMESTAMP),
(9, 'MARKETING_MANAGER', CURRENT_TIMESTAMP),
(10, 'SALES_MANAGER', CURRENT_TIMESTAMP);

-- Insert Customer Companies (15 companies)
INSERT INTO customer_companies (company_name, legal_name, website, founding_year, industry_code, industry_name, sector, company_size, employee_count, annual_revenue_amount, annual_revenue_currency, revenue_range, status, is_public_company, is_startup, target_market, mission_statement, created_at, updated_at) VALUES
('TechCorp Solutions', 'TechCorp Solutions Inc.', 'https://techcorp.example.com', 2010, 'TECH001', 'Technology', 'Software', 'ENTERPRISE', 500, 5000000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', false, false, 'B2B SaaS enterprises', 'Empowering businesses through innovative technology solutions', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Global Finance Inc', 'Global Finance Incorporated', 'https://globalfinance.example.com', 2005, 'FIN001', 'Finance', 'Banking', 'ENTERPRISE', 750, 8000000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', true, false, 'Corporate financial services', 'Delivering world-class financial solutions', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HealthCare Plus', 'HealthCare Plus LLC', 'https://healthcareplus.example.com', 2015, 'HEALTH01', 'Healthcare', 'Medical Services', 'LARGE', 300, 3000000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', false, false, 'Healthcare providers', 'Improving patient care through innovation', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Retail Masters LLC', 'Retail Masters Limited Liability Company', 'https://retailmasters.example.com', 2012, 'RET001', 'Retail', 'Commerce', 'MEDIUM', 150, 1500000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', false, false, 'B2C retail customers', 'Delivering quality products nationwide', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('EduTech Academy', 'EduTech Academy Corp', 'https://edutech.example.com', 2018, 'EDU001', 'Education', 'E-Learning', 'MEDIUM', 100, 1000000.00, 'USD', 'BETWEEN_100K_1M', 'PROSPECT', false, true, 'Online education students', 'Transforming education through technology', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Manufacturing Pro', 'Manufacturing Pro Industries', 'https://mfgpro.example.com', 2008, 'MFG001', 'Manufacturing', 'Industrial', 'LARGE', 400, 4000000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', false, false, 'Industrial equipment buyers', 'Building the future of manufacturing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cloud Services Co', 'Cloud Services Company', 'https://cloudservices.example.com', 2016, 'TECH002', 'Technology', 'Cloud Computing', 'MEDIUM', 200, 2000000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', false, true, 'SaaS businesses', 'Powering the cloud revolution', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Legal Advisors Group', 'Legal Advisors Group PLLC', 'https://legaladvisors.example.com', 2014, 'LEGAL01', 'Professional Services', 'Legal', 'SMALL', 50, 800000.00, 'USD', 'BETWEEN_100K_1M', 'ACTIVE', false, false, 'Corporate legal clients', 'Excellence in legal counsel', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Energy Solutions Ltd', 'Energy Solutions Limited', 'https://energysolutions.example.com', 2011, 'ENERGY01', 'Energy', 'Renewable', 'LARGE', 600, 6000000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', true, false, 'Commercial energy consumers', 'Leading the renewable energy transition', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Media Network Corp', 'Media Network Corporation', 'https://medianetwork.example.com', 2013, 'MEDIA01', 'Media', 'Digital Content', 'MEDIUM', 250, 2500000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', false, false, 'Content consumers', 'Creating compelling digital experiences', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Logistics Express', 'Logistics Express International', 'https://logisticsexpress.example.com', 2009, 'TRANS01', 'Transportation', 'Logistics', 'LARGE', 350, 3500000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', false, false, 'International shipping clients', 'Delivering excellence worldwide', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Food & Beverage Co', 'Food & Beverage Company LLC', 'https://foodbev.example.com', 2017, 'FOOD01', 'Food & Beverage', 'Distribution', 'MEDIUM', 180, 1800000.00, 'USD', 'BETWEEN_1M_10M', 'PROSPECT', false, false, 'Organic food retailers', 'Nourishing communities with quality products', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Insurance Partners', 'Insurance Partners Group', 'https://insurancepartners.example.com', 2007, 'INS001', 'Insurance', 'Commercial', 'LARGE', 450, 4500000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', true, false, 'Commercial insurance clients', 'Protecting businesses, securing futures', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Real Estate Ventures', 'Real Estate Ventures LLC', 'https://realestate.example.com', 2019, 'RE001', 'Real Estate', 'Development', 'MEDIUM', 120, 2200000.00, 'USD', 'BETWEEN_1M_10M', 'LEAD', false, true, 'Commercial property investors', 'Building tomorrow spaces today', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Telecom Systems Inc', 'Telecom Systems Incorporated', 'https://telecomsystems.example.com', 2006, 'TELCO01', 'Telecommunications', 'Infrastructure', 'LARGE', 550, 5500000.00, 'USD', 'BETWEEN_1M_10M', 'ACTIVE', true, false, 'Enterprise telecom clients', 'Connecting the world through innovation', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Service Packages (12 packages)
INSERT INTO services_packages (name, description, price, service_type, deliverables, estimated_hours, complexity, is_recurring, frequency, project_duration, active, created_at, updated_at) VALUES
('Social Media Marketing', 'Comprehensive social media management and growth', 2500.00, 'SOCIAL_MEDIA', 'Daily posts, engagement, analytics reporting', 40, 'MEDIUM', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SEO Optimization', 'Search engine optimization and content strategy', 3500.00, 'SEO', 'Keyword research, on-page optimization, link building', 50, 'HIGH', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Content Marketing Package', 'Blog posts, articles, and content creation', 4000.00, 'CONTENT_MARKETING', '20 blog posts, SEO optimization, distribution', 60, 'MEDIUM', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PPC Campaign Management', 'Pay-per-click advertising across platforms', 5000.00, 'PPC', 'Campaign setup, optimization, reporting', 35, 'HIGH', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Email Marketing Campaign', 'Email strategy and campaign execution', 2200.00, 'EMAIL_MARKETING', 'Template design, automation, analytics', 30, 'MEDIUM', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Analytics and Reporting', 'Comprehensive analytics and insights', 3000.00, 'ANALYTICS', 'Dashboard setup, monthly reports, recommendations', 25, 'MEDIUM', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Marketing Strategy Consulting', 'Strategic marketing planning and execution', 8000.00, 'STRATEGY', 'Market analysis, strategy document, implementation plan', 80, 'VERY_HIGH', false, 'ONE_TIME', 90, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Social Media Starter', 'Basic social media presence', 1500.00, 'SOCIAL_MEDIA', '3 posts per week, basic engagement', 20, 'LOW', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Content Creation Basic', 'Essential content creation services', 1800.00, 'CONTENT_MARKETING', '8 blog posts per month', 40, 'LOW', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PPC Starter Package', 'Entry-level PPC campaign', 2500.00, 'PPC', 'Google Ads setup and management', 25, 'MEDIUM', true, 'MONTHLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Email Automation Setup', 'Marketing automation implementation', 4500.00, 'EMAIL_MARKETING', 'Platform setup, workflow automation, training', 55, 'HIGH', false, 'ONE_TIME', 60, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Quarterly Marketing Review', 'Comprehensive performance analysis', 3500.00, 'ANALYTICS', 'Data analysis, performance report, recommendations', 45, 'HIGH', true, 'QUARTERLY', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Opportunities (15 opportunities)
INSERT INTO opportunities (customer_company_id, title, amount, stage, expected_close_date, created_at, updated_at) VALUES
(1, 'TechCorp Digital Transformation', 150000.00, 'NEGOTIATION', '2026-03-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Global Finance Marketing Campaign', 95000.00, 'PROPOSAL', '2026-02-28', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'HealthCare Plus Services', 75000.00, 'QUALIFICATION', '2026-04-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Retail Masters E-commerce', 45000.00, 'PROSPECTING', '2026-05-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'EduTech Content Strategy', 38000.00, 'PROPOSAL', '2026-03-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Manufacturing Pro Analytics', 125000.00, 'NEGOTIATION', '2026-02-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Cloud Services Marketing', 85000.00, 'CLOSED_WON', '2026-01-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Legal Advisors Digital Presence', 28000.00, 'QUALIFICATION', '2026-04-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Energy Solutions Campaign', 110000.00, 'PROPOSAL', '2026-03-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Media Network Content', 68000.00, 'NEGOTIATION', '2026-02-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Logistics Express Automation', 92000.00, 'CLOSED_WON', '2026-01-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'Food & Beverage Social Media', 42000.00, 'QUALIFICATION', '2026-04-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'Insurance Partners Marketing', 135000.00, 'PROPOSAL', '2026-03-05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'Real Estate Digital Strategy', 58000.00, 'QUALIFICATION', '2026-05-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'Telecom Systems Enterprise', 180000.00, 'NEGOTIATION', '2026-02-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Deals (10 deals from closed opportunities)
INSERT INTO deals (deal_status, final_amount, start_date, end_date, campaign_manager_id, deliverables, terms, customer_id, opportunity_id, created_at, updated_at) VALUES
('SIGNED', 85000.00, '2026-01-20', '2027-01-20', 3, 'Complete marketing services package', '12-month contract with monthly billing', 7, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SIGNED', 92000.00, '2026-01-10', '2027-01-10', 3, 'Marketing automation and CRM integration', 'Annual contract, quarterly reviews', 11, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IN_NEGOTIATION', 50000.00, '2026-02-15', '2026-08-15', 2, 'Strategic consulting engagement', 'Project-based with milestones', 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DRAFT', 48000.00, '2026-03-30', '2027-03-30', 3, 'Second quarter marketing initiatives', 'Quarterly renewable contract', 2, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DRAFT', 35000.00, '2026-04-15', '2026-10-15', 7, 'Technical content creation', '6-month engagement', 6, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IN_NEGOTIATION', 42000.00, '2026-03-20', '2027-03-20', 4, 'Social media management package', 'Annual contract with monthly deliverables', 9, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IN_NEGOTIATION', 65000.00, '2026-02-28', '2027-02-28', 10, 'Business analytics platform', '12-month subscription', 13, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DRAFT', 55000.00, '2026-04-30', '2027-04-30', 3, 'Annual content creation services', 'Renewable annual contract', 15, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DRAFT', 38000.00, '2026-03-15', '2026-09-15', 4, 'CRM system setup and training', '6-month implementation', 10, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DRAFT', 32000.00, '2026-05-10', '2026-11-10', 4, 'Q2 marketing campaign', 'Campaign-based project', 14, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Deal Service Packages (linking deals to service packages)
INSERT INTO deal_service_packages (deal_id, service_package_id, added_at, quantity, unit_price) VALUES
(1, 2, CURRENT_TIMESTAMP, 1, 3500.00),
(1, 4, CURRENT_TIMESTAMP, 1, 5000.00),
(2, 4, CURRENT_TIMESTAMP, 1, 5000.00),
(2, 6, CURRENT_TIMESTAMP, 1, 3000.00),
(3, 7, CURRENT_TIMESTAMP, 1, 8000.00),
(4, 3, CURRENT_TIMESTAMP, 1, 4000.00),
(5, 9, CURRENT_TIMESTAMP, 1, 1800.00),
(6, 1, CURRENT_TIMESTAMP, 1, 2500.00),
(7, 6, CURRENT_TIMESTAMP, 1, 3000.00),
(8, 3, CURRENT_TIMESTAMP, 1, 4000.00);

-- Insert Quotes (12 quotes)
INSERT INTO quotes (customer_company_id, opportunity_id, valid_until, status, created_at, updated_at) VALUES
(1, 1, '2026-02-15', 'SENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, '2026-02-28', 'SENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, '2026-03-15', 'DRAFT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, '2026-04-01', 'SENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 6, '2026-02-10', 'ACCEPTED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 9, '2026-03-01', 'SENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 10, '2026-02-20', 'SENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 13, '2026-03-10', 'SENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 15, '2026-02-25', 'SENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, '2026-04-05', 'DRAFT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 8, '2026-04-10', 'SENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 14, '2026-05-05', 'DRAFT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Quote Items
INSERT INTO quote_items (quote_id, service_package_id, unit_price, total, discount_percentage, discount, created_at, updated_at) VALUES
(1, 2, 3500.00, 3500.00, 0, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 4, 5000.00, 5000.00, 0, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 2500.00, 2500.00, 5, 125.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 5, 2200.00, 2200.00, 5, 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 6, 3000.00, 3000.00, 0, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 8, 1500.00, 1500.00, 0, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 6, 3000.00, 3000.00, 10, 300.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 2, 3500.00, 3500.00, 10, 350.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 3, 4000.00, 4000.00, 5, 200.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 9, 1800.00, 1800.00, 0, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Tasks (15 tasks)
INSERT INTO tasks (title, description, due_date, status, priority, customer_id, opportunity_id, assigned_to_user_id, created_at, updated_at) VALUES
('Follow up on TechCorp proposal', 'Schedule meeting to discuss proposal details', '2026-01-15', 'IN_PROGRESS', 'HIGH', 1, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Prepare presentation for Global Finance', 'Create custom presentation deck', '2026-01-18', 'PENDING', 'HIGH', 2, 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cloud Services onboarding', 'Schedule kickoff meeting', '2026-01-22', 'COMPLETED', 'HIGH', 7, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Logistics Express implementation', 'Begin system setup', '2026-01-15', 'IN_PROGRESS', 'HIGH', 11, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Manufacturing Pro contract review', 'Review legal terms with customer', '2026-01-20', 'PENDING', 'MEDIUM', 6, 6, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Energy Solutions budget approval', 'Confirm budget with finance team', '2026-01-17', 'IN_PROGRESS', 'HIGH', 9, 9, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TechCorp quarterly review', 'Prepare QBR materials', '2026-02-01', 'PENDING', 'MEDIUM', 1, NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Insurance Partners demo', 'Schedule product demonstration', '2026-01-19', 'PENDING', 'HIGH', 13, 13, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Telecom Systems executive meeting', 'Arrange C-level presentation', '2026-01-16', 'IN_PROGRESS', 'HIGH', 15, 15, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TechCorp consulting kickoff', 'Prepare consulting engagement plan', '2026-02-20', 'PENDING', 'MEDIUM', 1, NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cloud Services check-in', 'Monthly account health check', '2026-02-15', 'PENDING', 'LOW', 7, NULL, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Media Network pricing discussion', 'Finalize pricing structure', '2026-01-14', 'IN_PROGRESS', 'HIGH', 10, 10, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('HealthCare Plus requirements gathering', 'Collect detailed requirements', '2026-01-25', 'PENDING', 'MEDIUM', 3, 3, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Telecom Systems account planning', 'Develop account growth strategy', '2026-02-10', 'PENDING', 'MEDIUM', 15, NULL, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Insurance Partners proposal refinement', 'Update proposal based on feedback', '2026-01-13', 'IN_PROGRESS', 'HIGH', 13, NULL, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Interactions (20 interactions)
INSERT INTO interactions (customer_id, type, date_time, description, outcome, feedback_type, channel_preference, created_at, updated_at) VALUES
(1, 'CALL', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Discussed proposal details and timeline with decision maker', 'Positive response, requested minor changes', 'POSITIVE', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'EMAIL', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Sent detailed proposal document with pricing breakdown', 'Proposal received, under review', 'POSITIVE', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'MEETING', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Initial discovery and requirements gathering session', 'Strong interest, good alignment', 'POSITIVE', 'IN_PERSON', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'CALL', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Scheduled implementation kickoff for signed contract', 'Onboarding confirmed', 'POSITIVE', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'MEETING', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Discussed technical requirements and timeline', 'Technical alignment achieved', 'POSITIVE', 'VIDEO_CALL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'CALL', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Clarified contract terms and service scope', 'Contract review scheduled', 'POSITIVE', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'EMAIL', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Sent comprehensive campaign strategy document', 'Document received, reviewing with team', 'POSITIVE', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'FOLLOW_UP', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Account notes: discussed expansion opportunities for next quarter', 'Identified upsell potential', 'NEUTRAL', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'CALL', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Customer requested additional information about analytics platform', 'Sent additional materials', 'POSITIVE', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'PRESENTATION', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Preparing for C-level executive presentation', 'Meeting scheduled', 'POSITIVE', 'IN_PERSON', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'CALL', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Negotiating final pricing and contract terms', 'Price negotiations ongoing', 'NEUTRAL', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'EMAIL', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Sent requirements questionnaire for CRM implementation', 'Questionnaire completed', 'POSITIVE', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'MEETING', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Project scope definition for consulting engagement', 'Meeting scheduled', 'POSITIVE', 'IN_PERSON', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'EMAIL', CURRENT_TIMESTAMP - INTERVAL '7 days', 'Sent monthly performance metrics and recommendations', 'Positive feedback on results', 'POSITIVE', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'CALL', CURRENT_TIMESTAMP - INTERVAL '6 days', 'Initial needs assessment and discovery call', 'Qualified opportunity', 'NEUTRAL', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'EMAIL', CURRENT_TIMESTAMP - INTERVAL '3 days', 'Responded to customer questions about proposal', 'Clarifications provided', 'POSITIVE', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'CALL', CURRENT_TIMESTAMP - INTERVAL '8 days', 'Introduction and initial discovery', 'Moved to qualification stage', 'POSITIVE', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Strategy discussion with internal team about account approach', 'Action plan developed', 'NEUTRAL', 'IN_PERSON', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'CALL', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Technical support inquiry about platform features', 'Issue resolved successfully', 'POSITIVE', 'PHONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'EMAIL', CURRENT_TIMESTAMP - INTERVAL '5 days', 'Sent social media management proposal with case studies', 'Proposal under review', 'POSITIVE', 'EMAIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
