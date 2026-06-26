package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import java.math.BigDecimal;

public record TargetProgressOutput(
		BigDecimal targetValue,
		BigDecimal currentValue,
		BigDecimal achievementPercentage,
		Boolean isAchieved,
		BigDecimal remainingValue
) {
}
