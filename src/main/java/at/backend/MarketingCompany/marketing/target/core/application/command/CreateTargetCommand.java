package at.backend.MarketingCompany.marketing.target.core.application.command;

import java.math.BigDecimal;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CreateTargetParams;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;

public record CreateTargetCommand(
    MarketingCampaignId campaignId,
    String metricName,
    MetricType metricType,
    BigDecimal targetValue,
    String measurementUnit) {
  public CreateTargetParams toParams() {
    return new CreateTargetParams(campaignId, metricName, metricType, targetValue, measurementUnit);
  }
}
