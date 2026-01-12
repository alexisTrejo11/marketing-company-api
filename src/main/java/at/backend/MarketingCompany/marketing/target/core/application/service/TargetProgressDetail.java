package at.backend.MarketingCompany.marketing.target.core.application.service;

import java.math.BigDecimal;

public record TargetProgressDetail(
    String targetId,
    String metricName,
    String metricType,
    BigDecimal targetValue,
    BigDecimal currentValue,
    BigDecimal achievementPercentage,
    boolean isAchieved,
    String measurementUnit) {
}
