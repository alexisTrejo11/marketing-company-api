package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.target.core.application.command.ChangeTargetStatusCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.TargetStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TargetStatusChangeInput(
		@NotNull @Positive Long targetId,
		@NotNull TargetStatus status,
		String reason
) {
	public ChangeTargetStatusCommand toCommand() {
		return new ChangeTargetStatusCommand(new CampaignTargetId(targetId), status, reason);
	}
}
