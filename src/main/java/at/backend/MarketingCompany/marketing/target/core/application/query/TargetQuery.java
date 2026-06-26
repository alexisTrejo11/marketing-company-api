package at.backend.MarketingCompany.marketing.target.core.application.query;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;

import java.math.BigDecimal;
import java.util.List;

public record TargetQuery(
		Long campaignId,
		List<MetricType> metricTypes,
		Boolean isAchieved,
		BigDecimal minTargetValue,
		BigDecimal maxTargetValue,
		BigDecimal minCurrentValue,
		BigDecimal maxCurrentValue,
		BigDecimal minAchievementPercentage,
		BigDecimal maxAchievementPercentage,
		String searchTerm
) {
	public static TargetQuery empty() {
		return new TargetQuery(null, null, null, null, null, null, null, null, null, null);
	}

	public boolean isEmpty() {
		return campaignId == null
				&& (metricTypes == null || metricTypes.isEmpty())
				&& isAchieved == null
				&& minTargetValue == null
				&& maxTargetValue == null
				&& minCurrentValue == null
				&& maxCurrentValue == null
				&& minAchievementPercentage == null
				&& maxAchievementPercentage == null
				&& (searchTerm == null || searchTerm.isBlank());
	}
}
