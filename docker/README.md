# Docker setup

All container configuration for **Marketing Company API** lives in this folder.

| File | Purpose |
|------|---------|
| `Dockerfile` | Multi-stage build (Gradle → JRE) for the Spring Boot app |
| `compose.full.yml` | **Local full stack** — app + PostgreSQL + Redis |
| `compose.deploy.yml` | **Deploy** — app container only (RDS, Upstash, cloud Kafka via `.env`) |

---

## Environment variables

| Variable | Purpose |
|----------|---------|
| `DB_*` | PostgreSQL — RDS in deploy; overridden to `postgres` in full stack |
| `REDIS_URL` | Redis — Upstash `rediss://…` in deploy; overridden to `redis://redis:6379` in full stack |
| `DB_HOST_PORT` / `REDIS_HOST_PORT` | Host port mappings — **full stack only** |
| `JWT_*`, `CORS_*`, `KAFKA_BOOTSTRAP_SERVERS` | App config from `.env` |

```bash
cp .env.example .env
```

---

## Option 1 — Local full stack (`compose.full.yml`)

Runs **app + PostgreSQL + Redis** for offline local development. Compose overrides DB and Redis to Docker service names so the app never uses Upstash/RDS from `.env`.

```bash
docker compose -f docker/compose.full.yml --env-file .env up --build
```

| Service | Default URL |
|---------|-------------|
| GraphQL API | http://localhost:8081/api/graphql |
| Health | http://localhost:8081/api/actuator/health |
| PostgreSQL (host) | `localhost:5431` |
| Redis (host) | `localhost:6378` |

```bash
docker compose -f docker/compose.full.yml --env-file .env down
docker compose -f docker/compose.full.yml --env-file .env down -v   # removes DB volumes
```

---

## Option 2 — Deploy app only (`compose.deploy.yml`)

Runs **only the API container**. Database, Redis, and Kafka come from your `.env` (AWS RDS, Upstash, cloud broker). Use on EC2 or any host with Docker.

Ensure `.env` has cloud endpoints:

```env
SPRING_PROFILES_ACTIVE=prod

DB_HOST=your-rds-endpoint.region.rds.amazonaws.com
DB_PORT=5432
DB_NAME=postgres
DB_USERNAME=your_user
DB_PASSWORD=your_password

REDIS_URL=rediss://default:YOUR_PASSWORD@your-endpoint.upstash.io:6379

JWT_ACCESS_SECRET=...
JWT_REFRESH_SECRET=...
```

```bash
docker compose -f docker/compose.deploy.yml --env-file .env up --build -d
```

| Service | Default URL |
|---------|-------------|
| GraphQL API | http://localhost:8080/api/graphql |
| Health | http://localhost:8080/api/actuator/health |

```bash
docker compose -f docker/compose.deploy.yml --env-file .env logs -f app
docker compose -f docker/compose.deploy.yml --env-file .env down
```

---

## Connection reference

| Scenario | Compose file | `DB_HOST` | `REDIS_URL` |
|----------|--------------|-----------|-------------|
| **Local full stack** | `compose.full.yml` | `postgres` (override) | `redis://redis:6379` (override) |
| **Deploy / EC2** | `compose.deploy.yml` | RDS from `.env` | Upstash from `.env` |
| **IDE / `./gradlew bootRun`** | none | RDS from `.env` | Upstash from `.env` |

---

## Optional: SQL init scripts

Place `.sql` files in `init-scripts/` at the project root. Used by **full stack** Postgres on first boot only.

---

## Troubleshooting

**Full stack — app cannot connect to Postgres/Redis**

- Use `compose.full.yml`, not `compose.deploy.yml`.
- `docker compose -f docker/compose.full.yml logs app`

**Deploy — connection refused to RDS or Upstash**

- Verify `.env` endpoints and security group / Upstash allowlist for the host IP.
- `docker compose -f docker/compose.deploy.yml exec app env | grep -E 'DB_|REDIS'`

**Port in use** — adjust in `.env`:

```env
APP_HOST_PORT=8080
DB_HOST_PORT=5431
REDIS_HOST_PORT=6378
```
