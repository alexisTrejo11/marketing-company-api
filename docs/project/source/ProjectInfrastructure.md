---
metrics:
  - label: Compute
    value: AWS EC2
    icon: server
    description: Spring Boot JAR deployed on EC2 (minimalist single-tier or behind ALB)
  - label: Database
    value: AWS RDS PostgreSQL 16
    icon: database
    description: Managed relational store for CRM and marketing data
  - label: Cache / sessions
    value: Upstash Redis
    icon: redis
    description: TLS Redis for auth sessions and rate-limit counters
  - label: Messaging
    value: Cloud Kafka
    icon: kafka
    description: External broker — bootstrap via KAFKA_BOOTSTRAP_SERVERS on EC2
  - label: Observability
    value: Actuator + Prometheus
    icon: chart
    description: Health and metrics scraped by external monitoring from EC2
  - label: Local dev
    value: Docker Compose
    icon: docker
    description: Full stack or infra-only compose under docker/

cloudServices:
  - name: AWS EC2
    purpose: Runs the Spring Boot API (Docker or JAR). Exposes /api/graphql and /api/actuator/*
    icon: aws-ec2
    cost: "~$10–40/mo (instance dependent) — {{VERIFY_BILLING}}"
  - name: AWS RDS (PostgreSQL 16)
    purpose: Primary database — companies, deals, campaigns, users, Flyway schema
    icon: aws-rds
    cost: "~$15–50/mo (db.t4g.micro+) — {{VERIFY_BILLING}}"
  - name: Upstash Redis
    purpose: Auth sessions, refresh-token index, GraphQL rate limiting
    icon: upstash
    cost: Free tier / pay-as-you-go — {{VERIFY_BILLING}}
  - name: Cloud Kafka (MSK or external)
    purpose: Async event consumption from upstream marketing/CRM systems
    icon: kafka
    cost: "{{YOUR_KAFKA_PROVIDER_COST}}"
  - name: External observability
    purpose: Prometheus scrape of /api/actuator/prometheus; health checks for ALB
    icon: prometheus
    cost: Depends on stack (Grafana Cloud, self-hosted, etc.)

deploymentLayers:
  - name: Edge & compute
    color: "#3B82F6"
    components:
      - name: ALB (optional)
        icon: load-balancer
        description: "{{OPTIONAL_ALB}} — HTTPS termination and health checks to EC2 targets"
      - name: EC2 instance
        icon: server
        description: Hosts marketing-company-api JAR or Docker container; Java 23 JRE
      - name: Security group
        icon: shield
        description: Inbound 443/8080 from ALB or trusted IPs; outbound to RDS, Upstash, Kafka

  - name: Data & messaging
    color: "#10B981"
    components:
      - name: RDS PostgreSQL
        icon: database
        description: JDBC URL via DB_HOST / SPRING_DATASOURCE_URL — not localhost in prod
      - name: Upstash Redis
        icon: redis
        description: SPRING_REDIS_HOST set to Upstash endpoint; TLS enabled
      - name: Kafka cluster
        icon: kafka
        description: Cloud broker; consumer group on EC2 app — KAFKA_BOOTSTRAP_SERVERS env

  - name: Observability
    color: "#8B5CF6"
    components:
      - name: Spring Actuator
        icon: heart
        description: /api/actuator/health, /metrics, /prometheus on EC2
      - name: Log files
        icon: file
        description: /app/logs on container or EC2 volume; audit subdirectory
      - name: External scraper
        icon: chart
        description: Prometheus or cloud agent polls EC2 — not bundled in minimalist deploy

  - name: Local development
    color: "#F59E0B"
    components:
      - name: docker/compose.full.yml
        icon: docker
        description: App + Postgres + Redis with service-name host overrides
      - name: docker/compose.infra.yml
        icon: docker
        description: Postgres + Redis only; app via ./gradlew bootRun on localhost ports
      - name: .env
        icon: key
        description: Secrets and connection strings — copied from .env.example

dockerFiles:
  - service: Dockerfile
    description: Multi-stage build — Gradle 8.11 + JDK 23 compile, Temurin 24 JRE runtime.
    content: |
      # Build: gradle bootJar
      # Runtime: non-root spring user, healthcheck GET /api/actuator/health
      # Context: project root (..) when built from compose
      FROM gradle:8.11-jdk23 AS build
      ...
      FROM eclipse-temurin:24-jre-alpine
      HEALTHCHECK CMD curl -f http://localhost:8080/api/actuator/health

  - service: compose.full.yml
    description: Full local stack — overrides DB/Redis hosts to postgres/redis service names.
    content: |
      services:
        app:
          environment:
            SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
            SPRING_REDIS_HOST: redis
          build:
            context: ..
            dockerfile: docker/Dockerfile

  - service: compose.infra.yml
    description: Infrastructure only — use DB_HOST=localhost and mapped ports for host-run app.
    content: |
      services:
        postgres:
          ports: ["5431:5432"]
        redis:
          ports: ["6378:6379"]
---

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
