-- Marketing Channels Table
create table if not exists marketing_channels
(
    id                          bigserial
        primary key,
    name                        varchar(100)                        not null,
    channel_type                varchar(50)                         not null
        constraint ck_channel_type_valid
            check ((channel_type)::text = ANY
                   ((ARRAY ['PAID'::character varying, 'ORGANIC'::character varying, 'SOCIAL'::character varying, 'DIRECT'::character varying, 'EMAIL'::character varying, 'REFERRAL'::character varying])::text[])),
    description                 text,
    default_cost_per_click      numeric(10, 2),
    default_cost_per_impression numeric(10, 4),
    is_active                   boolean   default true,
    created_at                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  bigint,
    updated_at                  timestamp default CURRENT_TIMESTAMP not null,
    updated_by                  bigint,
    deleted_at                  timestamp,
    version                     integer   default 1                 not null
);

alter table marketing_channels
    owner to postgres;

create unique index if not exists idx_uq_marketing_channel_name
    on marketing_channels (name)
    where (deleted_at IS NULL);


-- Marketing Campaigns Table
create table if not exists marketing_campaigns
(
    id                           bigserial
        primary key,
    name                         varchar(100)                             not null,
    description                  varchar(1000),
    campaign_type                varchar(50)                              not null
        constraint ck_campaign_type_valid
            check ((campaign_type)::text = ANY
                   ((ARRAY ['AWARENESS'::character varying, 'CONVERSION'::character varying, 'RETENTION'::character varying, 'REACTIVATION'::character varying, 'BRANDING'::character varying])::text[])),
    status                       varchar(30)                              not null
        constraint ck_campaign_status_valid
            check ((status)::text = ANY
                   ((ARRAY ['DRAFT'::character varying, 'PLANNED'::character varying, 'ACTIVE'::character varying, 'PAUSED'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[])),
    total_budget                 numeric(15, 2)                           not null,
    spent_amount                 numeric(15, 2) default 0.00,
    budget_allocations           jsonb,
    start_date                   date                                     not null,
    end_date                     date,
    target_audience_demographics jsonb,
    target_locations             jsonb,
    target_interests             jsonb,
    primary_goal                 varchar(200),
    secondary_goals              jsonb,
    success_metrics              jsonb,
    primary_channel_id           bigint
        constraint fk_campaign_primary_channel
            references marketing_channels
            on delete set null,
    created_at                   timestamp      default CURRENT_TIMESTAMP not null,
    created_by                   bigint,
    updated_at                   timestamp      default CURRENT_TIMESTAMP not null,
    updated_by                   bigint,
    deleted_at                   timestamp,
    version                      integer        default 1                 not null,
    constraint ck_campaign_budget_valid
        check ((total_budget > (0)::numeric) AND (spent_amount >= (0)::numeric)),
    constraint ck_campaign_dates_valid
        check ((end_date IS NULL) OR (end_date >= start_date))
);

alter table marketing_campaigns
    owner to postgres;

create index if not exists idx_campaigns_status_date
    on marketing_campaigns (status asc, start_date desc)
    where (deleted_at IS NULL);

create index if not exists idx_campaigns_type_budget
    on marketing_campaigns (campaign_type asc, total_budget desc)
    where (deleted_at IS NULL);

create index if not exists idx_campaigns_channel
    on marketing_campaigns (primary_channel_id)
    where (primary_channel_id IS NOT NULL);

create unique index if not exists idx_uq_campaign_name
    on marketing_campaigns (name)
    where (deleted_at IS NULL);

-- Trigger helper: ensure function exists for updating `updated_at` on row updates
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS trigger AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

create trigger trg_update_marketing_campaigns
    before update
    on marketing_campaigns
    for each row
execute procedure update_updated_at_column();



-- Marketing Assets Table
create table if not exists marketing_assets
(
    id                bigserial
        primary key,
    campaign_id       bigint                              not null
        constraint fk_marketing_assets_campaign
            references marketing_campaigns
            on delete cascade,
    asset_type        varchar(50)                         not null
        constraint ck_asset_type_valid
            check ((asset_type)::text = ANY
                   ((ARRAY ['LANDING_PAGE'::character varying, 'AD_CREATIVE'::character varying, 'EMAIL_TEMPLATE'::character varying, 'WHITEPAPER'::character varying, 'VIDEO'::character varying, 'IMAGE'::character varying, 'DOCUMENT'::character varying, 'SOCIAL_POST'::character varying])::text[])),
    name              varchar(200)                        not null,
    description       text,
    url               varchar(500)                        not null,
    file_size_kb      integer
        constraint ck_asset_file_size
            check ((file_size_kb IS NULL) OR (file_size_kb > 0)),
    mime_type         varchar(100),
    views_count       integer   default 0,
    clicks_count      integer   default 0,
    conversions_count integer   default 0,
    status            varchar(30)                         not null
        constraint ck_asset_status_valid
            check ((status)::text = ANY
                   ((ARRAY ['DRAFT'::character varying, 'READY'::character varying, 'ACTIVE'::character varying, 'ARCHIVED'::character varying])::text[])),
    is_primary_asset  boolean   default false,
    created_at        timestamp default CURRENT_TIMESTAMP not null,
    created_by        bigint,
    updated_at        timestamp default CURRENT_TIMESTAMP not null,
    updated_by        bigint,
    deleted_at        timestamp,
    version           integer   default 1                 not null
);

alter table marketing_assets
    owner to postgres;

create index if not exists idx_assets_campaign_type
    on marketing_assets (campaign_id, asset_type, status);

create trigger trg_update_marketing_assets
    before update
    on marketing_assets
    for each row
execute procedure update_updated_at_column();


-- AbTest Table
create table if not exists ab_tests
(
    id                       bigserial
        primary key,
    campaign_id              bigint                                  not null
        constraint fk_ab_test_campaign
            references marketing_campaigns
            on delete cascade,
    test_name                varchar(200)                            not null,
    hypothesis               varchar(500),
    test_type                varchar(50)                             not null
        constraint ck_test_type_valid
            check ((test_type)::text = ANY
                   ((ARRAY ['SPLIT_URL'::character varying, 'MULTIVARIATE'::character varying, 'BANDIT'::character varying])::text[])),
    primary_metric           varchar(100)                            not null,
    confidence_level         numeric(4, 3) default 0.95
        constraint ck_confidence_level_valid
            check ((confidence_level >= 0.8) AND (confidence_level <= 0.999)),
    required_sample_size     integer
        constraint ck_sample_size_valid
            check ((required_sample_size IS NULL) OR (required_sample_size > 100)),
    control_variant          varchar(100)                            not null,
    treatment_variants       jsonb                                   not null,
    winning_variant          varchar(100),
    statistical_significance numeric(4, 3),
    is_completed             boolean       default false,
    start_date               timestamp                               not null,
    end_date                 timestamp,
    created_at               timestamp     default CURRENT_TIMESTAMP not null,
    created_by               bigint,
    updated_at               timestamp     default CURRENT_TIMESTAMP not null,
    updated_by               bigint,
    deleted_at               timestamp,
    version                  integer       default 1                 not null,
    constraint ck_dates_valid
        check ((end_date IS NULL) OR (end_date > start_date))
);

alter table ab_tests
    owner to postgres;

create index if not exists idx_ab_tests_campaign_status
    on ab_tests (campaign_id asc, is_completed asc, start_date desc);

create trigger trg_update_ab_tests
    before update
    on ab_tests
    for each row
execute procedure update_updated_at_column();

create table if not exists campaign_activities
(
    id                      bigserial
        primary key,
    campaign_id             bigint                              not null
        constraint fk_activity_campaign
            references marketing_campaigns
            on delete cascade,
    name                    varchar(100)                        not null,
    description             varchar(1000),
    activity_type           varchar(50)                         not null
        constraint ck_activity_type_valid
            check ((activity_type)::text = ANY
                   ((ARRAY ['CONTENT_CREATION'::character varying, 'AD_SETUP'::character varying, 'EMAIL_BLAST'::character varying, 'SOCIAL_POST'::character varying, 'EVENT'::character varying, 'WEBINAR'::character varying, 'ANALYSIS'::character varying, 'OPTIMIZATION'::character varying])::text[])),
    status                  varchar(30)                         not null
        constraint ck_activity_status_valid
            check ((status)::text = ANY
                   ((ARRAY ['PLANNED'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying, 'BLOCKED'::character varying])::text[])),
    planned_start_date      timestamp                           not null,
    planned_end_date        timestamp                           not null,
    actual_start_date       timestamp,
    actual_end_date         timestamp,
    planned_cost            numeric(10, 2)                      not null,
    actual_cost             numeric(10, 2),
    cost_overrun_percentage numeric(5, 2),
    assigned_to_user_id     bigint
        constraint fk_activity_assigned_to
            references users
            on delete set null,
    delivery_channel        varchar(50)                         not null
        constraint ck_delivery_channel_not_empty
            check ((delivery_channel)::text <> ''::text),
    success_criteria        varchar(500),
    target_audience         varchar(500),
    dependencies            jsonb,
    created_at              timestamp default CURRENT_TIMESTAMP not null,
    created_by              bigint,
    updated_at              timestamp default CURRENT_TIMESTAMP not null,
    updated_by              bigint,
    deleted_at              timestamp,
    version                 integer   default 1                 not null,
    constraint ck_activity_dates_valid
        check (planned_end_date > planned_start_date),
    constraint ck_actual_dates_valid
        check (((actual_start_date IS NULL) AND (actual_end_date IS NULL)) OR ((actual_start_date IS NOT NULL) AND
                                                                               ((actual_end_date IS NULL) OR (actual_end_date >= actual_start_date)))),
    constraint ck_cost_valid
        check ((planned_cost > (0)::numeric) AND ((actual_cost IS NULL) OR (actual_cost >= (0)::numeric)))
);

alter table campaign_activities
    owner to postgres;

create index if not exists idx_activities_campaign_status
    on campaign_activities (campaign_id, status, planned_start_date);

create index if not exists idx_activities_assigned_user
    on campaign_activities (assigned_to_user_id, status)
    where (assigned_to_user_id IS NOT NULL);

create trigger trg_update_campaign_activities
    before update
    on campaign_activities
    for each row
execute procedure update_updated_at_column();

-- Campaign Attribution Table
create table if not exists campaign_attribution
(
    id                     bigserial
        primary key,
    deal_id                bigint                                                  not null
        constraint fk_attribution_deal
            references deals
            on delete cascade,
    campaign_id            bigint                                                  not null
        constraint fk_attribution_campaign
            references marketing_campaigns
            on delete cascade,
    attribution_model      varchar(50)                                             not null
        constraint ck_attribution_model_valid
            check ((attribution_model)::text = ANY
                   ((ARRAY ['FIRST_TOUCH'::character varying, 'LAST_TOUCH'::character varying, 'LINEAR'::character varying, 'TIME_DECAY'::character varying, 'CUSTOM'::character varying])::text[])),
    attribution_percentage numeric(5, 2)                                           not null
        constraint ck_attribution_percentage_valid
            check ((attribution_percentage >= (0)::numeric) AND (attribution_percentage <= (100)::numeric)),
    attributed_revenue     numeric(15, 2)                default 0.00              not null
        constraint ck_attribution_revenue_positive
            check (attributed_revenue >= (0)::numeric),
    touch_timestamps       timestamp without time zone[] default '{}'::timestamp without time zone[],
    touch_count            integer                       default 0,
    first_touch_weight     numeric(5, 2)                 default 0,
    last_touch_weight      numeric(5, 2)                 default 0,
    linear_weight          numeric(5, 2)                 default 0,
    created_at             timestamp                     default CURRENT_TIMESTAMP not null,
    created_by             bigint,
    updated_at             timestamp                     default CURRENT_TIMESTAMP not null,
    updated_by             bigint,
    deleted_at             timestamp,
    version                integer                       default 1                 not null,
    constraint ck_weights_valid
        check ((first_touch_weight >= (0)::numeric) AND (last_touch_weight >= (0)::numeric) AND
               (linear_weight >= (0)::numeric))
);

alter table campaign_attribution
    owner to postgres;

create index if not exists idx_attribution_deal
    on campaign_attribution (deal_id asc, attribution_percentage desc);

create index if not exists idx_attribution_campaign_revenue
    on campaign_attribution (campaign_id asc, attributed_revenue desc);

create unique index if not exists idx_uq_attribution_deal_campaign
    on campaign_attribution (deal_id, campaign_id)
    where (deleted_at IS NULL);

create trigger trg_update_campaign_attribution
    before update
    on campaign_attribution
    for each row
execute procedure update_updated_at_column();

-- Marketing Channels Table
create table if not exists marketing_channels
(
    id                          bigserial
        primary key,
    name                        varchar(100)                        not null,
    channel_type                varchar(50)                         not null
        constraint ck_channel_type_valid
            check ((channel_type)::text = ANY
                   ((ARRAY ['PAID'::character varying, 'ORGANIC'::character varying, 'SOCIAL'::character varying, 'DIRECT'::character varying, 'EMAIL'::character varying, 'REFERRAL'::character varying])::text[])),
    description                 text,
    default_cost_per_click      numeric(10, 2),
    default_cost_per_impression numeric(10, 4),
    is_active                   boolean   default true,
    created_at                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  bigint,
    updated_at                  timestamp default CURRENT_TIMESTAMP not null,
    updated_by                  bigint,
    deleted_at                  timestamp,
    version                     integer   default 1                 not null
);

-- Marketing Channels Table (defined above)

-- Campaign Interactions Table
create table if not exists campaign_interactions
(
    id               bigserial
        primary key,
    campaign_id      bigint                              not null
        constraint fk_interaction_campaign
            references marketing_campaigns
            on delete cascade,
    customer_id      bigint                              not null
        constraint fk_interaction_customer
            references customer_companies
            on delete cascade,
    interaction_type varchar(50)                         not null
        constraint ck_interaction_type_valid
            check ((interaction_type)::text = ANY
                   ((ARRAY ['AD_CLICK'::character varying, 'AD_VIEW'::character varying, 'EMAIL_OPEN'::character varying, 'EMAIL_CLICK'::character varying, 'LANDING_PAGE_VISIT'::character varying, 'FORM_SUBMIT'::character varying, 'SOCIAL_ENGAGEMENT'::character varying, 'WEBINAR_REGISTRATION'::character varying, 'WHITEPAPER_DOWNLOAD'::character varying])::text[])),
    interaction_date timestamp default CURRENT_TIMESTAMP not null
        constraint ck_interaction_date_not_future
            check (interaction_date <= (CURRENT_TIMESTAMP + '00:05:00'::interval)),
    channel_id       bigint
        constraint fk_interaction_channel
            references marketing_channels
            on delete set null,
    utm_source       varchar(100),
    utm_medium       varchar(100),
    utm_campaign     varchar(100),
    utm_content      varchar(100),
    utm_term         varchar(100),
    device_type      varchar(50),
    device_os        varchar(50),
    browser          varchar(100),
    country_code     varchar(2),
    city             varchar(100),
    deal_id          bigint
        constraint fk_interaction_deal
            references deals
            on delete set null,
    conversion_value numeric(10, 2)
        constraint ck_conversion_value_positive
            check ((conversion_value IS NULL) OR (conversion_value > (0)::numeric)),
    is_conversion    boolean   default false,
    conversion_date  timestamp,
    conversion_notes varchar(500),
    landing_page_url varchar(500),
    referrer_url     varchar(500),
    session_id       varchar(100),
    properties       jsonb,
    created_at       timestamp default CURRENT_TIMESTAMP not null,
    updated_at       timestamp default CURRENT_TIMESTAMP not null,
    deleted_at       timestamp,
    version          integer   default 1                 not null,
    constraint ck_is_conversion_valid
        check ((is_conversion = false) OR ((is_conversion = true) AND (deal_id IS NOT NULL)))
);

alter table campaign_interactions
    owner to postgres;

create index if not exists idx_interactions_campaign_customer
    on campaign_interactions (campaign_id asc, customer_id asc, interaction_date desc);

create index if not exists idx_interactions_conversion
    on campaign_interactions (campaign_id, is_conversion, interaction_date)
    where (is_conversion = true);

create index if not exists idx_interactions_utm
    on campaign_interactions (utm_source, utm_medium, utm_campaign)
    where (utm_source IS NOT NULL);

create index if not exists idx_interactions_date_type
    on campaign_interactions (interaction_date desc, interaction_type asc);

create trigger trg_update_campaign_interactions
    before update
    on campaign_interactions
    for each row
execute procedure update_updated_at_column();


-- Create Campaign Metric Table
create table if not exists campaign_metrics
(
    id                   bigserial
        primary key,
    campaign_id          bigint                                   not null
        constraint fk_campaign_metrics_campaign
            references marketing_campaigns
            on delete cascade,
    name                 varchar(100)                             not null,
    metric_type          varchar(30)                              not null
        constraint ck_metric_type_valid
            check ((metric_type)::text = ANY
                   ((ARRAY ['COUNT'::character varying, 'CURRENCY'::character varying, 'PERCENTAGE'::character varying, 'DURATION'::character varying, 'COST'::character varying, 'RATIO'::character varying, 'SCORE'::character varying])::text[])),
    description          text,
    current_value        numeric(15, 4) default 0,
    target_value         numeric(15, 4),
    measurement_unit     varchar(50),
    calculation_formula  varchar(500),
    data_source          varchar(200),
    last_calculated_date timestamp,
    is_automated         boolean        default false,
    is_target_achieved   boolean        default false,
    created_at           timestamp      default CURRENT_TIMESTAMP not null,
    created_by           bigint,
    updated_at           timestamp      default CURRENT_TIMESTAMP not null,
    updated_by           bigint,
    deleted_at           timestamp,
    version              integer        default 1                 not null,
    constraint ck_values_valid
        check ((current_value >= (0)::numeric) AND ((target_value IS NULL) OR (target_value >= (0)::numeric)))
);

alter table campaign_metrics
    owner to postgres;

create index if not exists idx_metrics_campaign_target
    on campaign_metrics (campaign_id, is_target_achieved, metric_type);

create unique index if not exists idx_uq_campaign_metric_name
    on campaign_metrics (campaign_id, name)
    where (deleted_at IS NULL);

create trigger trg_update_campaign_metrics
    before update
    on campaign_metrics
    for each row
execute procedure update_updated_at_column();

-- Campaign Targets Table
create table if not exists campaign_targets
(
    id                     bigserial
        primary key,
    campaign_id            bigint                              not null
        constraint fk_campaign_target_campaign
            references marketing_campaigns
            on delete cascade,
    metric_name            varchar(100)                        not null,
    metric_type            varchar(50)                         not null
        constraint ck_target_metric_type_valid
            check ((metric_type)::text = ANY
                   ((ARRAY ['CONVERSION'::character varying, 'ENGAGEMENT'::character varying, 'REACH'::character varying, 'REVENUE'::character varying, 'LEAD_GENERATION'::character varying, 'BRAND_AWARENESS'::character varying, 'CUSTOMER_ACQUISITION'::character varying])::text[])),
    target_value           numeric(15, 2)                      not null
        constraint ck_target_value_positive
            check (target_value > (0)::numeric),
    current_value          numeric(15, 2) default 0
        constraint ck_current_value_positive
            check (current_value >= (0)::numeric),
    measurement_unit       varchar(50),
    status                 varchar(50)    not null default 'PENDING'
        constraint ck_target_status_valid
            check ((status)::text = ANY
                   ((ARRAY ['PENDING'::character varying, 'IN_PROGRESS'::character varying, 'ACHIEVED'::character varying, 'FAILED'::character varying, 'CANCELLED'::character varying])::text[])),
    achievement_percentage numeric(5, 2)  default 0
        constraint ck_achievement_percentage_valid
            check ((achievement_percentage >= (0)::numeric) AND (achievement_percentage <= (10000)::numeric)),
    created_at             timestamp      default CURRENT_TIMESTAMP not null,
    created_by             bigint,
    updated_at             timestamp      default CURRENT_TIMESTAMP not null,
    updated_by             bigint,
    deleted_at             timestamp,
    version                integer        default 1                 not null
);

alter table campaign_targets
    owner to postgres;

create index if not exists idx_targets_campaign
    on campaign_targets (campaign_id);

create index if not exists idx_targets_metric_type
    on campaign_targets (metric_type);

create index if not exists idx_targets_status
    on campaign_targets (status);

create index if not exists idx_targets_campaign_metric
    on campaign_targets (campaign_id, metric_type);

create trigger trg_update_campaign_targets
    before update
    on campaign_targets
    for each row
execute procedure update_updated_at_column();


