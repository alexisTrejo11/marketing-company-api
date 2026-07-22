package at.backend.MarketingCompany.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

/**
 * Replaces Redis with a no-network stub for integration tests.
 * Uses Mockito from spring-boot-starter-test only — no Docker or extra libraries.
 */
@TestConfiguration
public class InMemoryRedisTestConfig {

	@Bean
	@Primary
	RedisConnectionFactory redisConnectionFactory() {
		return mock(RedisConnectionFactory.class, RETURNS_DEEP_STUBS);
	}

}
