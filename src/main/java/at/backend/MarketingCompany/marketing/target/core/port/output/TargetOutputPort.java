package at.backend.MarketingCompany.marketing.target.core.port.output;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.application.query.TargetQuery;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TargetOutputPort {

	CampaignTarget save(CampaignTarget target);

	Optional<CampaignTarget> findById(CampaignTargetId id);

	void hardDelete(CampaignTargetId id);

	Page<CampaignTarget> findAll(Pageable pageable);

	Page<CampaignTarget> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

	Page<CampaignTarget> findByCampaignIdAndAchieved(MarketingCampaignId campaignId, boolean achieved, Pageable pageable);

	Page<CampaignTarget> findByMetricTypeAndNotAchieved(MetricType metricType, Pageable pageable);

	Optional<CampaignTarget> findByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName);

	Page<CampaignTarget> findByFilters(TargetQuery query, Pageable pageable);

	List<CampaignTarget> findAllByCampaignId(MarketingCampaignId campaignId);

	long countByCampaignId(MarketingCampaignId campaignId);

	long countAchievedByCampaignId(MarketingCampaignId campaignId);

	boolean existsByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName);

	BigDecimal calculateAverageAchievementByCampaignId(MarketingCampaignId campaignId);
}
