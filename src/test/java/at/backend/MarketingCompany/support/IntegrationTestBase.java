package at.backend.MarketingCompany.support;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Import(InMemoryRedisTestConfig.class)
@ActiveProfiles("test")
public abstract class IntegrationTestBase {

	@DynamicPropertySource
	static void registerIntegrationProperties(DynamicPropertyRegistry registry) {
		registry.add("app.auth.jwt.refresh-expiration-days", () -> "30");
		registry.add("app.auth.max-sessions-per-user", () -> "5");
	}

}
