package at.backend.MarketingCompany.config.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Adds a global key prefix on write and removes it on read so callers keep using logical keys.
 */
public class PrefixedStringRedisSerializer implements RedisSerializer<String> {

	private final String prefix;
	private final StringRedisSerializer delegate = new StringRedisSerializer();

	public PrefixedStringRedisSerializer(String prefix) {
		this.prefix = normalize(prefix);
	}

	@Override
	public byte[] serialize(String key) {
		if (key == null) {
			return null;
		}
		if (prefix.isEmpty()) {
			return delegate.serialize(key);
		}
		return delegate.serialize(prefix + key);
	}

	@Override
	public String deserialize(byte[] bytes) {
		String key = delegate.deserialize(bytes);
		if (key == null || prefix.isEmpty()) {
			return key;
		}
		if (key.startsWith(prefix)) {
			return key.substring(prefix.length());
		}
		return key;
	}

	private static String normalize(String prefix) {
		if (prefix == null || prefix.isBlank()) {
			return "";
		}
		return prefix.endsWith(":") ? prefix : prefix + ":";
	}
}
