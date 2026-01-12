package at.backend.MarketingCompany.marketing.target.core.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.ports.input.TargetQueryInputPort;

@Service
public class TargetQueryService implements TargetQueryInputPort {

  @Override
  public CampaignTarget getTargetById(CampaignTargetId targetId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Page<CampaignTarget> getTargetsByCampaign(MarketingCampaignId campaignId, Pageable pageRequest) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Page<CampaignTarget> getAchievedTargets(MarketingCampaignId campaignId, Pageable pageRequest) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Page<CampaignTarget> getUnachievedTargets(MarketingCampaignId campaignId, Pageable pageRequest) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Page<CampaignTarget> getUnachievedTargetsByMetricType(MetricType metricType, Pageable pageRequest) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public CampaignTarget getTargetByMetricName(MarketingCampaignId campaignId, String metricName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public CampaignTarget getCampaignTargetByMetricName(MarketingCampaignId campaignId, String metricName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public TargetStatistics getTargetStatistics(MarketingCampaignId campaignId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public BigDecimal getAchievementRate(MarketingCampaignId campaignId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public BigDecimal getAverageAchievementPercentage(MarketingCampaignId campaignId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int countAchievedTargets(MarketingCampaignId campaignId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int countUnachievedTargets(MarketingCampaignId campaignId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isMetricNameAvailable(MarketingCampaignId campaignId, String metricName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean existsByCampaignId(MarketingCampaignId campaignId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean existsByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<CampaignTarget> getTargetsByIds(List<CampaignTargetId> targetIds) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<CampaignTarget> getCampaignTargetsByIds(List<CampaignTargetId> targetIds) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
