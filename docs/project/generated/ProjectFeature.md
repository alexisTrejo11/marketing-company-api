# Project Features

## GraphQL API

Schema-first GraphQL with modular `.graphqls` files for account, CRM, customer, and marketing domains. Single endpoint at `/api/graphql` with GraphiQL in dev.

| Property | Value |
| --- | --- |
| ID | graphql-api |
| Category | api |
| Status | stable |
| Icon | graphql |

### Highlights

- 22 schema files organized by bounded context
- Extended scalars (Date, DateTime, etc.)
- Global exception handler for GraphQL errors
- Subscriptions scaffold at `/api/subscriptions`

### Tech stack

- Spring GraphQL
- graphql-java-extended-scalars

### Metrics

| Label | Value | Trend |
| --- | --- | --- |
| Controllers | 25+ | stable |

## JWT authentication & sessions

Dual-token JWT (access + refresh) with BCrypt passwords and Redis-backed session storage. Separate secrets for access and refresh tokens.

| Property | Value |
| --- | --- |
| ID | jwt-auth |
| Category | authentication |
| Status | stable |
| Icon | lock |

### Highlights

- Access token default 15 minutes; refresh 30 days
- Max 5 concurrent sessions per user (configurable)
- GraphQL directives for authentication and roles
- Sensitive endpoints rate-limited

### Tech stack

- jjwt 0.12
- Spring Security
- Redis / Upstash

### Code snippet

_JwtTokenProvider.java_

```java
@Value("${app.auth.jwt.access-secret}")
private String accessTokenSecret;

public String generateAccessToken(String userId, String email, Set<Role> roles) {
    return Jwts.builder()
        .subject(userId)
        .claim("email", email)
        .claim("roles", roles.stream().map(Enum::name).toList())
        .signWith(getAccessSigningKey())
        .compact();
}
```

## CRM domain

Sales pipeline management — companies, opportunities, deals, quotes, tasks, CRM interactions, and service packages.

| Property | Value |
| --- | --- |
| ID | crm-module |
| Category | database |
| Status | stable |
| Icon | briefcase |

### Highlights

- Opportunity lifecycle (open, won, lost, reopen)
- Deal and quote workflows with line items
- Task statistics and CRM interaction analytics
- Hexagonal adapters (GraphQL → application → JPA)

### Tech stack

- Spring Data JPA
- PostgreSQL / RDS
- Flyway migrations

## Marketing domain

Campaign management, channels, activities, metrics, assets, A/B tests, campaign interactions, and attribution tracking.

| Property | Value |
| --- | --- |
| ID | marketing-module |
| Category | integration |
| Status | stable |
| Icon | megaphone |

### Highlights

- Campaign CRUD and performance metrics
- UTM and conversion tracking on interactions
- A/B test management
- Marketing asset catalog

### Tech stack

- Spring Data JPA
- GraphQL mutations and queries

## GraphQL rate limiting

Annotation-driven rate limits with global and per-operation profiles, backed by Redis counters and exposed via response headers.

| Property | Value |
| --- | --- |
| ID | rate-limiting |
| Category | security |
| Status | stable |
| Icon | shield |

### Highlights

- Profiles sensitive (7/min), standard (30/min), strict (5/5min), public (500/min)
- Global cap 5000 requests per hour
- X-RateLimit-* headers on responses

### Tech stack

- Spring AOP
- Redis

## Redis sessions & cache

Auth sessions and rate-limit state in Redis. Production uses Upstash; local development uses Docker Redis or Upstash tunnel.

| Property | Value |
| --- | --- |
| ID | redis-cache |
| Category | caching |
| Status | stable |
| Icon | database |

### Highlights

- Refresh token as session key
- User session sets for logout-all
- Caffeine in-process cache for app-level TTLs

### Tech stack

- Spring Data Redis
- Jedis
- Upstash (production)

## Observability & health

Spring Boot Actuator with health probes, metrics, and Prometheus export for external monitoring on EC2.

| Property | Value |
| --- | --- |
| ID | observability |
| Category | monitoring |
| Status | stable |
| Icon | activity |

### Highlights

- /api/actuator/health for ALB/EC2 checks
- Prometheus scrape at /api/actuator/prometheus
- Structured logging to `/app/logs`
- GraphQL audit interceptor

### Tech stack

- Spring Actuator
- Prometheus
- Logback

## Kafka event consumption

Consumes events from a cloud-managed Kafka cluster (AWS MSK or external broker). Bootstrap servers configured via environment — not hardcoded in repository.

| Property | Value |
| --- | --- |
| ID | kafka-integration |
| Category | messaging |
| Status | beta |
| Icon | message-square |

### Highlights

- External broker URL via `KAFKA_BOOTSTRAP_SERVERS`
- Intended for async domain events and integrations
- Decoupled from HTTP GraphQL flow

### Tech stack

- Apache Kafka (cloud instance)

### Metrics

| Label | Value | Trend |
| --- | --- | --- |
| Broker | {{YOUR_KAFKA_BROKER}} | stable |

## Docker local development

Multi-stage Dockerfile and two Compose files — full stack or infra-only — with explicit host overrides for container networking.

| Property | Value |
| --- | --- |
| ID | docker-local |
| Category | performance |
| Status | stable |
| Icon | box |

### Highlights

- docker/compose.full.yml — app + Postgres + Redis
- docker/compose.infra.yml — DBs only for ./gradlew bootRun
- Healthcheck on /api/actuator/health

### Tech stack

- Docker
- Docker Compose v2

## Additional notes

# Project Features

> **Warning:** Kafka integration is documented as deployed against a cloud broker; client
> configuration lives in environment variables on EC2 — verify `KAFKA_BOOTSTRAP_SERVERS`
> before enabling consumers in a new environment.

> **Useful note:** The project is minimalist by design — no REST CRUD layer beyond Actuator.
> All business operations go through GraphQL.

