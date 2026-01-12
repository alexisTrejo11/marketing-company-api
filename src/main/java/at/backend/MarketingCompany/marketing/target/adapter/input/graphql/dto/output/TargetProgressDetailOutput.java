package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output;

import java.math.BigDecimal;

public record TargetProgressDetailOutput(
    String targetId,
    String metricName,
    String metricType,
    BigDecimal targetValue,
    BigDecimal currentValue,
    BigDecimal achievementPercentage,
    boolean isAchieved,
    String measurementUnit) {
}
