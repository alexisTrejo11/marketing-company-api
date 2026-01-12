package at.backend.MarketingCompany.marketing.target.core.application.command;

import java.math.BigDecimal;

import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public record UpdateTargetProgressCommand(
    CampaignTargetId targetId,
    BigDecimal currentValue,
    String reason) {
}
