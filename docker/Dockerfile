# Build stage
FROM gradle:8.11-jdk23 AS build
WORKDIR /app

COPY gradlew .
COPY gradle/wrapper gradle/wrapper
COPY build.gradle .
COPY settings.gradle .

RUN gradle dependencies --no-daemon

COPY src src

RUN gradle bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /app/build/libs/*.jar app.jar

RUN addgroup -g 1000 spring && adduser -u 1000 -G spring -s /bin/sh -D spring

RUN echo '#!/bin/sh' > /docker-entrypoint.sh && \
    echo 'set -e' >> /docker-entrypoint.sh && \
    echo 'mkdir -p /app/logs/audit' >> /docker-entrypoint.sh && \
    echo 'chown -R spring:spring /app/logs' >> /docker-entrypoint.sh && \
    echo 'chmod -R 755 /app/logs' >> /docker-entrypoint.sh && \
    echo 'exec su spring -c "java -jar /app/app.jar"' >> /docker-entrypoint.sh && \
    chmod +x /docker-entrypoint.sh

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

ENTRYPOINT ["/docker-entrypoint.sh"]
