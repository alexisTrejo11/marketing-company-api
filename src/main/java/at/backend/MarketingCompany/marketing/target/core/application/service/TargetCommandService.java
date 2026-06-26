package at.backend.MarketingCompany.marketing.target.core.application.service;

import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.marketing.target.core.application.command.*;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetNotFoundException;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetValidationException;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.port.input.TargetCommandInputPort;
import at.backend.MarketingCompany.marketing.target.core.port.output.TargetOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TargetCommandService implements TargetCommandInputPort {

	private final TargetOutputPort targetRepository;
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional
	public CampaignTarget createTarget(CreateTargetCommand command) {
		log.debug("Creating target for campaign: {}, metricName: {}",
				command.campaignId().getValue(), command.metricName());

		if (!campaignRepository.existsById(command.campaignId())) {
			throw new TargetValidationException(command.campaignId());
		}
		if (targetRepository.existsByCampaignIdAndMetricName(command.campaignId(), command.metricName())) {
			throw new TargetValidationException("Metric name already exists for this campaign: " + command.metricName());
		}

		CampaignTarget target = CampaignTarget.create(command.toCreateParams());
		CampaignTarget savedTarget = targetRepository.save(target);
		log.info("Target created successfully with ID: {}", savedTarget.getId().getValue());
		return savedTarget;
	}

	@Override
	@Transactional
	public CampaignTarget updateTarget(UpdateTargetCommand command) {
		log.debug("Updating target: {}", command.id().getValue());

		CampaignTarget target = findTargetByIdOrThrow(command.id());
		if (command.metricName() != null
				&& !command.metricName().equalsIgnoreCase(target.getMetricName())
				&& targetRepository.existsByCampaignIdAndMetricName(target.getCampaignId(), command.metricName())) {
			throw new TargetValidationException("Metric name already exists for this campaign: " + command.metricName());
		}

		target.update(command.metricName(), command.metricType(), command.targetValue(), command.measurementUnit());
		CampaignTarget updatedTarget = targetRepository.save(target);
		log.info("Target updated successfully: {}", command.id().getValue());
		return updatedTarget;
	}

	@Override
	@Transactional
	public CampaignTarget updateTargetProgress(UpdateTargetProgressCommand command) {
		log.debug("Updating target progress: {} to {}", command.targetId().getValue(), command.currentValue());

		CampaignTarget target = findTargetByIdOrThrow(command.targetId());
		target.updateProgress(command.currentValue());

		CampaignTarget updatedTarget = targetRepository.save(target);
		log.info("Target progress updated successfully: {}", command.targetId().getValue());
		return updatedTarget;
	}

	@Override
	@Transactional
	public CampaignTarget changeTargetStatus(ChangeTargetStatusCommand command) {
		log.debug("Changing target status: {} to {}", command.targetId().getValue(), command.status());

		CampaignTarget target = findTargetByIdOrThrow(command.targetId());
		target.changeStatus(command.status(), command.reason());

		CampaignTarget updatedTarget = targetRepository.save(target);
		log.info("Target status changed successfully: {}", command.targetId().getValue());
		return updatedTarget;
	}

	@Override
	@Transactional
	public CampaignTarget recalculateTarget(RecalculateTargetCommand command) {
		log.debug("Recalculating target: {}", command.targetId().getValue());

		CampaignTarget target = findTargetByIdOrThrow(command.targetId());
		target.recalculate();

		CampaignTarget updatedTarget = targetRepository.save(target);
		log.info("Target recalculated successfully: {}", command.targetId().getValue());
		return updatedTarget;
	}

	@Override
	@Transactional
	public void deleteTarget(DeleteTargetCommand command) {
		log.debug("Deleting target: {}, hardDelete={}", command.targetId().getValue(), command.hardDelete());

		CampaignTarget target = findTargetByIdOrThrow(command.targetId());
		if (command.hardDelete()) {
			targetRepository.hardDelete(command.targetId());
		} else {
			target.softDelete();
			targetRepository.save(target);
		}

		log.info("Target deleted successfully: {}", command.targetId().getValue());
	}

	private CampaignTarget findTargetByIdOrThrow(CampaignTargetId targetId) {
		return targetRepository.findById(targetId)
				.orElseThrow(() -> new TargetNotFoundException(targetId));
	}
}
