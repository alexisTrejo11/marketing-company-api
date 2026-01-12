package at.backend.MarketingCompany.marketing.target.core.domain.valueobject;

import at.backend.MarketingCompany.shared.domain.NumericId;

public class CampaignTargetId extends NumericId {
  public CampaignTargetId(Long value) {
    super(value);
  }

  public static CampaignTargetId generate() {
    return new CampaignTargetId(0L);
  }
}
