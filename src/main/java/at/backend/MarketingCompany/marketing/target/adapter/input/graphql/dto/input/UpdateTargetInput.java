package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import at.backend.MarketingCompany.marketing.target.core.application.command.TargetUpdateCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public record UpdateTargetInput(
    @NotNull @Positive Long id,
    String metricName,
    BigDecimal targetValue,
    String measurementUnit,
    BigDecimal currentValue,
    String reason) {
  public TargetUpdateCommand toCommand() {
    return new TargetUpdateCommand(
        new CampaignTargetId(id),
        metricName,
        targetValue,
        measurementUnit,
        currentValue,
        reason);
  }
}
