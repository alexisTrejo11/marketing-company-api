package at.backend.MarketingCompany.marketing.target.core.application.command;

import java.math.BigDecimal;

import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public record TargetUpdateCommand(
    CampaignTargetId targetId,
    String metricName,
    BigDecimal targetValue,
    String measurementUnit,
    BigDecimal currentValue,
    String reason) {
  public boolean hasProgressUpdate() {
    return currentValue != null && reason != null && !reason.isBlank();
  }

  public boolean hasDetailsUpdate() {
    return metricName != null || targetValue != null || measurementUnit != null;
  }
}
