---
features:
  - id: graphql-api
    title: GraphQL API
    description: >-
      Schema-first GraphQL with modular `.graphqls` files for account, CRM, customer,
      and marketing domains. Single endpoint at `/api/graphql` with GraphiQL in dev.
    icon: graphql
    category: api
    status: stable
    highlights:
      - 22 schema files organized by bounded context
      - Extended scalars (Date, DateTime, etc.)
      - Global exception handler for GraphQL errors
      - Subscriptions scaffold at `/api/subscriptions`
    techStack:
      - Spring GraphQL
      - graphql-java-extended-scalars
    metrics:
      - label: Controllers
        value: 25+
        trend: stable

  - id: jwt-auth
    title: JWT authentication & sessions
    description: >-
      Dual-token JWT (access + refresh) with BCrypt passwords and Redis-backed session
      storage. Separate secrets for access and refresh tokens.
    icon: lock
    category: authentication
    status: stable
    highlights:
      - Access token default 15 minutes; refresh 30 days
      - Max 5 concurrent sessions per user (configurable)
      - GraphQL directives for authentication and roles
      - Sensitive endpoints rate-limited
    techStack:
      - jjwt 0.12
      - Spring Security
      - Redis / Upstash
    codeSnippet:
      language: java
      filename: JwtTokenProvider.java
      code: |
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

  - id: crm-module
    title: CRM domain
    description: >-
      Sales pipeline management — companies, opportunities, deals, quotes, tasks,
      CRM interactions, and service packages.
    icon: briefcase
    category: database
    status: stable
    highlights:
      - Opportunity lifecycle (open, won, lost, reopen)
      - Deal and quote workflows with line items
      - Task statistics and CRM interaction analytics
      - Hexagonal adapters (GraphQL → application → JPA)
    techStack:
      - Spring Data JPA
      - PostgreSQL / RDS
      - Flyway migrations

  - id: marketing-module
    title: Marketing domain
    description: >-
      Campaign management, channels, activities, metrics, assets, A/B tests,
      campaign interactions, and attribution tracking.
    icon: megaphone
    category: integration
    status: stable
    highlights:
      - Campaign CRUD and performance metrics
      - UTM and conversion tracking on interactions
      - A/B test management
      - Marketing asset catalog
    techStack:
      - Spring Data JPA
      - GraphQL mutations and queries

  - id: rate-limiting
    title: GraphQL rate limiting
    description: >-
      Annotation-driven rate limits with global and per-operation profiles,
      backed by Redis counters and exposed via response headers.
    icon: shield
    category: security
    status: stable
    highlights:
      - Profiles sensitive (7/min), standard (30/min), strict (5/5min), public (500/min)
      - Global cap 5000 requests per hour
      - X-RateLimit-* headers on responses
    techStack:
      - Spring AOP
      - Redis

  - id: redis-cache
    title: Redis sessions & cache
    description: >-
      Auth sessions and rate-limit state in Redis. Production uses Upstash;
      local development uses Docker Redis or Upstash tunnel.
    icon: database
    category: caching
    status: stable
    highlights:
      - Refresh token as session key
      - User session sets for logout-all
      - Caffeine in-process cache for app-level TTLs
    techStack:
      - Spring Data Redis
      - Jedis
      - Upstash (production)

  - id: observability
    title: Observability & health
    description: >-
      Spring Boot Actuator with health probes, metrics, and Prometheus export
      for external monitoring on EC2.
    icon: activity
    category: monitoring
    status: stable
    highlights:
      - "/api/actuator/health for ALB/EC2 checks"
      - "Prometheus scrape at /api/actuator/prometheus"
      - Structured logging to `/app/logs`
      - GraphQL audit interceptor
    techStack:
      - Spring Actuator
      - Prometheus
      - Logback

  - id: kafka-integration
    title: Kafka event consumption
    description: >-
      Consumes events from a cloud-managed Kafka cluster (AWS MSK or external broker).
      Bootstrap servers configured via environment — not hardcoded in repository.
    icon: message-square
    category: messaging
    status: beta
    highlights:
      - External broker URL via `KAFKA_BOOTSTRAP_SERVERS`
      - Intended for async domain events and integrations
      - Decoupled from HTTP GraphQL flow
    techStack:
      - Apache Kafka (cloud instance)
    metrics:
      - label: Broker
        value: "{{YOUR_KAFKA_BROKER}}"
        trend: stable

  - id: docker-local
    title: Docker local development
    description: >-
      Multi-stage Dockerfile and two Compose files — full stack or infra-only —
      with explicit host overrides for container networking.
    icon: box
    category: performance
    status: stable
    highlights:
      - "docker/compose.full.yml — app + Postgres + Redis"
      - "docker/compose.infra.yml — DBs only for ./gradlew bootRun"
      - "Healthcheck on /api/actuator/health"
    techStack:
      - Docker
      - Docker Compose v2
---

# Project Features

> **Warning:** Kafka integration is documented as deployed against a cloud broker; client
> configuration lives in environment variables on EC2 — verify `KAFKA_BOOTSTRAP_SERVERS`
> before enabling consumers in a new environment.

> **Useful note:** The project is minimalist by design — no REST CRUD layer beyond Actuator.
> All business operations go through GraphQL.
