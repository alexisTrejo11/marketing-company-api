package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import java.math.BigDecimal;

public record TargetProgressDetailOutput(
		Long targetId,
		String metricName,
		String metricType,
		BigDecimal targetValue,
		BigDecimal currentValue,
		BigDecimal achievementPercentage,
		Boolean isAchieved,
		String measurementUnit
) {
}
