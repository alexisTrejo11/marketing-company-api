# Deployment Guide

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Environment Configuration](#environment-configuration)
- [Docker Deployment](#docker-deployment)
- [Local Development Setup](#local-development-setup)
- [Production Deployment](#production-deployment)
- [Monitoring & Health Checks](#monitoring--health-checks)
- [Troubleshooting](#troubleshooting)
- [Backup & Recovery](#backup--recovery)

## Overview

This guide covers deployment options for the Marketing Company Backend, including Docker containerization, local development setup, and production deployment strategies.

### Deployment Options

1. **Docker Compose** (Recommended for development and testing)
2. **Local Development** (For active development)
3. **Kubernetes** (For production at scale)
4. **Cloud Platforms** (AWS ECS, Azure Container Instances, Google Cloud Run)

## Prerequisites

### Required Software

| Software       | Minimum Version | Purpose                       |
| -------------- | --------------- | ----------------------------- |
| Java JDK       | 23              | Runtime environment           |
| Gradle         | 8.11+           | Build tool                    |
| Docker         | 20.10+          | Containerization              |
| Docker Compose | 2.0+            | Multi-container orchestration |
| PostgreSQL     | 16              | Database (if running locally) |
| Redis          | 7               | Cache (if running locally)    |

### System Requirements

#### Development Environment

- **CPU**: 2 cores minimum, 4 cores recommended
- **RAM**: 4GB minimum, 8GB recommended
- **Disk**: 10GB free space
- **OS**: Linux, macOS, or Windows with WSL2

#### Production Environment

- **CPU**: 4 cores minimum, 8 cores recommended
- **RAM**: 8GB minimum, 16GB recommended
- **Disk**: 50GB free space with SSD
- **OS**: Linux (Ubuntu 22.04 LTS or similar)

## Environment Configuration

### 1. Create Environment File

Create a `.env` file in the project root directory:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=marketing_company_db
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password_here

# PostgreSQL Docker Configuration
POSTGRES_DB=marketing_company_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password_here

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_change_this_in_production_min_256_bits
JWT_ACCESS_TOKEN_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=2592000000

# Application Configuration
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080

# Logging
LOG_LEVEL=INFO
LOG_FILE_PATH=/app/logs
```

### 2. Security Considerations

⚠️ **IMPORTANT**: Never commit `.env` files to version control!

Add to `.gitignore`:

```gitignore
.env
.env.local
.env.production
*.log
```

### 3. Environment-Specific Configuration

#### Development (`.env.dev`)

```env
SPRING_PROFILES_ACTIVE=dev
LOG_LEVEL=DEBUG
SPRING_JPA_SHOW_SQL=true
```

#### Testing (`.env.test`)

```env
SPRING_PROFILES_ACTIVE=test
DB_NAME=marketing_company_test_db
```

#### Production (`.env.prod`)

```env
SPRING_PROFILES_ACTIVE=prod
LOG_LEVEL=WARN
SPRING_JPA_SHOW_SQL=false
JWT_SECRET=<use_strong_secret_from_secrets_manager>
DB_PASSWORD=<use_strong_password_from_secrets_manager>
```

## Docker Deployment

### Architecture

```
┌─────────────────────────────────────────┐
│          Docker Network                  │
│                                          │
│  ┌──────────┐  ┌──────────┐  ┌────────┐│
│  │PostgreSQL│  │  Redis   │  │  App   ││
│  │   :5432  │  │  :6379   │  │ :8080  ││
│  └──────────┘  └──────────┘  └────────┘│
│       │             │            │      │
│       └─────────────┴────────────┘      │
│                                          │
└─────────────────────────────────────────┘
```

### Docker Compose Setup

The `docker-compose.yml` defines three services:

1. **postgres**: PostgreSQL database
2. **redis**: Redis cache
3. **app**: Spring Boot application

### 1. Quick Start

```bash
# Clone the repository
git clone <repository-url>
cd backend_marketing_company

# Create .env file
cp .env.example .env
# Edit .env with your configuration

# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Check service status
docker-compose ps
```

### 2. Service Management

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose stop

# Stop and remove containers
docker-compose down

# Stop and remove containers, volumes, and images
docker-compose down -v --rmi all

# Restart a specific service
docker-compose restart app

# View logs for specific service
docker-compose logs -f app

# Execute command in running container
docker-compose exec app bash
```

### 3. Database Initialization

The database is automatically initialized on first run:

1. PostgreSQL container starts
2. Flyway migrations run automatically
3. Database schema is created
4. Sample data is loaded (if configured)

To manually trigger migrations:

```bash
docker-compose exec app ./gradlew flywayMigrate
```

### 4. Health Checks

Health checks are configured in `docker-compose.yml`:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 30s
  timeout: 3s
  start-period: 5s
  retries: 3
```

Check health status:

```bash
docker-compose ps
curl http://localhost:8080/actuator/health
```

### 5. Scaling

Scale the application horizontally:

```bash
# Scale to 3 instances
docker-compose up -d --scale app=3

# Note: You'll need a load balancer (nginx, traefik) in front
```

### 6. Building Custom Images

#### Development Build

```bash
# Build image
docker build -t marketing-company-backend:dev .

# Run with custom image
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  marketing-company-backend:dev
```

#### Production Build

```bash
# Multi-stage build optimized for production
docker build \
  --target=production \
  -t marketing-company-backend:latest \
  .

# Tag for registry
docker tag marketing-company-backend:latest \
  registry.example.com/marketing-company-backend:1.0.0

# Push to registry
docker push registry.example.com/marketing-company-backend:1.0.0
```

## Local Development Setup

### 1. Prerequisites Setup

```bash
# Install SDKMAN (for Java management)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install Java 23
sdk install java 23-open

# Verify installation
java -version
```

### 2. Database Setup

#### Option A: Use Docker for Database Only

```bash
# Start only PostgreSQL and Redis
docker-compose up -d postgres redis

# Verify services are running
docker-compose ps
```

#### Option B: Install PostgreSQL Locally

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql-16 postgresql-contrib

# Start PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Create database
sudo -u postgres createdb marketing_company_db
sudo -u postgres psql -c "CREATE USER postgres WITH PASSWORD 'postgres';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE marketing_company_db TO postgres;"
```

#### Option C: Install Redis Locally

```bash
# Ubuntu/Debian
sudo apt install redis-server

# Start Redis
sudo systemctl start redis-server
sudo systemctl enable redis-server

# Verify
redis-cli ping
```

### 3. Build and Run

```bash
# Clean build
./gradlew clean build

# Run application
./gradlew bootRun

# Or run JAR directly
java -jar build/libs/*.jar

# With specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### 4. Development Tools

#### Hot Reload with Spring DevTools

Already included in `build.gradle`. Changes to Java files trigger automatic restart.

#### Interactive GraphQL IDE

Access GraphiQL at: http://localhost:8080/graphiql

#### Database Migrations

```bash
# Check migration status
./gradlew flywayInfo

# Run pending migrations
./gradlew flywayMigrate

# Validate migrations
./gradlew flywayValidate

# Clean database (DANGER: Drops all objects)
./gradlew flywayClean
```

## Production Deployment

### 1. Pre-Deployment Checklist

- [ ] Update `.env` with production values
- [ ] Set strong JWT secret (min 256 bits)
- [ ] Use secure database passwords
- [ ] Configure proper CORS origins
- [ ] Set `SPRING_PROFILES_ACTIVE=prod`
- [ ] Disable GraphiQL (`graphql.graphiql.enabled=false`)
- [ ] Configure SSL/TLS certificates
- [ ] Set up database backups
- [ ] Configure monitoring and logging
- [ ] Review security configuration
- [ ] Test with production-like data
- [ ] Prepare rollback plan

### 2. Build Production Image

```bash
# Build optimized production image
./gradlew clean build -x test

docker build \
  --build-arg JAR_FILE=build/libs/*.jar \
  -t marketing-company-backend:1.0.0 \
  -f dockerfile \
  .

# Test image locally
docker run -p 8080:8080 \
  --env-file .env.prod \
  marketing-company-backend:1.0.0
```

### 3. Docker Compose Production

Create `docker-compose.prod.yml`:

```yaml
version: "3.8"

services:
  postgres:
    image: postgres:16-alpine
    restart: always
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./backups:/backups
    networks:
      - backend-network
    deploy:
      resources:
        limits:
          cpus: "2"
          memory: 4G
        reservations:
          cpus: "1"
          memory: 2G

  redis:
    image: redis:7-alpine
    restart: always
    command: redis-server --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis_data:/data
    networks:
      - backend-network
    deploy:
      resources:
        limits:
          cpus: "1"
          memory: 2G

  app:
    image: marketing-company-backend:1.0.0
    restart: always
    depends_on:
      - postgres
      - redis
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
      SPRING_DATASOURCE_USERNAME: ${POSTGRES_USER}
      SPRING_DATASOURCE_PASSWORD: ${POSTGRES_PASSWORD}
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PASSWORD: ${REDIS_PASSWORD}
    ports:
      - "8080:8080"
    networks:
      - backend-network
    volumes:
      - ./logs:/app/logs
    deploy:
      replicas: 2
      resources:
        limits:
          cpus: "2"
          memory: 4G
        reservations:
          cpus: "1"
          memory: 2G
      restart_policy:
        condition: on-failure
        delay: 5s
        max_attempts: 3

  nginx:
    image: nginx:alpine
    restart: always
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./ssl:/etc/nginx/ssl
    depends_on:
      - app
    networks:
      - backend-network

networks:
  backend-network:
    driver: bridge

volumes:
  postgres_data:
  redis_data:
```

Deploy:

```bash
docker-compose -f docker-compose.prod.yml up -d
```

### 4. Kubernetes Deployment

Create Kubernetes manifests:

#### deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: marketing-company-backend
  namespace: production
spec:
  replicas: 3
  selector:
    matchLabels:
      app: marketing-company-backend
  template:
    metadata:
      labels:
        app: marketing-company-backend
    spec:
      containers:
        - name: app
          image: registry.example.com/marketing-company-backend:1.0.0
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "prod"
            - name: SPRING_DATASOURCE_URL
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: url
            - name: SPRING_DATASOURCE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: password
          resources:
            requests:
              memory: "2Gi"
              cpu: "1"
            limits:
              memory: "4Gi"
              cpu: "2"
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8080
            initialDelaySeconds: 30
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8080
            initialDelaySeconds: 20
            periodSeconds: 5
```

#### service.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: marketing-company-backend
  namespace: production
spec:
  selector:
    app: marketing-company-backend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

Deploy to Kubernetes:

```bash
# Create namespace
kubectl create namespace production

# Create secrets
kubectl create secret generic db-secret \
  --from-literal=url=jdbc:postgresql://... \
  --from-literal=password=... \
  -n production

# Apply manifests
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml

# Check status
kubectl get pods -n production
kubectl get services -n production
```

### 5. Cloud Platform Deployment

#### AWS ECS

```bash
# Install AWS CLI
pip install awscli

# Configure AWS credentials
aws configure

# Push image to ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin \
  <account-id>.dkr.ecr.us-east-1.amazonaws.com

docker tag marketing-company-backend:1.0.0 \
  <account-id>.dkr.ecr.us-east-1.amazonaws.com/marketing-backend:1.0.0

docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/marketing-backend:1.0.0

# Deploy using ECS task definition
aws ecs update-service \
  --cluster marketing-cluster \
  --service marketing-backend-service \
  --force-new-deployment
```

#### Azure Container Instances

```bash
# Login to Azure
az login

# Create resource group
az group create \
  --name marketing-company-rg \
  --location eastus

# Create container
az container create \
  --resource-group marketing-company-rg \
  --name marketing-backend \
  --image registry.example.com/marketing-company-backend:1.0.0 \
  --cpu 2 \
  --memory 4 \
  --ports 8080 \
  --environment-variables \
    SPRING_PROFILES_ACTIVE=prod
```

## Monitoring & Health Checks

### 1. Health Endpoints

The application exposes several health endpoints:

```bash
# Overall health
curl http://localhost:8080/actuator/health

# Detailed health (requires authentication)
curl http://localhost:8080/actuator/health \
  -H "Authorization: Bearer <token>"

# Liveness probe (for Kubernetes)
curl http://localhost:8080/actuator/health/liveness

# Readiness probe (for Kubernetes)
curl http://localhost:8080/actuator/health/readiness
```

### 2. Metrics

Expose metrics using Spring Boot Actuator:

```bash
# Metrics endpoint
curl http://localhost:8080/actuator/metrics

# Specific metric
curl http://localhost:8080/actuator/metrics/jvm.memory.used
```

### 3. Logging

Logs are stored in `/app/logs` directory:

```bash
# View application logs
docker-compose logs -f app

# View specific log file
tail -f logs/application.log

# Search for errors
grep ERROR logs/application.log

# Audit logs
tail -f logs/audit/audit.log
```

### 4. Prometheus & Grafana

Add to `docker-compose.yml`:

```yaml
prometheus:
  image: prom/prometheus
  volumes:
    - ./prometheus.yml:/etc/prometheus/prometheus.yml
  ports:
    - "9090:9090"

grafana:
  image: grafana/grafana
  ports:
    - "3000:3000"
  environment:
    - GF_SECURITY_ADMIN_PASSWORD=admin
```

## Troubleshooting

### Common Issues

#### 1. Application Won't Start

```bash
# Check logs
docker-compose logs app

# Common causes:
# - Database not ready
# - Invalid configuration
# - Port already in use

# Solution: Check database connection
docker-compose exec app nc -zv postgres 5432
```

#### 2. Database Connection Failed

```bash
# Verify PostgreSQL is running
docker-compose ps postgres

# Check PostgreSQL logs
docker-compose logs postgres

# Test connection
docker-compose exec postgres psql -U postgres -d marketing_company_db

# Verify environment variables
docker-compose exec app env | grep DB_
```

#### 3. Out of Memory

```bash
# Check memory usage
docker stats

# Increase memory limit in docker-compose.yml
deploy:
  resources:
    limits:
      memory: 8G
```

#### 4. Slow Performance

```bash
# Check database connection pool
# In application.yml, adjust:
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 10

# Check for slow queries
docker-compose exec postgres psql -U postgres -d marketing_company_db
SELECT * FROM pg_stat_statements ORDER BY mean_exec_time DESC LIMIT 10;
```

### Debug Mode

Enable debug logging:

```bash
# In .env or application.yml
LOG_LEVEL=DEBUG
SPRING_JPA_SHOW_SQL=true
```

## Backup & Recovery

### 1. Database Backup

#### Automated Backup Script

Create `backup.sh`:

```bash
#!/bin/bash
BACKUP_DIR="./backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/backup_$DATE.sql"

mkdir -p $BACKUP_DIR

docker-compose exec -T postgres pg_dump \
  -U postgres \
  marketing_company_db > $BACKUP_FILE

gzip $BACKUP_FILE

# Keep only last 7 days
find $BACKUP_DIR -name "backup_*.sql.gz" -mtime +7 -delete

echo "Backup completed: $BACKUP_FILE.gz"
```

Run backup:

```bash
chmod +x backup.sh
./backup.sh

# Schedule with cron
crontab -e
# Add: 0 2 * * * /path/to/backup.sh
```

### 2. Database Restore

```bash
# From compressed backup
gunzip -c backups/backup_20260115_020000.sql.gz | \
  docker-compose exec -T postgres psql \
    -U postgres \
    -d marketing_company_db

# From uncompressed backup
docker-compose exec -T postgres psql \
  -U postgres \
  -d marketing_company_db < backups/backup_20260115_020000.sql
```

### 3. Redis Backup

```bash
# Manual backup
docker-compose exec redis redis-cli SAVE

# Copy RDB file
docker cp backend-redis:/data/dump.rdb ./backups/redis_backup.rdb

# Restore
docker cp ./backups/redis_backup.rdb backend-redis:/data/dump.rdb
docker-compose restart redis
```

### 4. Disaster Recovery Plan

1. **Regular Backups**: Automated daily backups
2. **Offsite Storage**: Store backups in cloud storage (S3, Azure Blob)
3. **Test Restores**: Regularly test backup restoration
4. **Documentation**: Keep recovery procedures documented
5. **Monitoring**: Alert on backup failures

---

## Quick Reference

### Common Commands

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Restart application
docker-compose restart app

# Database backup
./backup.sh

# Check health
curl http://localhost:8080/actuator/health

# View running containers
docker-compose ps

# Stop all services
docker-compose down
```

### Useful URLs

- GraphQL API: http://localhost:8080/graphql
- GraphiQL IDE: http://localhost:8080/graphiql
- Health Check: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics

---

For additional help, consult the [README.md](README.md) and [ARCHITECTURE.md](ARCHITECTURE.md) documentation.
