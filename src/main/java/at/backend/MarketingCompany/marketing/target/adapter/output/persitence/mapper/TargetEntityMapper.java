package at.backend.MarketingCompany.marketing.target.adapter.output.persitence.mapper;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.adapter.output.persitence.model.CampaignTargetEntity;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTargetReconstructParams;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import org.springframework.stereotype.Component;

@Component
public class TargetEntityMapper {

	public CampaignTarget toDomain(CampaignTargetEntity entity) {
		if (entity == null) {
			return null;
		}

		var params = CampaignTargetReconstructParams.builder()
				.id(entity.getId() != null ? new CampaignTargetId(entity.getId()) : null)
				.campaignId(entity.getCampaign() != null ? new MarketingCampaignId(entity.getCampaign().getId()) : null)
				.metricName(entity.getMetricName())
				.metricType(entity.getMetricType())
				.targetValue(entity.getTargetValue())
				.currentValue(entity.getCurrentValue())
				.measurementUnit(entity.getMeasurementUnit())
				.status(entity.getStatus())
				.statusChangeReason(entity.getStatusChangeReason())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.deletedAt(entity.getDeletedAt())
				.version(entity.getVersion())
				.build();

		return CampaignTarget.reconstruct(params);
	}

	public CampaignTargetEntity toEntity(CampaignTarget target) {
		if (target == null) {
			return null;
		}

		CampaignTargetEntity entity = new CampaignTargetEntity();
		entity.setId(target.getId() != null ? target.getId().getValue() : null);
		entity.setCampaign(target.getCampaignId() != null
				? new MarketingCampaignEntity(target.getCampaignId().getValue())
				: null);
		entity.setMetricName(target.getMetricName());
		entity.setMetricType(target.getMetricType());
		entity.setTargetValue(target.getTargetValue());
		entity.setCurrentValue(target.getCurrentValue());
		entity.setMeasurementUnit(target.getMeasurementUnit());
		entity.setStatus(target.getStatus());
		entity.setStatusChangeReason(target.getStatusChangeReason());
		entity.setCreatedAt(target.getCreatedAt());
		entity.setUpdatedAt(target.getUpdatedAt());
		entity.setDeletedAt(target.getDeletedAt());
		entity.setVersion(target.getVersion());
		return entity;
	}
}
