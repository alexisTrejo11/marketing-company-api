package at.backend.MarketingCompany.marketing.target.core.application.command;

import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.TargetStatus;

public record ChangeTargetStatusCommand(
		CampaignTargetId targetId,
		TargetStatus status,
		String reason
) {
}
