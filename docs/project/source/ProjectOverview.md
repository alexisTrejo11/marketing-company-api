---
problemStatement:
  problemTitle: Unified CRM and marketing data without a monolithic frontend lock-in
  problemDescription: >-
    Marketing agencies and CRM teams need a single backend that models customers, sales
    pipelines, campaigns, and attribution — while staying API-first, secure, and deployable
    to cloud infrastructure without coupling to a specific UI framework.
  problemList:
    - Fragmented CRM and marketing data across spreadsheets and ad-hoc tools
    - Need for JWT-authenticated, rate-limited APIs suitable for multiple clients
    - Session and cache requirements that differ between local Docker and cloud (Upstash)
    - Operational need for health checks, metrics, and event consumption from managed Kafka
solution:
  solutionTitle: GraphQL CRM + marketing API on hexagonal Spring Boot
  solutionList:
    - title: Single GraphQL entrypoint
      description: One schema spanning account, CRM, customer, and marketing bounded contexts at `/api/graphql`.
    - title: Hexagonal / DDD modules
      description: Ports and adapters per domain — GraphQL in, JPA/Redis out — keeping business logic testable.
    - title: JWT + Redis sessions
      description: Access/refresh tokens with session state stored in Redis (Upstash in AWS).
    - title: Cloud-native deployment
      description: App on EC2, PostgreSQL on RDS, Redis on Upstash, Kafka consumed from a cloud broker; Actuator exposes Prometheus metrics.
keyMetrics:
  metricsTitle: At a glance
  metricsList:
    - "22 GraphQL schema modules under src/main/resources/graphql/"
    - "750+ Java source files across CRM, marketing, account, and shared layers"
    - "GraphQL API prefix /api with context path and Actuator at /api/actuator/*"
    - "Deployed on AWS — EC2 application tier, RDS PostgreSQL, Upstash Redis"
    - "External observability via Spring Actuator (health, metrics, prometheus)"
links:
  github: https://github.com/alexisTrejo11/marketing-company-api
  demo: https://{{YOUR_EC2_OR_ALB_HOST}}/api/actuator/health
  documentation: docs/project/generated/README.md
  dockerHub: null
mediaGallery:
  title: Architecture & API
  description: Placeholder gallery — add screenshots or diagrams when available.
  items: []
mediaItems: []
metrics:
  - label: API style
    value: GraphQL
    description: Spring GraphQL with schema-first `.graphqls` files
  - label: Runtime
    value: Java 23
    description: Eclipse Temurin JRE in Docker; EC2 in production
  - label: Database
    value: PostgreSQL 16
    description: AWS RDS in production; Docker/local for development
  - label: Cache / sessions
    value: Redis
    description: Upstash in production; Docker Redis for local full stack
  - label: Messaging
    value: Kafka (external)
    description: Consumed from cloud-managed broker — bootstrap URL via env (not in repo)
  - label: Status
    value: Deployed
    description: Live on AWS; minimalist feature set, production wiring
---

# Project Overview

> **Warning:** Do not expose GraphiQL or introspection on the public EC2 endpoint in production.
> The `prod` Spring profile disables GraphiQL; verify before go-live.

> **Useful note:** Local development can use `docker/compose.infra.yml` (DBs only) + `./gradlew bootRun`,
> or `docker/compose.full.yml` for the entire stack. See `docker/README.md`.
