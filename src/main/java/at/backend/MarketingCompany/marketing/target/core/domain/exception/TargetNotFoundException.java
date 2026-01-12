package at.backend.MarketingCompany.marketing.target.core.domain.exception;

import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class TargetNotFoundException extends NotFoundException {
  public TargetNotFoundException(CampaignTargetId targetId) {
    super("Campaign Target", targetId.asString());
  }

}
