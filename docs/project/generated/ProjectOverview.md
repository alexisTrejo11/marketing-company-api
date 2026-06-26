# Project Overview

## Unified CRM and marketing data without a monolithic frontend lock-in

Marketing agencies and CRM teams need a single backend that models customers, sales pipelines, campaigns, and attribution — while staying API-first, secure, and deployable to cloud infrastructure without coupling to a specific UI framework.

### Pain points

- Fragmented CRM and marketing data across spreadsheets and ad-hoc tools
- Need for JWT-authenticated, rate-limited APIs suitable for multiple clients
- Session and cache requirements that differ between local Docker and cloud (Upstash)
- Operational need for health checks, metrics, and event consumption from managed Kafka

## GraphQL CRM + marketing API on hexagonal Spring Boot

- **Single GraphQL entrypoint** — One schema spanning account, CRM, customer, and marketing bounded contexts at `/api/graphql`.
- **Hexagonal / DDD modules** — Ports and adapters per domain — GraphQL in, JPA/Redis out — keeping business logic testable.
- **JWT + Redis sessions** — Access/refresh tokens with session state stored in Redis (Upstash in AWS).
- **Cloud-native deployment** — App on EC2, PostgreSQL on RDS, Redis on Upstash, Kafka consumed from a cloud broker; Actuator exposes Prometheus metrics.

## At a glance

- 22 GraphQL schema modules under src/main/resources/graphql/
- 750+ Java source files across CRM, marketing, account, and shared layers
- GraphQL API prefix /api with context path and Actuator at /api/actuator/*
- Deployed on AWS — EC2 application tier, RDS PostgreSQL, Upstash Redis
- External observability via Spring Actuator (health, metrics, prometheus)

## Links

| Resource | URL |
| --- | --- |
| Github | https://github.com/alexisTrejo11/marketing-company-api |
| Demo | https://{{YOUR_EC2_OR_ALB_HOST}}/api/actuator/health |
| Documentation | docs/project/generated/README.md |
| Dockerhub | None |

## Architecture & API

Placeholder gallery — add screenshots or diagrams when available.

## Metrics

| Label | Value | Description |
| --- | --- | --- |
| API style | GraphQL | Spring GraphQL with schema-first `.graphqls` files |
| Runtime | Java 23 | Eclipse Temurin JRE in Docker; EC2 in production |
| Database | PostgreSQL 16 | AWS RDS in production; Docker/local for development |
| Cache / sessions | Redis | Upstash in production; Docker Redis for local full stack |
| Messaging | Kafka (external) | Consumed from cloud-managed broker — bootstrap URL via env (not in repo) |
| Status | Deployed | Live on AWS; minimalist feature set, production wiring |

## Additional notes

# Project Overview

> **Warning:** Do not expose GraphiQL or introspection on the public EC2 endpoint in production.
> The `prod` Spring profile disables GraphiQL; verify before go-live.

> **Useful note:** Local development can use `docker/compose.infra.yml` (DBs only) + `./gradlew bootRun`,
> or `docker/compose.full.yml` for the entire stack. See `docker/README.md`.

