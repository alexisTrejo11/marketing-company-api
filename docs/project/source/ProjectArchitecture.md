---
layers:
  - name: Input adapters (GraphQL)
    description: GraphQL controllers map schema operations to application commands and queries.
    color: "#3B82F6"
    components:
      - AuthController
      - UserController / UserManagerController
      - CompanyController
      - OpportunityQueryController / OpportunityMutationController
      - DealController
      - QuoteQueryController / QuoteCommandController
      - TaskController
      - InteractionController (CRM)
      - ServicePackageController
      - MarketingCampaignQueryController / MarketingCampaignMutationController
      - MarketingChannelController
      - ActivityQueryController / ActivityMutationController
      - CampaignMetricQueryController / CampaignMetricMutationController
      - AssetQueryController / AssetMutationController
      - AbTestQueryController / AbTestMutationController
      - CampaignInteractionController / CampaignInteractionMutationController
    responsibilities:
      - Validate GraphQL inputs (Jakarta Validation)
      - Map DTOs to commands/queries
      - Apply rate-limit and security directives
    technologies:
      - Spring GraphQL
      - graphql-java-tools

  - name: Application layer
    description: Use-case orchestration — command handlers, query services, mappers.
    color: "#10B981"
    components:
      - AuthCommandHandler
      - UserCommandServiceImpl
      - Domain command/query services per bounded context
    responsibilities:
      - Transaction boundaries
      - Authorization checks at use-case level
      - Coordinate domain entities and ports
    technologies:
      - "Spring @Service"
      - "@Transactional"

  - name: Domain layer
    description: Business rules, entities, value objects, domain exceptions, and events.
    color: "#8B5CF6"
    components:
      - account (User, AuthSession, Role, passwords)
      - customer (CustomerCompany, CompanyName, ContactPerson)
      - crm (Opportunity, Deal, Quote, Task, Interaction, ServicePackage)
      - marketing (Campaign, Channel, Activity, Metric, Asset, AbTest, Attribution)
    responsibilities:
      - Invariants and validation
      - Aggregate consistency
      - Domain-specific exceptions
    technologies:
      - Plain Java records and classes
      - DDD patterns

  - name: Output adapters (persistence & external)
    description: JPA repositories, Redis session store, JWT provider, future Kafka listeners.
    color: "#F59E0B"
    components:
      - JPA entities and repository adapters
      - RedisAuthSessionRepositoryImpl
      - JwtTokenProvider
      - Flyway migrations
    responsibilities:
      - Persist aggregates to PostgreSQL (RDS)
      - Store auth sessions in Redis (Upstash)
      - Issue and validate JWTs
    technologies:
      - Spring Data JPA
      - Spring Data Redis
      - Flyway
      - PostgreSQL

  - name: Cross-cutting (config)
    description: Security, CORS, rate limits, logging, GraphQL wiring.
    color: "#6B7280"
    components:
      - SecurityConfig
      - JwtAuthenticationFilter
      - CorsConfig / CorsProperties
      - GraphQLRateLimit / RateLimitProperties
      - GlobalGraphQLExceptionHandler
      - GraphQlAuditInterceptor
    responsibilities:
      - Stateless JWT security filter chain
      - CORS from env (`CORS_ALLOWED_ORIGINS`)
      - Audit and error shaping
    technologies:
      - Spring Security
      - Spring AOP
      - Actuator

designPatterns:
  - title: Hexagonal Architecture
    emoji: "⬡"
    description: Ports and adapters isolate domain from GraphQL, JPA, and Redis.
    category: Structural
    badge: Core
  - title: Domain-Driven Design
    emoji: "🏛"
    description: Bounded contexts for account, CRM, customer, and marketing.
    category: Strategic
    badge: Core
  - title: CQRS (light)
    emoji: "↔"
    description: Separate command and query types per use case without event sourcing.
    category: Behavioral
    badge: Applied
  - title: Repository
    emoji: "📦"
    description: Output ports abstract persistence behind adapter implementations.
    category: Structural
    badge: Core
  - title: Value Object
    emoji: "💎"
    description: Immutable types (Email, DealStatus, UTMParameters, etc.) enforce invariants.
    category: Domain
    badge: Core

scalabilityStrategies:
  - title: Stateless API tier
    description: JWT + Redis sessions allow horizontal scaling of EC2 instances behind a load balancer.
  - title: Managed data stores
    description: RDS and Upstash handle connection pooling and replication outside the app.
  - title: Connection pooling
    description: HikariCP (max 20 connections) tuned for RDS limits.

securityStrategies:
  - title: JWT dual secrets
    description: Separate access and refresh signing keys (`JWT_ACCESS_SECRET`, `JWT_REFRESH_SECRET`).
  - title: BCrypt passwords
    description: Passwords hashed before persistence; plain passwords validated at domain level.
  - title: Rate limiting
    description: Redis-backed limits on sensitive GraphQL operations.
  - title: CORS allow-list
    description: Origins from environment; credentials enabled for SPA clients.
  - title: Prod hardening
    description: GraphiQL off, reduced stack traces, SQL logging disabled in `prod` profile.

cacheStrategies:
  - name: Redis auth sessions
    description: Refresh tokens and user session sets
    ttl: Session TTL (refresh token expiry, default 30 days)
    coverage: Authentication
  - name: Redis rate limits
    description: Counters for global and per-operation limits
    ttl: 60s – 1h depending on profile
    coverage: API protection
  - name: Caffeine (in-process)
    description: Company and customer entity caches
    ttl: 300–600 seconds
    coverage: Read-heavy lookups

architectureFeatures:
  - title: Schema-first GraphQL
    emoji: "📜"
    description: SDL files in `graphql/` loaded automatically by Spring GraphQL.
  - title: Flyway migrations
    emoji: "🔄"
    description: "Versioned SQL migrations; ddl-auto validate mode in production."
  - title: Profile-based config
    emoji: "⚙"
    description: dev, docker, prod, and test profiles in `application.yml`.
  - title: External observability
    emoji: "📊"
    description: Actuator health and Prometheus metrics scraped from EC2.

architectureDiagram:
  legendItems:
    - type: client
      label: Client (SPA / tools)
      color: "#3B82F6"
      icon: monitor
    - type: gateway
      label: ALB / EC2
      color: "#10B981"
      icon: server
    - type: service
      label: Spring Boot API
      color: "#8B5CF6"
      icon: box
    - type: database
      label: RDS PostgreSQL
      color: "#F59E0B"
      icon: database
    - type: database
      label: Upstash Redis
      color: "#EF4444"
      icon: redis
    - type: queue
      label: Cloud Kafka
      color: "#6366F1"
      icon: kafka
    - type: monitoring
      label: Prometheus / logs
      color: "#64748B"
      icon: chart
  nodes:
    - id: spa
      label: Frontend / GraphQL client
      type: client
      x: 0
      y: 0
      status: healthy
    - id: alb
      label: ALB or EC2 public
      type: gateway
      x: 1
      y: 0
      status: healthy
    - id: api
      label: Marketing Company API
      type: service
      x: 2
      y: 0
      status: healthy
    - id: rds
      label: AWS RDS PostgreSQL
      type: database
      x: 3
      y: -1
      status: healthy
    - id: upstash
      label: Upstash Redis
      type: database
      x: 3
      y: 1
      status: healthy
    - id: kafka
      label: Cloud Kafka broker
      type: queue
      x: 2
      y: 1
      status: healthy
    - id: prom
      label: External observability
      type: monitoring
      x: 3
      y: 0
      status: healthy
  connections:
    - id: c1
      from: spa
      to: alb
      label: HTTPS GraphQL
      protocol: HTTPS
      isActive: true
    - id: c2
      from: alb
      to: api
      label: /api/graphql
      protocol: HTTP
      isActive: true
    - id: c3
      from: api
      to: rds
      label: JDBC
      protocol: TCP
      isActive: true
    - id: c4
      from: api
      to: upstash
      label: Sessions / rate limit
      protocol: TLS
      isActive: true
    - id: c5
      from: api
      to: kafka
      label: Consume events
      protocol: Kafka
      isActive: true
    - id: c6
      from: prom
      to: api
      label: Scrape metrics
      protocol: HTTP
      isActive: true

dataFlow:
  requestFlow:
    - number: 1
      title: Client request
      description: POST /api/graphql with optional Bearer token
      icon: send
    - number: 2
      title: Security filter
      description: JwtAuthenticationFilter validates access token and loads roles
      icon: lock
    - number: 3
      title: Rate limit check
      description: GraphQLRateLimit aspect enforces Redis counters
      icon: gauge
    - number: 4
      title: Controller → command
      description: GraphQL controller maps input to application command/query
      icon: arrow-right
    - number: 5
      title: Domain + persistence
      description: Handler executes domain logic via output port adapters (JPA/Redis)
      icon: database
    - number: 6
      title: Response
      description: Mapped DTO returned as GraphQL data or structured error
      icon: reply
  eventFlow:
    - number: 1
      title: External event published
      description: Upstream system produces message to cloud Kafka topic
      icon: kafka
    - number: 2
      title: Consumer on EC2
      description: App instance consumes via KAFKA_BOOTSTRAP_SERVERS (env)
      icon: server
    - number: 3
      title: Domain handler
      description: Event mapped to application command (integration boundary)
      icon: cog
    - number: 4
      title: Side effects
      description: Persist to RDS and/or invalidate Redis cache as needed
      icon: database

techDecisions:
  decisions:
    - title: GraphQL over REST
      problem: Multiple CRM and marketing clients need flexible reads without version explosion.
      solution: Single GraphQL schema with modular SDL and Spring GraphQL controllers.
      alternatives:
        - OpenAPI REST with many DTO permutations
        - gRPC for internal-only clients
      outcome: One endpoint, strong typing, GraphiQL for developer experience.
      icon: graphql
    - title: Hexagonal modules per domain
      problem: Marketing and CRM logic must stay testable as the system grows.
      solution: package-by-domain with explicit ports (input/output) and adapters.
      alternatives:
        - Layered package-by-technical-concern
        - Monolithic service class
      outcome: Clear boundaries; swap GraphQL or JPA without touching domain rules.
      icon: layers
    - title: Upstash Redis instead of self-hosted on EC2
      problem: Session and rate-limit state must survive app restarts and scale horizontally.
      solution: Managed Upstash Redis with TLS; configured via SPRING_REDIS_HOST in production.
      alternatives:
        - Redis container on same EC2
        - ElastiCache
      outcome: No Redis ops on EC2; suitable for minimalist deployment.
      icon: redis
    - title: AWS RDS for PostgreSQL
      problem: Need durable relational storage with backups for CRM/marketing data.
      solution: RDS PostgreSQL 16; Flyway migrations on startup.
      alternatives:
        - PostgreSQL in Docker on EC2
        - Aurora Serverless
      outcome: Managed backups and patching; app connects via JDBC env vars.
      icon: database
---

# Project Architecture

> **Important:** Kafka appears in the architecture diagram as an **external** cloud broker.
> Consumer configuration is environment-driven — the repository does not embed broker URLs.

> **Warning:** Never run `ddl-auto: update` in production. The project uses `validate` + Flyway.

> **Useful note:** Package layout follows `at.backend.MarketingCompany.<domain>.<adapter|core>`.
> Shared kernel lives under `shared/` and cross-cutting under `config/`.
