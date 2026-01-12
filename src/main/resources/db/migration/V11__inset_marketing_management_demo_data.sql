-- ============================================================================
-- MARKETING MANAGEMENT DEMO DATA INSERTION
-- ============================================================================
-- Purpose: Insert sample data for marketing modules (campaigns, channels, activities, etc.)
-- Dependencies: V10 (marketing management tables must exist)
-- ============================================================================

-- Insert Marketing Channels (10 channels)
INSERT INTO marketing_channels (name, channel_type, description, default_cost_per_click, default_cost_per_impression, is_active, created_at, updated_at, created_by, updated_by) VALUES
('Google Ads - Search', 'PAID', 'Google search advertising campaigns', 2.50, 0.0150, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('Facebook Ads', 'SOCIAL', 'Facebook and Instagram advertising', 1.75, 0.0120, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('LinkedIn Ads', 'SOCIAL', 'LinkedIn professional network advertising', 5.50, 0.0280, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('Google Display Network', 'PAID', 'Display advertising across Google network', 0.85, 0.0080, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('Email Marketing', 'EMAIL', 'Direct email campaigns and newsletters', NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('Organic Search', 'ORGANIC', 'SEO and organic search traffic', NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('Twitter Ads', 'SOCIAL', 'Twitter/X advertising platform', 1.25, 0.0095, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('YouTube Ads', 'PAID', 'YouTube video advertising', 0.15, 0.0200, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('Referral Program', 'REFERRAL', 'Customer and partner referral programs', NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
('Direct Traffic', 'DIRECT', 'Direct website visits and branded searches', NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);

-- Insert Marketing Campaigns (12 campaigns)
INSERT INTO marketing_campaigns (name, description, campaign_type, status, total_budget, spent_amount, start_date, end_date, primary_goal, primary_channel_id, created_at, updated_at, created_by, updated_by) VALUES
('Q1 2026 Lead Generation', 'Multi-channel lead generation campaign for Q1', 'CONVERSION', 'ACTIVE', 50000.00, 15000.00, '2026-01-01', '2026-03-31', 'Generate 500 qualified leads', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
('Brand Awareness Spring 2026', 'Brand awareness campaign targeting new markets', 'AWARENESS', 'ACTIVE', 75000.00, 22000.00, '2026-02-01', '2026-04-30', 'Increase brand recognition by 40%', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
('Customer Retention Program', 'Ongoing customer engagement and retention', 'RETENTION', 'ACTIVE', 30000.00, 8500.00, '2026-01-15', '2026-12-31', 'Reduce churn rate to under 5%', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
('Product Launch - Enterprise Suite', 'Launch campaign for new enterprise product', 'CONVERSION', 'PLANNED', 100000.00, 0.00, '2026-03-01', '2026-05-31', 'Acquire 100 enterprise customers', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
('Holiday Season Promotion', 'End of year promotional campaign', 'CONVERSION', 'PLANNED', 60000.00, 0.00, '2026-11-01', '2026-12-31', 'Drive 25% revenue increase', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
('Social Media Engagement Drive', 'Increase social media presence and engagement', 'AWARENESS', 'ACTIVE', 25000.00, 12000.00, '2026-01-01', '2026-06-30', 'Grow social following by 50%', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
('Webinar Series - Thought Leadership', 'Educational webinar series for prospects', 'AWARENESS', 'ACTIVE', 15000.00, 4500.00, '2026-01-15', '2026-06-15', 'Host 12 webinars with 200+ attendees each', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
('Partner Program Launch', 'Launch new partner referral program', 'CONVERSION', 'ACTIVE', 40000.00, 10000.00, '2026-02-01', '2026-07-31', 'Onboard 50 active partners', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
('Content Marketing Initiative', 'SEO-focused content creation campaign', 'AWARENESS', 'ACTIVE', 35000.00, 15000.00, '2026-01-01', '2026-12-31', 'Publish 100 high-quality articles', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
('Reactivation Campaign Q1', 'Re-engage dormant customers', 'REACTIVATION', 'ACTIVE', 20000.00, 6000.00, '2026-01-10', '2026-03-31', 'Reactivate 200 inactive accounts', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
('Video Marketing Campaign', 'YouTube and social video content series', 'AWARENESS', 'PLANNED', 45000.00, 0.00, '2026-03-15', '2026-09-15', 'Create 50 marketing videos', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
('Industry Conference Sponsorship', 'Sponsorship and presence at major industry events', 'BRANDING', 'PLANNED', 80000.00, 0.00, '2026-04-01', '2026-10-31', 'Sponsor 5 major conferences', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5);

-- Insert Campaign Targets (20 targets across campaigns)
INSERT INTO campaign_targets (campaign_id, metric_name, metric_type, target_value, current_value, measurement_unit, status, achievement_percentage, created_at, updated_at, created_by, updated_by) VALUES
(1, 'Qualified Leads Generated', 'LEAD_GENERATION', 500.00, 185.00, 'leads', 'PENDING', 37.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'Cost Per Lead', 'REVENUE', 100.00, 81.08, 'USD', 'PENDING', 81.08, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'Campaign Revenue', 'REVENUE', 150000.00, 42000.00, 'USD', 'PENDING', 28.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Brand Awareness Increase', 'BRAND_AWARENESS', 40.00, 12.00, 'percentage', 'PENDING', 30.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Social Media Impressions', 'REACH', 5000000.00, 1850000.00, 'impressions', 'PENDING', 37.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Website Traffic Increase', 'REACH', 50000.00, 22000.00, 'visits', 'PENDING', 44.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(3, 'Customer Retention Rate', 'ENGAGEMENT', 95.00, 92.50, 'percentage', 'PENDING', 97.37, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(3, 'Email Open Rate', 'ENGAGEMENT', 35.00, 38.50, 'percentage', 'ACHIEVED', 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(3, 'Customer Satisfaction Score', 'ENGAGEMENT', 4.50, 4.65, 'score', 'ACHIEVED', 103.33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Media Followers', 'REACH', 50000.00, 24000.00, 'followers', 'PENDING', 48.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Engagement Rate', 'ENGAGEMENT', 5.00, 6.20, 'percentage', 'ACHIEVED', 124.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Shares', 'ENGAGEMENT', 10000.00, 5500.00, 'shares', 'PENDING', 55.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(7, 'Webinars Hosted', 'ENGAGEMENT', 12.00, 3.00, 'events', 'PENDING', 25.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 'Average Attendees', 'REACH', 200.00, 245.00, 'attendees', 'ACHIEVED', 122.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 'Lead Conversion Rate', 'CONVERSION', 15.00, 18.50, 'percentage', 'ACHIEVED', 123.33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(8, 'Partners Onboarded', 'CUSTOMER_ACQUISITION', 50.00, 18.00, 'partners', 'PENDING', 36.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(8, 'Partner Generated Revenue', 'REVENUE', 200000.00, 45000.00, 'USD', 'PENDING', 22.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(9, 'Articles Published', 'ENGAGEMENT', 100.00, 42.00, 'articles', 'PENDING', 42.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(9, 'Organic Traffic Growth', 'REACH', 100000.00, 38000.00, 'visits', 'PENDING', 38.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(10, 'Reactivated Customers', 'CUSTOMER_ACQUISITION', 200.00, 65.00, 'customers', 'PENDING', 32.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6);

-- Insert Marketing Assets (15 assets)
INSERT INTO marketing_assets (campaign_id, asset_type, name, description, url, file_size_kb, mime_type, views_count, clicks_count, conversions_count, status, is_primary_asset, created_at, updated_at, created_by, updated_by) VALUES
(1, 'LANDING_PAGE', 'Q1 Lead Gen Landing Page', 'Main landing page for Q1 campaign', 'https://example.com/landing/q1-leads', NULL, 'text/html', 15420, 2840, 185, 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'AD_CREATIVE', 'Google Search Ad - Main', 'Primary search ad creative', 'https://assets.example.com/ads/search-main.jpg', 245, 'image/jpeg', 45000, 2840, 185, 'ACTIVE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'VIDEO', 'Brand Story Video 2026', 'Company brand story video', 'https://youtube.com/watch?v=example', 52400, 'video/mp4', 125000, 8500, 420, 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'AD_CREATIVE', 'Display Ad - Brand Awareness', 'Banner ad for display network', 'https://assets.example.com/display/brand-banner.png', 156, 'image/png', 1850000, 12500, 850, 'ACTIVE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(3, 'EMAIL_TEMPLATE', 'Customer Newsletter Template', 'Monthly newsletter design', 'https://assets.example.com/email/newsletter.html', 89, 'text/html', 8500, 3200, 240, 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'SOCIAL_POST', 'LinkedIn Product Announcement', 'New product announcement post', 'https://linkedin.com/posts/example', NULL, 'text/html', 24000, 1850, 125, 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'IMAGE', 'Social Media Graphic Pack', 'Set of social graphics', 'https://assets.example.com/social/graphic-pack.zip', 4500, 'application/zip', 12000, 0, 0, 'ACTIVE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(7, 'LANDING_PAGE', 'Webinar Registration Page', 'Webinar series registration', 'https://example.com/webinars/register', NULL, 'text/html', 4500, 980, 245, 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 'WHITEPAPER', 'Industry Trends Report 2026', 'Thought leadership whitepaper', 'https://example.com/resources/trends-2026.pdf', 2840, 'application/pdf', 1850, 420, 95, 'ACTIVE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(8, 'DOCUMENT', 'Partner Program Guide', 'Complete partner onboarding guide', 'https://example.com/partners/guide.pdf', 1250, 'application/pdf', 850, 180, 45, 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(9, 'LANDING_PAGE', 'Content Resource Hub', 'SEO-optimized content library', 'https://example.com/resources', NULL, 'text/html', 38000, 12500, 850, 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(9, 'DOCUMENT', 'SEO Best Practices Guide', 'Comprehensive SEO guide', 'https://example.com/guides/seo.pdf', 1840, 'application/pdf', 5600, 980, 125, 'ACTIVE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(1, 'EMAIL_TEMPLATE', 'Lead Nurture Email Series', 'Automated lead nurture sequence', 'https://assets.example.com/email/nurture.html', 65, 'text/html', 2840, 1250, 85, 'ACTIVE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'AD_CREATIVE', 'Facebook Video Ad', 'Short-form video ad for Facebook', 'https://assets.example.com/video/fb-ad.mp4', 8400, 'video/mp4', 850000, 25000, 1200, 'ACTIVE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(10, 'EMAIL_TEMPLATE', 'Reactivation Email Campaign', 'Win-back email template', 'https://assets.example.com/email/reactivation.html', 72, 'text/html', 6000, 1850, 65, 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6);

-- Insert AB Tests (10 tests)
INSERT INTO ab_tests (campaign_id, test_name, hypothesis, test_type, primary_metric, confidence_level, required_sample_size, control_variant, treatment_variants, winning_variant, statistical_significance, is_completed, start_date, end_date, created_at, updated_at, created_by, updated_by) VALUES
(1, 'Landing Page Headline Test', 'New headline will increase conversion rate', 'SPLIT_URL', 'Conversion Rate', 0.95, 1000, 'Control - Original Headline', '{"variant_a": "New Benefit-Focused Headline", "variant_b": "Question-Based Headline"}', 'variant_a', 0.972, true, '2026-01-05', '2026-01-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'CTA Button Color Test', 'Orange button will outperform blue button', 'MULTIVARIATE', 'Click-Through Rate', 0.95, 800, 'Blue Button', '{"variant_a": "Orange Button", "variant_b": "Green Button", "variant_c": "Red Button"}', 'variant_a', 0.985, true, '2026-01-10', '2026-01-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Video Length Test', 'Shorter video will have better engagement', 'SPLIT_URL', 'View Completion Rate', 0.90, 2000, '60 Second Video', '{"variant_a": "30 Second Video", "variant_b": "90 Second Video"}', 'variant_a', 0.923, true, '2026-02-01', '2026-02-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(3, 'Email Subject Line Test', 'Personalized subject line increases open rate', 'SPLIT_URL', 'Email Open Rate', 0.95, 5000, 'Generic Subject', '{"variant_a": "Personalized with Name", "variant_b": "Question Format"}', 'variant_a', 0.968, true, '2026-01-15', '2026-02-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Ad Creative Test', 'Image-based ad outperforms text-only', 'MULTIVARIATE', 'Engagement Rate', 0.90, 1500, 'Text Only Ad', '{"variant_a": "Image with Text", "variant_b": "Video Ad", "variant_c": "Carousel Ad"}', 'variant_c', 0.945, true, '2026-01-20', '2026-02-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(7, 'Webinar Time Slot Test', 'Afternoon webinars have better attendance', 'SPLIT_URL', 'Attendance Rate', 0.90, 600, 'Morning 10 AM', '{"variant_a": "Afternoon 2 PM", "variant_b": "Evening 6 PM"}', 'variant_a', 0.912, true, '2026-01-18', '2026-02-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(1, 'Form Field Optimization', 'Fewer form fields increase conversion', 'MULTIVARIATE', 'Form Completion Rate', 0.95, 1200, '8 Fields', '{"variant_a": "5 Fields", "variant_b": "3 Fields", "variant_c": "Progressive Form"}', NULL, NULL, false, '2026-02-01', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(9, 'Article Format Test', 'List-based articles get more engagement', 'SPLIT_URL', 'Time on Page', 0.90, 800, 'Standard Article', '{"variant_a": "List Format", "variant_b": "Q&A Format"}', NULL, NULL, false, '2026-01-25', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(2, 'Display Ad Size Test', 'Larger ad format drives more clicks', 'MULTIVARIATE', 'Click-Through Rate', 0.95, 10000, 'Medium Rectangle 300x250', '{"variant_a": "Large Rectangle 336x280", "variant_b": "Leaderboard 728x90", "variant_c": "Wide Skyscraper 160x600"}', NULL, NULL, false, '2026-02-10', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(10, 'Reactivation Offer Test', 'Discount offer outperforms feature highlight', 'SPLIT_URL', 'Reactivation Rate', 0.95, 400, 'Feature Highlight', '{"variant_a": "20% Discount", "variant_b": "Free Month Trial"}', NULL, NULL, false, '2026-01-15', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6);

-- Insert Campaign Activities (20 activities)
INSERT INTO campaign_activities (campaign_id, name, description, activity_type, status, planned_start_date, planned_end_date, actual_start_date, actual_end_date, planned_cost, actual_cost, assigned_to_user_id, delivery_channel, success_criteria, created_at, updated_at, created_by, updated_by) VALUES
(1, 'Google Ads Setup', 'Configure and launch Google Ads campaigns', 'AD_SETUP', 'COMPLETED', '2026-01-01', '2026-01-05', '2026-01-01', '2026-01-04', 2000.00, 1850.00, 5, 'Google Ads', 'Campaigns live with approved ads', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'Landing Page Development', 'Design and develop campaign landing pages', 'CONTENT_CREATION', 'COMPLETED', '2025-12-20', '2026-01-03', '2025-12-20', '2026-01-02', 5000.00, 4800.00, 9, 'Website', 'Landing page live with conversion tracking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'Email Nurture Campaign', 'Setup automated lead nurture emails', 'EMAIL_BLAST', 'IN_PROGRESS', '2026-01-10', '2026-03-31', '2026-01-10', NULL, 3000.00, 1200.00, 6, 'Email Marketing Platform', '35% open rate, 5% conversion', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Brand Video Production', 'Produce brand awareness video content', 'CONTENT_CREATION', 'COMPLETED', '2025-12-01', '2026-01-15', '2025-12-01', '2026-01-12', 15000.00, 14200.00, 9, 'YouTube', 'Video published with 100K+ views', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Display Network Campaign', 'Launch display advertising campaign', 'AD_SETUP', 'IN_PROGRESS', '2026-02-01', '2026-04-30', '2026-02-01', NULL, 20000.00, 8000.00, 5, 'Google Display Network', '5M impressions, 0.5% CTR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(3, 'Monthly Newsletter Creation', 'Create and send monthly newsletters', 'EMAIL_BLAST', 'IN_PROGRESS', '2026-01-15', '2026-12-31', '2026-01-15', NULL, 12000.00, 3400.00, 6, 'Email Marketing', '40% open rate, 10% click rate', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Media Content Calendar', 'Plan and create social media content', 'SOCIAL_POST', 'IN_PROGRESS', '2026-01-01', '2026-06-30', '2026-01-01', NULL, 8000.00, 4200.00, 6, 'Social Media Platforms', 'Daily posts, 5% engagement rate', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'LinkedIn Influencer Outreach', 'Partner with industry influencers', 'SOCIAL_POST', 'PLANNED', '2026-02-15', '2026-06-30', NULL, NULL, 5000.00, NULL, 6, 'LinkedIn', '10 influencer partnerships', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(7, 'Webinar 1 - Industry Trends', 'Host thought leadership webinar', 'WEBINAR', 'COMPLETED', '2026-01-15', '2026-01-15 23:59:59', '2026-01-15', '2026-01-15 23:59:59', 1500.00, 1450.00, 9, 'Webinar Platform', '200+ attendees, 80% satisfaction', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 'Webinar 2 - Best Practices', 'Educational webinar on best practices', 'WEBINAR', 'COMPLETED', '2026-02-05', '2026-02-05 23:59:59', '2026-02-05', '2026-02-05 23:59:59', 1500.00, 1420.00, 9, 'Webinar Platform', '250+ attendees, 85% satisfaction', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 'Webinar 3 - Case Studies', 'Customer success stories webinar', 'WEBINAR', 'COMPLETED', '2026-02-20', '2026-02-20 23:59:59', '2026-02-20', '2026-02-20 23:59:59', 1500.00, 1380.00, 9, 'Webinar Platform', '200+ attendees, 90% satisfaction', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(8, 'Partner Portal Development', 'Build partner resource portal', 'CONTENT_CREATION', 'IN_PROGRESS', '2026-02-01', '2026-03-15', '2026-02-01', NULL, 8000.00, 3200.00, 5, 'Website', 'Portal live with all resources', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(8, 'Partner Training Webinars', 'Conduct partner onboarding training', 'WEBINAR', 'IN_PROGRESS', '2026-02-15', '2026-07-31', '2026-02-15', NULL, 6000.00, 1800.00, 9, 'Webinar Platform', 'Train all new partners', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(9, 'SEO Content Creation - Batch 1', 'Create 25 SEO-optimized articles', 'CONTENT_CREATION', 'COMPLETED', '2026-01-01', '2026-02-15', '2026-01-01', '2026-02-10', 10000.00, 9500.00, 9, 'Blog', '25 articles published, ranking for keywords', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(9, 'SEO Content Creation - Batch 2', 'Create 25 more SEO articles', 'CONTENT_CREATION', 'IN_PROGRESS', '2026-02-15', '2026-04-15', '2026-02-15', NULL, 10000.00, 4200.00, 9, 'Blog', '25 articles published', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(10, 'Reactivation Email Series', 'Create and send reactivation emails', 'EMAIL_BLAST', 'IN_PROGRESS', '2026-01-10', '2026-03-31', '2026-01-10', NULL, 8000.00, 2800.00, 6, 'Email Marketing', '10% reactivation rate', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(10, 'Special Offer Landing Page', 'Create dedicated reactivation offer page', 'CONTENT_CREATION', 'COMPLETED', '2026-01-05', '2026-01-12', '2026-01-05', '2026-01-10', 2500.00, 2200.00, 9, 'Website', 'Landing page live and converting', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(1, 'Campaign Performance Analysis', 'Weekly analysis and optimization', 'ANALYSIS', 'IN_PROGRESS', '2026-01-01', '2026-03-31', '2026-01-01', NULL, 4000.00, 1800.00, 5, 'Analytics Platform', 'Weekly reports with recommendations', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Creative Asset Development', 'Design campaign creative assets', 'CONTENT_CREATION', 'IN_PROGRESS', '2026-01-15', '2026-04-30', '2026-01-15', NULL, 12000.00, 4800.00, 9, 'Design Tools', '50+ unique creative assets', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(9, 'Technical SEO Audit', 'Comprehensive website SEO audit', 'OPTIMIZATION', 'COMPLETED', '2026-01-01', '2026-01-31', '2026-01-01', '2026-01-28', 5000.00, 4800.00, 9, 'SEO Tools', 'Audit complete with action plan', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9);

-- Insert Campaign Attribution (15 records linking deals to campaigns)
INSERT INTO campaign_attribution (deal_id, campaign_id, attribution_model, attribution_percentage, attributed_revenue, touch_count, first_touch_weight, last_touch_weight, linear_weight, created_at, updated_at, created_by, updated_by) VALUES
(1, 1, 'LINEAR', 40.00, 34000.00, 3, 0.00, 0.00, 1.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 6, 'LINEAR', 30.00, 25500.00, 2, 0.00, 0.00, 1.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 7, 'LINEAR', 30.00, 25500.00, 2, 0.00, 0.00, 1.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 1, 'FIRST_TOUCH', 60.00, 55200.00, 4, 1.00, 0.00, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 9, 'FIRST_TOUCH', 40.00, 36800.00, 3, 0.00, 0.00, 0.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(3, 7, 'LAST_TOUCH', 70.00, 35000.00, 2, 0.00, 1.00, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(3, 9, 'LAST_TOUCH', 30.00, 15000.00, 1, 0.00, 0.00, 0.30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(4, 2, 'FIRST_TOUCH', 100.00, 48000.00, 2, 1.00, 0.00, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(5, 9, 'LINEAR', 80.00, 28000.00, 3, 0.00, 0.00, 1.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(5, 1, 'LINEAR', 20.00, 7000.00, 1, 0.00, 0.00, 0.20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(6, 6, 'LAST_TOUCH', 90.00, 37800.00, 4, 0.00, 1.00, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(7, 9, 'TIME_DECAY', 60.00, 39000.00, 5, 0.10, 0.50, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 1, 'TIME_DECAY', 40.00, 26000.00, 3, 0.20, 0.40, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(9, 10, 'FIRST_TOUCH', 75.00, 28500.00, 2, 1.00, 0.00, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(9, 3, 'FIRST_TOUCH', 25.00, 9500.00, 1, 0.00, 0.25, 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6);

-- Insert Campaign Interactions (25 interactions)
INSERT INTO campaign_interactions (campaign_id, customer_id, interaction_type, interaction_date, channel_id, utm_source, utm_medium, utm_campaign, device_type, device_os, browser, country_code, city, deal_id, conversion_value, is_conversion, landing_page_url, created_at, updated_at) VALUES
(1, 1, 'AD_CLICK', CURRENT_TIMESTAMP - INTERVAL '15 days', 1, 'google', 'cpc', 'q1-lead-gen', 'Desktop', 'Windows', 'Chrome', 'US', 'San Francisco', 1, 85000.00, true, 'https://example.com/landing/q1-leads', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 2, 'AD_CLICK', CURRENT_TIMESTAMP - INTERVAL '12 days', 1, 'google', 'cpc', 'q1-lead-gen', 'Mobile', 'iOS', 'Safari', 'US', 'New York', NULL, NULL, false, 'https://example.com/landing/q1-leads', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 3, 'LANDING_PAGE_VISIT', CURRENT_TIMESTAMP - INTERVAL '10 days', 6, 'google', 'organic', '', 'Desktop', 'macOS', 'Chrome', 'US', 'Chicago', NULL, NULL, false, 'https://example.com/landing/q1-leads', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 4, 'FORM_SUBMIT', CURRENT_TIMESTAMP - INTERVAL '8 days', 1, 'google', 'cpc', 'q1-lead-gen', 'Desktop', 'Windows', 'Firefox', 'US', 'Los Angeles', NULL, NULL, false, 'https://example.com/landing/q1-leads', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 5, 'AD_VIEW', CURRENT_TIMESTAMP - INTERVAL '20 days', 4, 'google', 'display', 'brand-awareness', 'Mobile', 'Android', 'Chrome', 'US', 'Boston', NULL, NULL, false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 6, 'AD_CLICK', CURRENT_TIMESTAMP - INTERVAL '18 days', 4, 'google', 'display', 'brand-awareness', 'Desktop', 'Windows', 'Edge', 'US', 'Detroit', NULL, NULL, false, 'https://example.com/brand-story', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 7, 'LANDING_PAGE_VISIT', CURRENT_TIMESTAMP - INTERVAL '16 days', 2, 'facebook', 'social', 'brand-awareness', 'Mobile', 'iOS', 'Safari', 'US', 'Seattle', NULL, NULL, false, 'https://example.com/brand-story', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, 'EMAIL_OPEN', CURRENT_TIMESTAMP - INTERVAL '5 days', 5, 'email', 'newsletter', 'retention', 'Desktop', 'Windows', 'Outlook', 'US', 'San Francisco', NULL, NULL, false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 7, 'EMAIL_CLICK', CURRENT_TIMESTAMP - INTERVAL '4 days', 5, 'email', 'newsletter', 'retention', 'Mobile', 'iOS', 'Mail', 'US', 'Seattle', NULL, NULL, false, 'https://example.com/resources', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 11, 'EMAIL_OPEN', CURRENT_TIMESTAMP - INTERVAL '3 days', 5, 'email', 'newsletter', 'retention', 'Desktop', 'macOS', 'Mail', 'US', 'Memphis', NULL, NULL, false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 8, 'SOCIAL_ENGAGEMENT', CURRENT_TIMESTAMP - INTERVAL '7 days', 2, 'facebook', 'social', 'social-engagement', 'Mobile', 'iOS', 'Facebook', 'US', 'Washington', NULL, NULL, false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 9, 'SOCIAL_ENGAGEMENT', CURRENT_TIMESTAMP - INTERVAL '6 days', 3, 'linkedin', 'social', 'social-engagement', 'Desktop', 'Windows', 'Chrome', 'US', 'Houston', NULL, NULL, false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 10, 'AD_CLICK', CURRENT_TIMESTAMP - INTERVAL '5 days', 2, 'facebook', 'social', 'social-engagement', 'Mobile', 'Android', 'Chrome', 'US', 'Los Angeles', 6, 42000.00, true, 'https://example.com/products', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 11, 'WEBINAR_REGISTRATION', CURRENT_TIMESTAMP - INTERVAL '14 days', 5, 'email', 'email', 'webinar-series', 'Desktop', 'Windows', 'Chrome', 'US', 'Memphis', NULL, NULL, false, 'https://example.com/webinars/register', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 12, 'WEBINAR_REGISTRATION', CURRENT_TIMESTAMP - INTERVAL '12 days', 6, 'google', 'organic', '', 'Desktop', 'macOS', 'Safari', 'US', 'Portland', NULL, NULL, false, 'https://example.com/webinars/register', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 1, 'WHITEPAPER_DOWNLOAD', CURRENT_TIMESTAMP - INTERVAL '10 days', 3, 'linkedin', 'social', 'webinar-series', 'Desktop', 'Windows', 'Chrome', 'US', 'San Francisco', NULL, NULL, false, 'https://example.com/resources/trends-2026.pdf', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 13, 'LANDING_PAGE_VISIT', CURRENT_TIMESTAMP - INTERVAL '9 days', 9, 'referral', 'partner', 'partner-program', 'Desktop', 'macOS', 'Chrome', 'US', 'Hartford', NULL, NULL, false, 'https://example.com/partners', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 14, 'FORM_SUBMIT', CURRENT_TIMESTAMP - INTERVAL '8 days', 9, 'referral', 'partner', 'partner-program', 'Desktop', 'Windows', 'Edge', 'US', 'Miami', NULL, NULL, false, 'https://example.com/partners/apply', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 3, 'LANDING_PAGE_VISIT', CURRENT_TIMESTAMP - INTERVAL '11 days', 6, 'google', 'organic', '', 'Desktop', 'Windows', 'Chrome', 'US', 'Chicago', NULL, NULL, false, 'https://example.com/resources', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 5, 'LANDING_PAGE_VISIT', CURRENT_TIMESTAMP - INTERVAL '9 days', 6, 'google', 'organic', '', 'Desktop', 'macOS', 'Safari', 'US', 'Boston', 5, 35000.00, true, 'https://example.com/blog/seo-guide', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 15, 'EMAIL_OPEN', CURRENT_TIMESTAMP - INTERVAL '6 days', 5, 'email', 'campaign', 'reactivation', 'Mobile', 'iOS', 'Mail', 'US', 'Atlanta', NULL, NULL, false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 8, 'EMAIL_CLICK', CURRENT_TIMESTAMP - INTERVAL '5 days', 5, 'email', 'campaign', 'reactivation', 'Desktop', 'Windows', 'Outlook', 'US', 'Washington', NULL, NULL, false, 'https://example.com/reactivation-offer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 10, 'AD_VIEW', CURRENT_TIMESTAMP - INTERVAL '13 days', 1, 'google', 'cpc', 'q1-lead-gen', 'Mobile', 'Android', 'Chrome', 'US', 'Los Angeles', NULL, NULL, false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 14, 'AD_VIEW', CURRENT_TIMESTAMP - INTERVAL '19 days', 8, 'youtube', 'video', 'brand-awareness', 'Mobile', 'iOS', 'YouTube', 'US', 'Miami', NULL, NULL, false, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 15, 'LANDING_PAGE_VISIT', CURRENT_TIMESTAMP - INTERVAL '7 days', 6, 'google', 'organic', '', 'Desktop', 'Windows', 'Chrome', 'US', 'Atlanta', 8, 55000.00, true, 'https://example.com/blog/marketing-trends', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Campaign Metrics (30 metrics across campaigns)
INSERT INTO campaign_metrics (campaign_id, name, metric_type, description, current_value, target_value, measurement_unit, is_target_achieved, is_automated, created_at, updated_at, created_by, updated_by) VALUES
(1, 'Total Leads', 'COUNT', 'Total number of leads generated', 185, 500, 'leads', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'Cost Per Lead', 'COST', 'Average cost to acquire a lead', 81.08, 100.00, 'USD', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'Conversion Rate', 'PERCENTAGE', 'Lead to customer conversion rate', 6.51, 8.00, '%', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(1, 'Campaign ROI', 'RATIO', 'Return on investment', 280.00, 300.00, '%', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Total Impressions', 'COUNT', 'Total ad impressions', 1850000, 5000000, 'impressions', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Click-Through Rate', 'PERCENTAGE', 'Ad click-through rate', 0.68, 0.50, '%', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Video Views', 'COUNT', 'Brand video total views', 125000, 250000, 'views', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(2, 'Video Completion Rate', 'PERCENTAGE', 'Percentage watching full video', 65.00, 50.00, '%', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(3, 'Email Open Rate', 'PERCENTAGE', 'Newsletter open rate', 38.50, 35.00, '%', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(3, 'Email Click Rate', 'PERCENTAGE', 'Newsletter click rate', 12.30, 10.00, '%', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(3, 'Customer Retention', 'PERCENTAGE', 'Customers retained', 92.50, 95.00, '%', false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(3, 'NPS Score', 'SCORE', 'Net Promoter Score', 68, 70, 'score', false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Followers', 'COUNT', 'Total social media followers', 24000, 50000, 'followers', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Engagement Rate', 'PERCENTAGE', 'Average engagement rate', 6.20, 5.00, '%', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Shares', 'COUNT', 'Total content shares', 5500, 10000, 'shares', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(6, 'Social Reach', 'COUNT', 'Total reach', 850000, 2000000, 'users', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(7, 'Webinars Hosted', 'COUNT', 'Total webinars completed', 3, 12, 'events', false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 'Total Attendees', 'COUNT', 'Total webinar attendees', 735, 2400, 'attendees', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 'Average Attendance Rate', 'PERCENTAGE', 'Registration to attendance rate', 82.00, 75.00, '%', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(7, 'Lead Conversion Rate', 'PERCENTAGE', 'Webinar lead conversion', 18.50, 15.00, '%', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(8, 'Partners Onboarded', 'COUNT', 'New partners recruited', 18, 50, 'partners', false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(8, 'Partner Revenue', 'CURRENCY', 'Revenue from partner channel', 45000.00, 200000.00, 'USD', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(8, 'Partner Satisfaction', 'SCORE', 'Partner satisfaction score', 4.5, 4.0, 'score', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5, 5),
(9, 'Articles Published', 'COUNT', 'SEO articles published', 42, 100, 'articles', false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(9, 'Organic Traffic', 'COUNT', 'Organic website visitors', 38000, 100000, 'visits', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(9, 'Average Time on Page', 'DURATION', 'Average article read time', 4.2, 3.5, 'minutes', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(9, 'SEO Keyword Rankings', 'COUNT', 'Keywords ranking top 10', 85, 150, 'keywords', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9, 9),
(10, 'Reactivated Customers', 'COUNT', 'Customers reactivated', 65, 200, 'customers', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(10, 'Reactivation Rate', 'PERCENTAGE', 'Percentage reactivated', 10.83, 15.00, '%', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6),
(10, 'Reactivation Revenue', 'CURRENCY', 'Revenue from reactivated customers', 126000.00, 400000.00, 'USD', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6, 6);
