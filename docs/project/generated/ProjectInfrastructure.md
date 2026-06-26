# Infrastructure

## Metrics

| Label | Value | Description |
| --- | --- | --- |
| Compute | AWS EC2 | Spring Boot JAR deployed on EC2 (minimalist single-tier or behind ALB) |
| Database | AWS RDS PostgreSQL 16 | Managed relational store for CRM and marketing data |
| Cache / sessions | Upstash Redis | TLS Redis for auth sessions and rate-limit counters |
| Messaging | Cloud Kafka | External broker — bootstrap via KAFKA_BOOTSTRAP_SERVERS on EC2 |
| Observability | Actuator + Prometheus | Health and metrics scraped by external monitoring from EC2 |
| Local dev | Docker Compose | Full stack or infra-only compose under docker/ |

## Cloud services

| Service | Purpose | Est. cost |
| --- | --- | --- |
| AWS EC2 | Runs the Spring Boot API (Docker or JAR). Exposes /api/graphql and /api/actuator/* | ~$10–40/mo (instance dependent) — {{VERIFY_BILLING}} |
| AWS RDS (PostgreSQL 16) | Primary database — companies, deals, campaigns, users, Flyway schema | ~$15–50/mo (db.t4g.micro+) — {{VERIFY_BILLING}} |
| Upstash Redis | Auth sessions, refresh-token index, GraphQL rate limiting | Free tier / pay-as-you-go — {{VERIFY_BILLING}} |
| Cloud Kafka (MSK or external) | Async event consumption from upstream marketing/CRM systems | {{YOUR_KAFKA_PROVIDER_COST}} |
| External observability | Prometheus scrape of /api/actuator/prometheus; health checks for ALB | Depends on stack (Grafana Cloud, self-hosted, etc.) |

## Deployment layers

### Edge & compute

- **ALB (optional)** — {{OPTIONAL_ALB}} — HTTPS termination and health checks to EC2 targets
- **EC2 instance** — Hosts marketing-company-api JAR or Docker container; Java 23 JRE
- **Security group** — Inbound 443/8080 from ALB or trusted IPs; outbound to RDS, Upstash, Kafka

### Data & messaging

- **RDS PostgreSQL** — JDBC URL via DB_HOST / SPRING_DATASOURCE_URL — not localhost in prod
- **Upstash Redis** — SPRING_REDIS_HOST set to Upstash endpoint; TLS enabled
- **Kafka cluster** — Cloud broker; consumer group on EC2 app — KAFKA_BOOTSTRAP_SERVERS env

### Observability

- **Spring Actuator** — /api/actuator/health, /metrics, /prometheus on EC2
- **Log files** — /app/logs on container or EC2 volume; audit subdirectory
- **External scraper** — Prometheus or cloud agent polls EC2 — not bundled in minimalist deploy

### Local development

- **docker/compose.full.yml** — App + Postgres + Redis with service-name host overrides
- **docker/compose.infra.yml** — Postgres + Redis only; app via ./gradlew bootRun on localhost ports
- **.env** — Secrets and connection strings — copied from .env.example

## Docker configuration

### Dockerfile

Multi-stage build — Gradle 8.11 + JDK 23 compile, Temurin 24 JRE runtime.

```yaml
# Build: gradle bootJar
# Runtime: non-root spring user, healthcheck GET /api/actuator/health
# Context: project root (..) when built from compose
FROM gradle:8.11-jdk23 AS build
...
FROM eclipse-temurin:24-jre-alpine
HEALTHCHECK CMD curl -f http://localhost:8080/api/actuator/health
```

### compose.full.yml

Full local stack — overrides DB/Redis hosts to postgres/redis service names.

```yaml
services:
  app:
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
      SPRING_REDIS_HOST: redis
    build:
      context: ..
      dockerfile: docker/Dockerfile
```

### compose.infra.yml

Infrastructure only — use DB_HOST=localhost and mapped ports for host-run app.

```yaml
services:
  postgres:
    ports: ["5431:5432"]
  redis:
    ports: ["6378:6379"]
```

## Additional notes

# Project Infrastructure

> **Important — production env on EC2:** Set `SPRING_PROFILES_ACTIVE=prod` and point JDBC/Redis
> to RDS and Upstash endpoints. Do **not** use `localhost` for DB or Redis in AWS.

> **Warning:** Upstash requires TLS — ensure Redis client URL/port match your Upstash dashboard.
> Wrong host causes auth failures and broken sessions.

> **Useful note — local vs cloud:**

| Variable | Local (infra compose + bootRun) | AWS production |
|----------|--------------------------------|----------------|
| `DB_HOST` | `localhost` | RDS endpoint |
| `DB_PORT` | `5431` (mapped) | `5432` |
| `SPRING_REDIS_HOST` | `localhost` | Upstash host |
| `SPRING_REDIS_PORT` | `6378` (mapped) | Upstash port (often 6379) |
| `KAFKA_BOOTSTRAP_SERVERS` | `{{LOCAL_OR_DISABLED}}` | Cloud broker URL |

> **Deploy checklist (EC2):**
> 1. Build JAR or pull Docker image
> 2. Configure `.env` / systemd / user-data with RDS, Upstash, JWT secrets, Kafka bootstrap
> 3. Open security group egress to RDS, Upstash, Kafka
> 4. Verify `curl https://{{HOST}}/api/actuator/health`
> 5. Point Prometheus scraper at `/api/actuator/prometheus`

Placeholder URLs: `{{YOUR_EC2_OR_ALB_HOST}}`, `{{YOUR_RDS_ENDPOINT}}`, `{{YOUR_UPSTASH_REDIS_URL}}`, `{{YOUR_KAFKA_BROKER}}`

