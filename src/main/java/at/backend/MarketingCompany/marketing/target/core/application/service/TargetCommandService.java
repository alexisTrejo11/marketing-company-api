package at.backend.MarketingCompany.marketing.target.core.application.service;

import org.springframework.stereotype.Service;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.marketing.target.core.application.command.*;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetNotFoundException;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetValidationException;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.ports.input.TargetCommandInputPort;
import at.backend.MarketingCompany.marketing.target.core.ports.output.TargetRepositoryPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TargetCommandService implements TargetCommandInputPort {
  private final TargetRepositoryPort targetRepository;
  private final CampaignRepositoryPort campaignRepository;

  @Override
  @Transactional
  public CampaignTarget createTarget(CreateTargetCommand command) {
    log.info("Creating target for campaign: {}", command.campaignId());

    validateCampaignExists(command.campaignId());
    validateUniqueTargetMetric(command.campaignId(), command.metricName());

    CampaignTarget target = CampaignTarget.create(command.toParams());
    CampaignTarget savedTarget = targetRepository.save(target);

    log.info("Target created with ID: {}", savedTarget.getId().getValue());
    return savedTarget;
  }

  @Override
  @Transactional
  public CampaignTarget updateTarget(TargetUpdateCommand command) {
    CampaignTarget target = getTarget(command.targetId());

    if (command.hasProgressUpdate()) {
      target.updateCurrentValue(command.currentValue(), command.reason());
    }

    if (command.hasDetailsUpdate()) {
      target.updateDetails(command.metricName(), command.targetValue(), command.measurementUnit());
    }

    CampaignTarget updatedTarget = targetRepository.save(target);
    log.debug("Target updated: {}", command.targetId().getValue());

    return updatedTarget;
  }

  @Override
  @Transactional
  public CampaignTarget updateTargetProgress(UpdateTargetProgressCommand command) {
    CampaignTarget target = getTarget(command.targetId());
    target.updateCurrentValue(command.currentValue(), command.reason());

    return targetRepository.save(target);
  }

  @Override
  @Transactional
  public CampaignTarget changeTargetStatus(TargetStatusChangeCommand command) {
    CampaignTarget target = getTarget(command.targetId());

    switch (command.status()) {
      case CANCELLED -> target.cancel(command.reason());
      case FAILED -> target.fail(command.reason());
      case IN_PROGRESS -> target.start(command.reason());
      case ACHIEVED -> target.achieved(command.reason());
      case PENDING -> target.reset(command.reason());
    }
    log.info("Target {} status changed to: {}", command.targetId().getValue(), command.status());

    return targetRepository.save(target);
  }

  @Override
  @Transactional
  public void deleteTarget(DeleteTargetCommand command) {
    CampaignTarget target = getTarget(command.targetId());

    if (command.hardDelete()) {
      targetRepository.delete(target.getId());
      log.info("Target permanently deleted: {}", command.targetId().getValue());
    } else {
      target.softDelete();
      targetRepository.save(target);
      log.info("Target soft deleted: {}", command.targetId().getValue());
    }
  }

  @Override
  @Transactional
  public CampaignTarget recalculateTarget(CampaignTargetId targetId) {
    CampaignTarget target = getTarget(targetId);
    target.validateConsistency();

    return targetRepository.save(target);
  }

  private CampaignTarget getTarget(CampaignTargetId targetId) {
    return targetRepository.findById(targetId)
        .orElseThrow(() -> new TargetNotFoundException(targetId));
  }

  private void validateCampaignExists(MarketingCampaignId campaignId) {
    if (!campaignRepository.existsById(campaignId)) {
      throw new TargetValidationException("Campaign not found: " + campaignId.getValue());
    }
  }

  private void validateUniqueTargetMetric(MarketingCampaignId campaignId, String metricName) {
    if (targetRepository.existsByCampaignIdAndMetricName(campaignId, metricName)) {
      throw new BusinessRuleException(
          "Target with metric name '" + metricName + "' already exists for this campaign");
    }
  }
}
