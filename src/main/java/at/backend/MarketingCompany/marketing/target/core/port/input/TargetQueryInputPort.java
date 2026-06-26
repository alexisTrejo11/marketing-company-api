package at.backend.MarketingCompany.marketing.target.core.port.input;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.application.query.TargetQuery;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TargetQueryInputPort {

	CampaignTarget getTargetById(CampaignTargetId id);

	Page<CampaignTarget> searchTargets(TargetQuery query, Pageable pageable);

	Page<CampaignTarget> getTargetsByCampaign(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignTarget> getAchievedTargets(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignTarget> getUnachievedTargets(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignTarget> getUnachievedTargetsByMetricType(MetricType metricType, Pageable pageable);

	CampaignTarget getTargetByMetricName(MarketingCampaignId campaignId, String metricName);

	boolean isTargetNameAvailable(MarketingCampaignId campaignId, String metricName);
}
