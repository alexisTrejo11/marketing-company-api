package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.target.core.application.command.DeleteTargetCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeleteTargetInput(
		@NotNull @Positive Long targetId,
		@NotNull Boolean hardDelete
) {
	public DeleteTargetCommand toCommand() {
		return new DeleteTargetCommand(new CampaignTargetId(targetId), Boolean.TRUE.equals(hardDelete));
	}
}
