package at.backend.MarketingCompany.marketing.target.core.ports.output;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public interface TargetRepositoryPort {

  CampaignTarget save(CampaignTarget target);

  Optional<CampaignTarget> findById(CampaignTargetId targetId);

  void delete(CampaignTargetId targetId);

  void hardDelete(CampaignTargetId targetId);

  Page<CampaignTarget> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable);

  Page<CampaignTarget> findByCampaignIdAndIsAchieved(MarketingCampaignId campaignId, boolean isAchieved,
      Pageable pageable);

  Page<CampaignTarget> findByMetricTypeAndNotAchieved(MetricType metricType, Pageable pageable);

  Optional<CampaignTarget> findByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName);

  boolean existsByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName);

  boolean existsByCampaignId(MarketingCampaignId campaignId);

  long countAchievedByCampaignId(MarketingCampaignId campaignId);

  Optional<BigDecimal> getAverageAchievementPercentage(MarketingCampaignId campaignId);

  BigDecimal getAchievementRate(MarketingCampaignId campaignId);

  List<CampaignTarget> findAllByIds(List<CampaignTargetId> targetIds);
}
