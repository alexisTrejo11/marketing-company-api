package at.backend.MarketingCompany.marketing.target.core.ports.input;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.core.application.service.TargetStatistics;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public interface TargetQueryInputPort {
  CampaignTarget getTargetById(CampaignTargetId targetId);

  // Campaign-specific queries
  Page<CampaignTarget> getTargetsByCampaign(MarketingCampaignId campaignId, Pageable pageRequest);

  Page<CampaignTarget> getAchievedTargets(MarketingCampaignId campaignId, Pageable pageRequest);

  Page<CampaignTarget> getUnachievedTargets(MarketingCampaignId campaignId, Pageable pageRequest);

  Page<CampaignTarget> getUnachievedTargetsByMetricType(MetricType metricType, Pageable pageRequest);

  CampaignTarget getTargetByMetricName(MarketingCampaignId campaignId, String metricName);

  CampaignTarget getCampaignTargetByMetricName(MarketingCampaignId campaignId, String metricName);

  // Statistics and analytics
  TargetStatistics getTargetStatistics(MarketingCampaignId campaignId);

  BigDecimal getAchievementRate(MarketingCampaignId campaignId);

  BigDecimal getAverageAchievementPercentage(MarketingCampaignId campaignId);

  int countAchievedTargets(MarketingCampaignId campaignId);

  int countUnachievedTargets(MarketingCampaignId campaignId);

  // Validation queries
  boolean isMetricNameAvailable(MarketingCampaignId campaignId, String metricName);

  boolean existsByCampaignId(MarketingCampaignId campaignId);

  boolean existsByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName);

  // Batch operations
  List<CampaignTarget> getTargetsByIds(List<CampaignTargetId> targetIds);

  List<CampaignTarget> getCampaignTargetsByIds(List<CampaignTargetId> targetIds);
}
