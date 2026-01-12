package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record TargetProgressOutput(
    BigDecimal targetValue,
    BigDecimal currentValue,
    BigDecimal achievementPercentage,
    boolean isAchieved,
    BigDecimal remainingValue) {
}
