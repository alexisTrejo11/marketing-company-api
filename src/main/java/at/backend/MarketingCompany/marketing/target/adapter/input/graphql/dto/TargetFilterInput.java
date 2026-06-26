package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.application.query.TargetQuery;

import java.math.BigDecimal;
import java.util.List;

public record TargetFilterInput(
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
	public TargetQuery toQuery() {
		if (campaignId == null
				&& (metricTypes == null || metricTypes.isEmpty())
				&& isAchieved == null
				&& minTargetValue == null
				&& maxTargetValue == null
				&& minCurrentValue == null
				&& maxCurrentValue == null
				&& minAchievementPercentage == null
				&& maxAchievementPercentage == null
				&& (searchTerm == null || searchTerm.isBlank())) {
			return TargetQuery.empty();
		}

		return new TargetQuery(
				campaignId,
				metricTypes,
				isAchieved,
				minTargetValue,
				maxTargetValue,
				minCurrentValue,
				maxCurrentValue,
				minAchievementPercentage,
				maxAchievementPercentage,
				searchTerm
		);
	}
}
