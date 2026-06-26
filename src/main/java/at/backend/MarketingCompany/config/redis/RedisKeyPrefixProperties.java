package at.backend.MarketingCompany.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.redis")
public class RedisKeyPrefixProperties {

	/**
	 * Global namespace for all Redis keys in this application.
	 * Use a unique value per project when sharing Upstash or a local Redis instance.
	 */
	private String keyPrefix = "marketing-company-api:";

	public String normalizedPrefix() {
		if (keyPrefix == null || keyPrefix.isBlank()) {
			return "";
		}
		return keyPrefix.endsWith(":") ? keyPrefix : keyPrefix + ":";
	}
}
