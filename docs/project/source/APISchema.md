---
type: GraphQL
httpEndpoints:
  - id: graphql-main
    method: POST
    urlPath: /api/graphql
    summary: GraphQL API (queries, mutations, subscriptions)
    description: >-
      Primary API. Send JSON body `{ "query": "...", "variables": {} }`.
      Authentication via `Authorization: Bearer <accessToken>` header.
      Domains include account (auth, users), CRM (companies, opportunities, deals,
      quotes, tasks, interactions, service packages), and marketing (campaigns,
      channels, activities, metrics, assets, A/B tests, interactions, attribution).
    tags:
      - graphql
    authenticated: false
    rateLimit: Global 5000 req/h + per-operation profiles (sensitive 7/min, standard 30/min)
    requestBody:
      contentType: application/json
      schema:
        type: object
        properties:
          query:
            type: string
          variables:
            type: object
          operationName:
            type: string
      example:
        query: "query { healthCheck version }"
    responses:
      - status: 200
        description: GraphQL response (data and/or errors array)
        example:
          data:
            healthCheck: true
            version: "2.0.0"
      - status: 401
        description: Missing or invalid JWT on protected fields
        example:
          errors:
            - message: Unauthorized

  - id: auth-signup
    method: POST
    urlPath: /api/graphql
    summary: Mutation signUp
    description: Register a new user. Returns access and refresh tokens. Rate limit profile `sensitive`.
    tags:
      - auth
    authenticated: false
    rateLimit: 7 requests / 60 seconds (sensitive)
    requestBody:
      contentType: application/json
      schema:
        type: object
      example:
        query: "mutation($input: SignUpInput!) { signUp(input: $input) { accessToken refreshToken user { id email } } }"
        variables:
          input:
            email: user@example.com
            password: "SecurePass1!"
            firstName: Jane
            lastName: Doe
            gender: FEMALE
            dateOfBirth: "1990-01-01"
    responses:
      - status: 200
        description: AuthResponse with tokens
        example:
          data:
            signUp:
              accessToken: "{{jwt_access_token}}"
              refreshToken: "{{jwt_refresh_token}}"

  - id: auth-login
    method: POST
    urlPath: /api/graphql
    summary: Mutation login
    description: Authenticate with email and password. Session stored in Redis.
    tags:
      - auth
    authenticated: false
    rateLimit: 7 requests / 60 seconds (sensitive)
    requestBody:
      contentType: application/json
      schema:
        type: object
      example:
        query: "mutation($input: LoginInput!) { login(input: $input) { accessToken refreshToken } }"
        variables:
          input:
            email: user@example.com
            password: "SecurePass1!"
    responses:
      - status: 200
        description: AuthResponse
        example:
          data:
            login:
              accessToken: "{{jwt_access_token}}"

  - id: auth-refresh
    method: POST
    urlPath: /api/graphql
    summary: Mutation refreshToken
    description: Exchange a valid refresh token for new access and refresh tokens.
    tags:
      - auth
    authenticated: false
    rateLimit: 30 requests / 60 seconds (standard)
    requestBody:
      contentType: application/json
      schema:
        type: object
      example:
        query: "mutation($input: RefreshTokenInput!) { refreshToken(input: $input) { accessToken refreshToken } }"
        variables:
          input:
            refreshToken: "{{jwt_refresh_token}}"
    responses:
      - status: 200
        description: New token pair
        example:
          data:
            refreshToken:
              accessToken: "{{new_access_token}}"

  - id: auth-logout
    method: POST
    urlPath: /api/graphql
    summary: Mutation logout
    description: Invalidate the given refresh token (session).
    tags:
      - auth
    authenticated: true
    rateLimit: 30 requests / 60 seconds
    requestBody:
      contentType: application/json
      schema:
        type: object
      example:
        query: 'mutation { logout(refreshToken: "{{jwt_refresh_token}}") }'
    responses:
      - status: 200
        description: Boolean success
        example:
          data:
            logout: true

  - id: graphql-health
    method: POST
    urlPath: /api/graphql
    summary: Query healthCheck / version
    description: Public health and version queries defined in schema.graphqls.
    tags:
      - system
    authenticated: false
    rateLimit: 500 requests / 60 seconds (public)
    requestBody:
      contentType: application/json
      schema:
        type: object
      example:
        query: "query { healthCheck version }"
    responses:
      - status: 200
        description: Server operational
        example:
          data:
            healthCheck: true
            version: "2.0.0"

  - id: actuator-health
    method: GET
    urlPath: /api/actuator/health
    summary: Spring Actuator health
    description: >-
      Liveness/readiness for load balancers and external monitoring.
      Used in production on EC2 and in Docker HEALTHCHECK.
    tags:
      - observability
    authenticated: false
    rateLimit: none
    responses:
      - status: 200
        description: UP when DB and Redis reachable
        example:
          status: UP

  - id: actuator-prometheus
    method: GET
    urlPath: /api/actuator/prometheus
    summary: Prometheus metrics scrape endpoint
    description: >-
      Metrics export for external observability stack (Prometheus/Grafana or cloud agent on EC2).
    tags:
      - observability
    authenticated: false
    rateLimit: none
    responses:
      - status: 200
        description: Prometheus text format
        example:
          status: "# HELP jvm_memory_used_bytes ..."

  - id: graphiql-dev
    method: GET
    urlPath: /api/graphiql
    summary: GraphiQL IDE (development only)
    description: Interactive GraphQL explorer. Disabled in `prod` profile.
    tags:
      - developer
    authenticated: false
    rateLimit: n/a
    responses:
      - status: 200
        description: GraphiQL UI (dev/docker profiles only)
        example:
          status: HTML UI
---

# API Schema

> **Important:** All GraphQL operations share `POST /api/graphql`. The server uses a servlet
> context path of `/api` — do not omit it when calling production (EC2/ALB).

> **Warning:** Auth mutations (`signUp`, `login`) use the `sensitive` rate-limit profile.
> Brute-force protection depends on Redis being available (Upstash in AWS).

> **Useful note:** GraphQL field-level auth uses `@authenticated` and `@requiresRole` directives.
> See `src/main/resources/graphql/` for the full schema; CRM and marketing modules extend base
> `Query` and `Mutation` types.

### Domain operation groups (GraphQL)

| Tag | Areas |
|-----|--------|
| **auth** | signUp, login, refreshToken, logout, logoutAll |
| **account** | User profile, admin user management |
| **crm** | Companies, opportunities, deals, quotes, tasks, interactions, service packages |
| **marketing** | Campaigns, channels, activities, metrics, assets, A/B tests, campaign interactions, attribution |

Placeholder for Kafka-driven consumers: event handlers are wired to an external cloud Kafka
instance (`KAFKA_BOOTSTRAP_SERVERS={{YOUR_KAFKA_BROKER}}`) — not exposed as HTTP endpoints.
