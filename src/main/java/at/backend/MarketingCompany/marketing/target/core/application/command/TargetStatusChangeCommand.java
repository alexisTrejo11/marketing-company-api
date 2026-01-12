package at.backend.MarketingCompany.marketing.target.core.application.command;

import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTargetStatus;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public record TargetStatusChangeCommand(
    CampaignTargetId targetId,
    CampaignTargetStatus status,
    String reason) {
}
