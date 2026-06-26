package at.backend.MarketingCompany.marketing.target.core.application.command;

import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public record RecalculateTargetCommand(CampaignTargetId targetId) {
}
