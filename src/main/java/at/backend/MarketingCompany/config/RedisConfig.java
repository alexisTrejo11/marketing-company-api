package at.backend.MarketingCompany.config;

import at.backend.MarketingCompany.account.auth.adapters.outbound.persistence.AuthSessionEntity;
import at.backend.MarketingCompany.config.redis.PrefixedStringRedisSerializer;
import at.backend.MarketingCompany.config.redis.RedisKeyPrefixProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@Primary
public class RedisConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(
				com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
		);
		return mapper;
	}

	@Bean
	public RedisSerializer<String> redisKeySerializer(RedisKeyPrefixProperties redisKeyPrefixProperties) {
		return new PrefixedStringRedisSerializer(redisKeyPrefixProperties.normalizedPrefix());
	}

	@Bean
	@Primary
	public RedisTemplate<String, Object> redisTemplate(
			RedisConnectionFactory connectionFactory,
			ObjectMapper objectMapper,
			RedisSerializer<String> redisKeySerializer
	) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(redisKeySerializer);
		template.setHashKeySerializer(redisKeySerializer);

		Jackson2JsonRedisSerializer<Object> jsonSerializer =
				new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
		template.setValueSerializer(jsonSerializer);
		template.setHashValueSerializer(jsonSerializer);

		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public RedisTemplate<String, AuthSessionEntity> authSessionRedisTemplate(
			RedisConnectionFactory connectionFactory,
			ObjectMapper objectMapper,
			RedisSerializer<String> redisKeySerializer
	) {
		RedisTemplate<String, AuthSessionEntity> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(redisKeySerializer);
		template.setHashKeySerializer(redisKeySerializer);

		Jackson2JsonRedisSerializer<AuthSessionEntity> serializer =
				new Jackson2JsonRedisSerializer<>(objectMapper, AuthSessionEntity.class);
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);

		template.afterPropertiesSet();
		return template;
	}
}
