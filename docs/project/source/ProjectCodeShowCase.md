---
codeExamples:
  - id: hexagonal-auth
    title: Auth command handler (application core)
    description: >-
      Login flow orchestrates user lookup, password validation, token generation,
      and Redis session persistence — domain stays free of GraphQL/JPA details.
    category: authentication
    duration: 5 min read
    tags:
      - hexagonal
      - jwt
      - redis
    files:
      - name: AuthCommandHandler.java
        path: src/main/java/at/backend/MarketingCompany/account/auth/core/application/AuthCommandHandler.java
        language: java
        explanation: Core use case — validates credentials and creates session via ports.
        content: |
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

  - id: graphql-auth-controller
    title: GraphQL auth mutations
    description: Input adapter exposing signUp, login, refresh, and logout with sensitive rate limits.
    category: api
    duration: 3 min read
    tags:
      - graphql
      - spring
    files:
      - name: AuthController.java
        path: src/main/java/at/backend/MarketingCompany/account/auth/adapters/inbound/controller/AuthController.java
        language: java
        explanation: Maps GraphQL mutations to commands; captures client IP and user agent.
        content: |
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

  - id: docker-host-overrides
    title: Docker Compose host overrides
    description: >-
      Full local stack forces postgres/redis service names so the app container
      never connects to localhost for data stores.
    category: integration
    duration: 2 min read
    tags:
      - docker
      - devops
    files:
      - name: compose.full.yml (excerpt)
        path: docker/compose.full.yml
        language: yaml
        explanation: Environment block overrides .env localhost values inside the app service.
        content: |
          app:
            environment:
              SPRING_PROFILES_ACTIVE: docker
              SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${POSTGRES_DB}
              SPRING_REDIS_HOST: redis
              DB_HOST: postgres

  - id: env-configuration
    title: Environment-driven secrets
    description: >-
      JWT, database, Redis, CORS, and Kafka settings come from .env — loaded by
      spring-dotenv locally and passed to EC2/Docker in production.
    category: security
    duration: 2 min read
    tags:
      - config
      - aws
    files:
      - name: application.yml (auth excerpt)
        path: src/main/resources/application.yml
        language: yaml
        explanation: Production profile requires JWT secrets without insecure defaults.
        content: |
          app:
            auth:
              jwt:
                access-secret: ${JWT_ACCESS_SECRET}
                refresh-secret: ${JWT_REFRESH_SECRET}
                access-expiration-minutes: ${JWT_ACCESS_EXPIRATION_MINUTES:15}
                refresh-expiration-days: ${JWT_REFRESH_EXPIRATION_DAYS:30}

  - id: redis-sessions
    title: Redis session repository
    description: Refresh tokens are session IDs; user session sets enable logout-all.
    category: caching
    duration: 4 min read
    tags:
      - redis
      - upstash
    files:
      - name: RedisAuthSessionRepositoryImpl.java (excerpt)
        path: src/main/java/at/backend/MarketingCompany/account/auth/adapters/outbound/persistence/RedisAuthSessionRepositoryImpl.java
        language: java
        explanation: "Session keys prefixed with session: and indexed per user in Redis sets."
        content: |
          private static final String SESSION_KEY_PREFIX = "session:";
          private static final String USER_SESSIONS_PREFIX = "user_sessions:";

          public void save(AuthSession session) {
            String refreshToken = session.getSessionId().value();
            redisTemplate.opsForValue().set(getSessionKey(refreshToken), entity);
            redisTemplate.opsForSet().add(getUserSessionsKey(userId), refreshToken);
          }
---

# Code Showcase

> **Useful note:** Examples are abbreviated for readability. Open the cited paths in the repo
> for full implementations including error handling and mappers.

> **Warning:** Never log access or refresh tokens. Existing code truncates refresh tokens in logs.
