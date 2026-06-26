# API Schema

**API type:** GraphQL

## Auth

### `POST` /api/graphql

**Mutation signUp**

Register a new user. Returns access and refresh tokens. Rate limit profile `sensitive`.

| | |
|---|---|
| **Auth required** | No |
| **Rate limit** | 7 requests / 60 seconds (sensitive) |
| **Tags** | auth |

#### Request body

**Content-Type:** `application/json`

**Schema (summary):**

```json
{
  "type": "object"
}
```

**Example:**

```json
{
  "query": "mutation($input: SignUpInput!) { signUp(input: $input) { accessToken refreshToken user { id email } } }",
  "variables": {
    "input": {
      "email": "user@example.com",
      "password": "SecurePass1!",
      "firstName": "Jane",
      "lastName": "Doe",
      "gender": "FEMALE",
      "dateOfBirth": "1990-01-01"
    }
  }
}
```

#### Responses

- **200** — AuthResponse with tokens

```json
{
  "data": {
    "signUp": {
      "accessToken": "{{jwt_access_token}}",
      "refreshToken": "{{jwt_refresh_token}}"
    }
  }
}
```

---

### `POST` /api/graphql

**Mutation login**

Authenticate with email and password. Session stored in Redis.

| | |
|---|---|
| **Auth required** | No |
| **Rate limit** | 7 requests / 60 seconds (sensitive) |
| **Tags** | auth |

#### Request body

**Content-Type:** `application/json`

**Schema (summary):**

```json
{
  "type": "object"
}
```

**Example:**

```json
{
  "query": "mutation($input: LoginInput!) { login(input: $input) { accessToken refreshToken } }",
  "variables": {
    "input": {
      "email": "user@example.com",
      "password": "SecurePass1!"
    }
  }
}
```

#### Responses

- **200** — AuthResponse

```json
{
  "data": {
    "login": {
      "accessToken": "{{jwt_access_token}}"
    }
  }
}
```

---

### `POST` /api/graphql

**Mutation refreshToken**

Exchange a valid refresh token for new access and refresh tokens.

| | |
|---|---|
| **Auth required** | No |
| **Rate limit** | 30 requests / 60 seconds (standard) |
| **Tags** | auth |

#### Request body

**Content-Type:** `application/json`

**Schema (summary):**

```json
{
  "type": "object"
}
```

**Example:**

```json
{
  "query": "mutation($input: RefreshTokenInput!) { refreshToken(input: $input) { accessToken refreshToken } }",
  "variables": {
    "input": {
      "refreshToken": "{{jwt_refresh_token}}"
    }
  }
}
```

#### Responses

- **200** — New token pair

```json
{
  "data": {
    "refreshToken": {
      "accessToken": "{{new_access_token}}"
    }
  }
}
```

---

### `POST` /api/graphql

**Mutation logout**

Invalidate the given refresh token (session).

| | |
|---|---|
| **Auth required** | Yes |
| **Rate limit** | 30 requests / 60 seconds |
| **Tags** | auth |

#### Request body

**Content-Type:** `application/json`

**Schema (summary):**

```json
{
  "type": "object"
}
```

**Example:**

```json
{
  "query": "mutation { logout(refreshToken: \"{{jwt_refresh_token}}\") }"
}
```

#### Responses

- **200** — Boolean success

```json
{
  "data": {
    "logout": true
  }
}
```

---

## Developer

### `GET` /api/graphiql

**GraphiQL IDE (development only)**

Interactive GraphQL explorer. Disabled in `prod` profile.

| | |
|---|---|
| **Auth required** | No |
| **Rate limit** | n/a |
| **Tags** | developer |

#### Responses

- **200** — GraphiQL UI (dev/docker profiles only)

```json
{
  "status": "HTML UI"
}
```

---

## Graphql

### `POST` /api/graphql

**GraphQL API (queries, mutations, subscriptions)**

Primary API. Send JSON body `{ "query": "...", "variables": {} }`. Authentication via `Authorization: Bearer <accessToken>` header. Domains include account (auth, users), CRM (companies, opportunities, deals, quotes, tasks, interactions, service packages), and marketing (campaigns, channels, activities, metrics, assets, A/B tests, interactions, attribution).

| | |
|---|---|
| **Auth required** | No |
| **Rate limit** | Global 5000 req/h + per-operation profiles (sensitive 7/min, standard 30/min) |
| **Tags** | graphql |

#### Request body

**Content-Type:** `application/json`

**Schema (summary):**

```json
{
  "type": "object",
  "properties": {
    "query": {
      "type": "string"
    },
    "variables": {
      "type": "object"
    },
    "operationName": {
      "type": "string"
    }
  }
}
```

**Example:**

```json
{
  "query": "query { healthCheck version }"
}
```

#### Responses

- **200** — GraphQL response (data and/or errors array)

```json
{
  "data": {
    "healthCheck": true,
    "version": "2.0.0"
  }
}
```

- **401** — Missing or invalid JWT on protected fields

```json
{
  "errors": [
    {
      "message": "Unauthorized"
    }
  ]
}
```

---

## Observability

### `GET` /api/actuator/health

**Spring Actuator health**

Liveness/readiness for load balancers and external monitoring. Used in production on EC2 and in Docker HEALTHCHECK.

| | |
|---|---|
| **Auth required** | No |
| **Rate limit** | none |
| **Tags** | observability |

#### Responses

- **200** — UP when DB and Redis reachable

```json
{
  "status": "UP"
}
```

---

### `GET` /api/actuator/prometheus

**Prometheus metrics scrape endpoint**

Metrics export for external observability stack (Prometheus/Grafana or cloud agent on EC2).

| | |
|---|---|
| **Auth required** | No |
| **Rate limit** | none |
| **Tags** | observability |

#### Responses

- **200** — Prometheus text format

```json
{
  "status": "# HELP jvm_memory_used_bytes ..."
}
```

---

## System

### `POST` /api/graphql

**Query healthCheck / version**

Public health and version queries defined in schema.graphqls.

| | |
|---|---|
| **Auth required** | No |
| **Rate limit** | 500 requests / 60 seconds (public) |
| **Tags** | system |

#### Request body

**Content-Type:** `application/json`

**Schema (summary):**

```json
{
  "type": "object"
}
```

**Example:**

```json
{
  "query": "query { healthCheck version }"
}
```

#### Responses

- **200** — Server operational

```json
{
  "data": {
    "healthCheck": true,
    "version": "2.0.0"
  }
}
```

---

## Additional notes

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

