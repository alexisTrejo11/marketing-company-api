# Code Showcase

## Auth command handler (application core)

Login flow orchestrates user lookup, password validation, token generation, and Redis session persistence — domain stays free of GraphQL/JPA details.

**Category:** authentication | **Duration:** 5 min read | **Tags:** hexagonal, jwt, redis

### AuthCommandHandler.java

**Path:** `src/main/java/at/backend/MarketingCompany/account/auth/core/application/AuthCommandHandler.java`

Core use case — validates credentials and creates session via ports.

```java
@Transactional
public AuthResult handleLogin(LoginCommand command) {
  var user = userRepository.findByEmail(command.email())
      .orElseThrow(InvalidCredentialsException::new);
  validatePassword(command.password(), user.getHashedPassword());
  var accessToken = tokenProvider.generateAccessToken(
      user.getId().value(), user.getEmail().value(), user.getRoles());
  var refreshToken = tokenProvider.generateRefreshToken(user.getId().value());
  authSessionRepository.save(buildSession(user, refreshToken, command));
  return AuthResult.of(accessToken, refreshToken, user);
}
```

## GraphQL auth mutations

Input adapter exposing signUp, login, refresh, and logout with sensitive rate limits.

**Category:** api | **Duration:** 3 min read | **Tags:** graphql, spring

### AuthController.java

**Path:** `src/main/java/at/backend/MarketingCompany/account/auth/adapters/inbound/controller/AuthController.java`

Maps GraphQL mutations to commands; captures client IP and user agent.

```java
@MutationMapping
@GraphQLRateLimit("sensitive")
public AuthResponse login(
    @Valid @Argument LoginInput input,
    @ContextValue(name = "userAgent") String userAgent,
    @ContextValue(name = "clientIp") String clientIp) {
  LoginCommand command = input.toCommand(userAgent, clientIp);
  AuthResult result = authService.handleLogin(command);
  return authResponseMapper.toAuthResponse(result);
}
```

## Docker Compose host overrides

Full local stack forces postgres/redis service names so the app container never connects to localhost for data stores.

**Category:** integration | **Duration:** 2 min read | **Tags:** docker, devops

### compose.full.yml (excerpt)

**Path:** `docker/compose.full.yml`

Environment block overrides .env localhost values inside the app service.

```yaml
app:
  environment:
    SPRING_PROFILES_ACTIVE: docker
    SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
    SPRING_REDIS_HOST: redis
    DB_HOST: postgres
```

## Environment-driven secrets

JWT, database, Redis, CORS, and Kafka settings come from .env — loaded by spring-dotenv locally and passed to EC2/Docker in production.

**Category:** security | **Duration:** 2 min read | **Tags:** config, aws

### application.yml (auth excerpt)

**Path:** `src/main/resources/application.yml`

Production profile requires JWT secrets without insecure defaults.

```yaml
app:
  auth:
    jwt:
      access-secret: ${JWT_ACCESS_SECRET}
      refresh-secret: ${JWT_REFRESH_SECRET}
      access-expiration-minutes: ${JWT_ACCESS_EXPIRATION_MINUTES:15}
      refresh-expiration-days: ${JWT_REFRESH_EXPIRATION_DAYS:30}
```

## Redis session repository

Refresh tokens are session IDs; user session sets enable logout-all.

**Category:** caching | **Duration:** 4 min read | **Tags:** redis, upstash

### RedisAuthSessionRepositoryImpl.java (excerpt)

**Path:** `src/main/java/at/backend/MarketingCompany/account/auth/adapters/outbound/persistence/RedisAuthSessionRepositoryImpl.java`

Session keys prefixed with session: and indexed per user in Redis sets.

```java
private static final String SESSION_KEY_PREFIX = "session:";
private static final String USER_SESSIONS_PREFIX = "user_sessions:";

public void save(AuthSession session) {
  String refreshToken = session.getSessionId().value();
  redisTemplate.opsForValue().set(getSessionKey(refreshToken), entity);
  redisTemplate.opsForSet().add(getUserSessionsKey(userId), refreshToken);
}
```

## Additional notes

# Code Showcase

> **Useful note:** Examples are abbreviated for readability. Open the cited paths in the repo
> for full implementations including error handling and mappers.

> **Warning:** Never log access or refresh tokens. Existing code truncates refresh tokens in logs.

