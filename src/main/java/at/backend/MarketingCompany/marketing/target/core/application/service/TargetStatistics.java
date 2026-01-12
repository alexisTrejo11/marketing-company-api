package at.backend.MarketingCompany.marketing.target.core.application.service;

import java.math.BigDecimal;
import java.util.List;

public record TargetStatistics(
    String campaignId,
    String campaignName,
    int totalTargets,
    int achievedTargets,
    int unachievedTargets,
    BigDecimal achievementRate,
    BigDecimal averageAchievementPercentage,
    TargetMetricBreakdown metricBreakdown,
    List<TargetProgressDetail> targetProgress) {

}
