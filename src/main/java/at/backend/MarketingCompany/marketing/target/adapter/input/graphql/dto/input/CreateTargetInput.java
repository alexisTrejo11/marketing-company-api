package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input;

import java.math.BigDecimal;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.core.application.command.CreateTargetCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateTargetInput(
    @NotNull @Positive Long campaignId,
    @NotBlank String metricName,
    @NotNull MetricType metricType,
    @NotNull BigDecimal targetValue,
    String measurementUnit) {
  public CreateTargetCommand toCommand() {
    return new CreateTargetCommand(
        new MarketingCampaignId(campaignId),
        metricName,
        metricType,
        targetValue,
        measurementUnit);
  }
}
