package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.target.core.application.command.UpdateTargetProgressCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateTargetProgressInput(
    @NotNull @Positive Long targetId,
    @NotNull BigDecimal currentValue,
    @NotBlank String reason) {
  public UpdateTargetProgressCommand toCommand() {
    return new UpdateTargetProgressCommand(
        new CampaignTargetId(targetId),
        currentValue,
        reason);
  }
}
