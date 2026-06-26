package at.backend.MarketingCompany.marketing.target.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

import java.util.Map;

public class CampaignTargetException extends DomainException {
	public CampaignTargetException(String message) {
		super(message, "CAMPAIGN_TARGET_ERROR");
	}

	public CampaignTargetException(String message, String errorCode) {
		super(message, errorCode);
	}

	public CampaignTargetException(String message, String errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}

	public CampaignTargetException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}
