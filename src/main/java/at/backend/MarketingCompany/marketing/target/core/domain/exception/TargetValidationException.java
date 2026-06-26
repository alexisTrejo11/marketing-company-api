package at.backend.MarketingCompany.marketing.target.core.domain.exception;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;

public class TargetValidationException extends CampaignTargetException {
	public TargetValidationException(String message) {
		super(message, "TARGET_VALIDATION_ERROR");
	}

	public TargetValidationException(MarketingCampaignId id) {
		super("Invalid Campaign ID provided: " + id.getValue(), "TARGET_VALIDATION_ERROR");
	}
}
