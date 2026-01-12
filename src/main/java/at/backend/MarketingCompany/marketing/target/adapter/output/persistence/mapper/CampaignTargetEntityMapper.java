package at.backend.MarketingCompany.marketing.target.adapter.output.persistence.mapper;

import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.adapter.output.persistence.model.CampaignTargetEntity;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTargetReconstructParams;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

@Component
public class CampaignTargetEntityMapper {

  public CampaignTarget toDomain(CampaignTargetEntity entity) {
    if (entity == null) {
      return null;
    }

    var params = new CampaignTargetReconstructParams(
        entity.getId() != null ? new CampaignTargetId(entity.getId()) : null,
        entity.getCampaign() != null ? new MarketingCampaignId(entity.getCampaign().getId()) : null,
        entity.getMetricName(),
        entity.getMetricType(),
        entity.getTargetValue(),
        entity.getCurrentValue(),
        entity.getMeasurementUnit(),
        entity.getStatus(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getDeletedAt(),
        entity.getVersion());

    return CampaignTarget.reconstruct(params);
  }

  public CampaignTargetEntity toEntity(CampaignTarget domain) {
    if (domain == null) {
      return null;
    }

    CampaignTargetEntity entity = new CampaignTargetEntity();

    if (domain.getCampaignId() != null) {
      MarketingCampaignEntity campaignEntity = new MarketingCampaignEntity();
      campaignEntity.setId(domain.getCampaignId().getValue());
      entity.setCampaign(campaignEntity);
    }

    entity.setId(domain.getId() != null ? domain.getId().getValue() : null);
    entity.setMetricName(domain.getMetricName());
    entity.setMetricType(domain.getMetricType());
    entity.setTargetValue(domain.getTargetValue());
    entity.setCurrentValue(domain.getCurrentValue());
    entity.setMeasurementUnit(domain.getMeasurementUnit());
    entity.setStatus(domain.getStatus());

    entity.setCreatedAt(domain.getCreatedAt());
    entity.setUpdatedAt(domain.getUpdatedAt());
    entity.setDeletedAt(domain.getDeletedAt());
    entity.setVersion(domain.getVersion());

    return entity;
  }
}
