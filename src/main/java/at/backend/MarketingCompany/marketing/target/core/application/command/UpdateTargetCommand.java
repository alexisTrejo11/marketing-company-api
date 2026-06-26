package at.backend.MarketingCompany.marketing.target.core.application.command;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

import java.math.BigDecimal;

public record UpdateTargetCommand(
		CampaignTargetId id,
		String metricName,
		MetricType metricType,
		BigDecimal targetValue,
		String measurementUnit
) {
}
