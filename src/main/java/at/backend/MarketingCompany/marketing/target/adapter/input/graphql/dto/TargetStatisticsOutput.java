package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import java.math.BigDecimal;
import java.util.List;

public record TargetStatisticsOutput(
		Long campaignId,
		String campaignName,
		Long totalTargets,
		Long achievedTargets,
		Long unachievedTargets,
		Double achievementRate,
		BigDecimal averageAchievementPercentage,
		TargetMetricBreakdownOutput metricBreakdown,
		List<TargetProgressDetailOutput> targetProgress
) {
}
