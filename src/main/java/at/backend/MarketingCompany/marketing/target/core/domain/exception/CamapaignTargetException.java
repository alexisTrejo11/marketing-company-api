package at.backend.MarketingCompany.marketing.target.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

public class CamapaignTargetException extends DomainException {
  public CamapaignTargetException(String message) {
    super(message, "CAMPAIGN_TARGET_ERROR");
  }

  public CamapaignTargetException(String message, String code) {
    super(message, code);
  }

}
