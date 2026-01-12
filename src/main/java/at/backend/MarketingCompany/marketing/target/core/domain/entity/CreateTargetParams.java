package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import java.math.BigDecimal;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

public record CreateTargetParams(
    MarketingCampaignId campaignId,
    String metricName,
    MetricType metricType,
    BigDecimal targetValue,
    String measurementUnit) {
}
