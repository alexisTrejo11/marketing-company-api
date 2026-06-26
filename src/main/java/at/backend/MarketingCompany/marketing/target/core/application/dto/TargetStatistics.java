package at.backend.MarketingCompany.marketing.target.core.application.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record TargetStatistics(
		Long campaignId,
		String campaignName,
		Long totalTargets,
		Long achievedTargets,
		Long unachievedTargets,
		Double achievementRate,
		BigDecimal averageAchievementPercentage,
		TargetMetricBreakdown metricBreakdown,
		List<TargetProgressDetail> targetProgress
) {
	@Builder
	public record TargetMetricBreakdown(
			Long revenueTargets,
			Long leadsTargets,
			Long conversionsTargets,
			Long clicksTargets,
			Long impressionsTargets,
			Long engagementTargets,
			Long reachTargets,
			Long customTargets
	) {
	}

	@Builder
	public record TargetProgressDetail(
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
}
