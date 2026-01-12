package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input;

import at.backend.MarketingCompany.marketing.target.core.application.command.TargetStatusChangeCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTargetStatus;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

public record ResetTargetProgressInput(
    @NotNull @Positive Long targetId,
    @NotBlank String reason) {

  public TargetStatusChangeCommand toCommand() {
    return new TargetStatusChangeCommand(
        new CampaignTargetId(targetId),
        CampaignTargetStatus.PENDING,
        reason);
  }
}
