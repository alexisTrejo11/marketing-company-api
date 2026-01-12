package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import lombok.Builder;

@Builder
public record CampaignTargetReconstructParams(
    CampaignTargetId id,
    MarketingCampaignId campaignId,
    String metricName,
    MetricType metricType,
    BigDecimal targetValue,
    BigDecimal currentValue,
    String measurementUnit,
    CampaignTargetStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version) {
}
