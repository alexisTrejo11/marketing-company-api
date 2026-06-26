package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateTargetParams(
		MarketingCampaignId campaignId,
		String metricName,
		MetricType metricType,
		BigDecimal targetValue,
		String measurementUnit
) {
}
