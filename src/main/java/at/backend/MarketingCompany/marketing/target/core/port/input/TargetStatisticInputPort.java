package at.backend.MarketingCompany.marketing.target.core.port.input;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.core.application.dto.TargetStatistics;

import java.math.BigDecimal;

public interface TargetStatisticInputPort {

	TargetStatistics getTargetStatistics(MarketingCampaignId campaignId);

	Double getTargetAchievementRate(MarketingCampaignId campaignId);

	BigDecimal getAverageAchievementPercentage(MarketingCampaignId campaignId);

	int countAchievedTargets(MarketingCampaignId campaignId);
}
