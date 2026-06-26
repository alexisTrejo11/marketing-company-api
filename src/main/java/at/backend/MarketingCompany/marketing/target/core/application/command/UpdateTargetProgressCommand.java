package at.backend.MarketingCompany.marketing.target.core.application.command;

import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

import java.math.BigDecimal;

public record UpdateTargetProgressCommand(
		CampaignTargetId targetId,
		BigDecimal currentValue
) {
}
