package at.backend.MarketingCompany.marketing.target.core.domain.exception;

public class TargetStateException extends CamapaignTargetException {
  public TargetStateException(String message) {
    super(message, "CAMPAIGN_TARGET_STATE_ERROR");
  }

}
