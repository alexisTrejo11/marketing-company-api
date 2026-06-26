package at.backend.MarketingCompany.marketing.target.core.application.command;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CreateTargetParams;

import java.math.BigDecimal;

public record CreateTargetCommand(
		MarketingCampaignId campaignId,
		String metricName,
		MetricType metricType,
		BigDecimal targetValue,
		String measurementUnit
) {
	public CreateTargetParams toCreateParams() {
		return CreateTargetParams.builder()
				.campaignId(campaignId)
				.metricName(metricName)
				.metricType(metricType)
				.targetValue(targetValue)
				.measurementUnit(measurementUnit)
				.build();
	}
}
