package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.target.core.application.command.TargetStatusChangeCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTargetStatus;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TargetStatusChangeInput(
    @NotNull @Positive Long targetId,
    @NotNull CampaignTargetStatus status,
    @NotBlank String reason) {

  public TargetStatusChangeCommand toCommand() {
    return new TargetStatusChangeCommand(
        new CampaignTargetId(targetId),
        status,
        reason);
  }
}
