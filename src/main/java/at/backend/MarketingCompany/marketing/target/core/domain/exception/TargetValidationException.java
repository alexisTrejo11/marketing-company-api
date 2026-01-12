package at.backend.MarketingCompany.marketing.target.core.domain.exception;

public class TargetValidationException extends CamapaignTargetException {
  public TargetValidationException(String message) {
    super(message, "CAMPAIGN_TARGET_VALIDATION_ERROR");
  }

}
