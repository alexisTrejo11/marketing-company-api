---
projectId: marketing-company-api
featured: true
name: Marketing Company API
language: Java
category: Backend API
framework: Spring Boot 3.4
version: "0.0.1-SNAPSHOT"
repositoryUrl: https://github.com/alexisTrejo11/marketing-company-api
liveDemoUrl: https://{{YOUR_EC2_OR_ALB_HOST}}/api/actuator/health
description: >-
  Minimalist GraphQL backend for CRM and marketing operations. Hexagonal architecture
  on Java 23 and Spring Boot, deployed on AWS (EC2, RDS, Upstash Redis) with external
  Kafka consumption and Prometheus-ready observability.
techStack:
  - Java 23
  - Spring Boot 3.4.2
  - Spring GraphQL
  - Spring Security + JWT
  - PostgreSQL 16 (AWS RDS in production)
  - Redis (Upstash in production, local/Docker for dev)
  - Flyway
  - Gradle 8.11
  - Docker
  - AWS EC2
  - AWS RDS
  - Upstash Redis
  - Apache Kafka (cloud-managed, external consumer)
  - Prometheus / Spring Actuator
status: deployed
createdAt: "2025-01-01"
updatedAt: "2026-06-05"
---

# Project Metadata

> Portfolio metadata for the Marketing Company API. Replace `{{YOUR_EC2_OR_ALB_HOST}}` in
> `liveDemoUrl` with your public EC2 or ALB hostname when publishing the portfolio.

**Important:** This project is intentionally minimalist in scope but production-deployed on AWS.
Secrets (JWT, DB, Redis, Kafka) are never committed — see `.env.example` at the repo root.
