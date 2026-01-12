package at.backend.MarketingCompany.marketing.target.core.application.command;

import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public record DeleteTargetCommand(
    CampaignTargetId targetId,
    boolean hardDelete) {

}
