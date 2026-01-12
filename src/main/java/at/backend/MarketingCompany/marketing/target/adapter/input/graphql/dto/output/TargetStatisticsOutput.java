package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output;

import java.math.BigDecimal;
import java.util.List;

public record TargetStatisticsOutput(
    String campaignId,
    String campaignName,
    int totalTargets,
    int achievedTargets,
    int unachievedTargets,
    BigDecimal achievementRate,
    BigDecimal averageAchievementPercentage,
    TargetMetricBreakdownOutput metricBreakdown,
    List<TargetProgressDetailOutput> targetProgress) {
}
