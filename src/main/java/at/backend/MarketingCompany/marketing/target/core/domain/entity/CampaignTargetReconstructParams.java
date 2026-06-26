package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.TargetStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CampaignTargetReconstructParams(
		CampaignTargetId id,
		MarketingCampaignId campaignId,
		String metricName,
		MetricType metricType,
		BigDecimal targetValue,
		BigDecimal currentValue,
		String measurementUnit,
		TargetStatus status,
		String statusChangeReason,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		LocalDateTime deletedAt,
		Integer version
) {
}
