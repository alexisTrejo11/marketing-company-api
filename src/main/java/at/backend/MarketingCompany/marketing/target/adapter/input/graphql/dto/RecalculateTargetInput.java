package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.target.core.application.command.RecalculateTargetCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecalculateTargetInput(
		@NotNull @Positive Long targetId
) {
	public RecalculateTargetCommand toCommand() {
		return new RecalculateTargetCommand(new CampaignTargetId(targetId));
	}
}
