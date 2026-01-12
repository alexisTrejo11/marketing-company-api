package at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.mapper;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.CampaignTarget;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.params.MarketingCampaignReconstructParams;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.*;
import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model.MarketingChannelEntity;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.target.adapter.output.persistence.model.CampaignTargetEntity;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CampaignEntityMapper {

  public MarketingCampaignEntity toEntity(MarketingCampaign domain) {
    if (domain == null) {
      return null;
    }

    MarketingCampaignEntity entity = new MarketingCampaignEntity();
    entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
    entity.setName(domain.getName() != null ? domain.getName().value() : null);
    entity.setDescription(domain.getDescription());
    entity.setCampaignType(domain.getCampaignType());
    entity.setStatus(domain.getStatus());

    if (domain.getBudget() != null) {
      entity.setTotalBudget(domain.getBudget().totalBudget());
      entity.setSpentAmount(domain.getBudget().spentAmount());
    }

    if (domain.getPeriod() != null) {
      entity.setStartDate(domain.getPeriod().startDate());
      entity.setEndDate(domain.getPeriod().endDate());
    }

    entity.setBudgetAllocations(domain.getBudgetAllocations());
    entity.setTargetAudienceDemographics(domain.getTargetAudienceDemographics());
    entity.setTargetLocations(domain.getTargetLocations());
    entity.setTargetInterests(domain.getTargetInterests());
    entity.setPrimaryGoal(domain.getPrimaryGoal());
    entity.setSecondaryGoals(domain.getSecondaryGoals());
    entity.setSuccessMetrics(domain.getSuccessMetrics());

    if (domain.getPrimaryChannelId() != null) {
      MarketingChannelEntity channelRef = new MarketingChannelEntity();
      channelRef.setId(domain.getPrimaryChannelId().getValue());
      entity.setPrimaryChannel(channelRef);
    }

    entity.setCreatedAt(domain.getCreatedAt());
    entity.setUpdatedAt(domain.getUpdatedAt());
    entity.setDeletedAt(domain.getDeletedAt());
    entity.setVersion(domain.getVersion());

    return entity;
  }

  public MarketingCampaign toDomain(MarketingCampaignEntity entity) {
    if (entity == null) {
      return null;
    }

    CampaignBudget budget = null;
    if (entity.getTotalBudget() != null) {
      budget = new CampaignBudget(
          entity.getTotalBudget(),
          entity.getSpentAmount());
    }

    CampaignPeriod period = null;
    if (entity.getStartDate() != null) {
      period = new CampaignPeriod(
          entity.getStartDate(),
          entity.getEndDate());
    }

    CampaignName name = null;
    if (entity.getName() != null) {
      name = new CampaignName(entity.getName());
    }

    MarketingChannelId channelId = null;
    if (entity.getPrimaryChannel() != null && entity.getPrimaryChannel().getId() != null) {
      channelId = new MarketingChannelId(entity.getPrimaryChannel().getId());
    }

    Set<CampaignTarget> targets = new HashSet<>();
    if (entity.getTargets() != null) {
      targets = entity.getTargets().stream()
          .map(this::toCampaignTargetDomain)
          .collect(Collectors.toSet());
    }

    return MarketingCampaign.reconstruct(new MarketingCampaignReconstructParams(
        entity.getId() != null ? new MarketingCampaignId(entity.getId()) : null,
        name,
        entity.getDescription(),
        entity.getCampaignType(),
        entity.getStatus(),
        budget,
        period,
        entity.getBudgetAllocations(),
        entity.getTargetAudienceDemographics(),
        entity.getTargetLocations(),
        entity.getTargetInterests(),
        entity.getPrimaryGoal(),
        entity.getSecondaryGoals(),
        entity.getSuccessMetrics(),
        channelId,
        targets,
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getDeletedAt(),
        entity.getVersion()));
  }

  private CampaignTarget toCampaignTargetDomain(CampaignTargetEntity entity) {
    if (entity == null) {
      return null;
    }

    return CampaignTarget.builder()
        .metricName(entity.getMetricName())
        .metricType(entity.getMetricType())
        .targetValue(entity.getTargetValue())
        .currentValue(entity.getCurrentValue())
        .measurementUnit(entity.getMeasurementUnit())
        .build();
  }
}
