package at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence;

import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.OpportunityReconstructParams;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.LossReason;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.NextSteps;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Probability;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

@Component
public class OpportunityEntityMapper {

  public OpportunityEntity toEntity(Opportunity opportunity) {
    if (opportunity == null)
      return null;

    OpportunityEntity entity = new OpportunityEntity();

    // ID
    if (opportunity.getId() != null) {
      entity.setId(opportunity.getId().getValue());
    }

    // Basic fields
    entity.setTitle(opportunity.getTitle());
    entity.setStage(opportunity.getStage());
    entity.setProbability(opportunity.getProbability() != null ? opportunity.getProbability().value() : null);

    // Optional fields
    if (opportunity.getAmount() != null) {
      entity.setAmount(opportunity.getAmount().value());
    }
    if (opportunity.getExpectedCloseDate() != null) {
      entity.setExpectedCloseDate(opportunity.getExpectedCloseDate().value());
    }

    // Loss reason fields
    if (opportunity.getLossReason() != null) {
      entity.setLossReasonValue(opportunity.getLossReason().value());
      entity.setLossReasonDetails(opportunity.getLossReason().details());
    }

    // Next steps fields
    if (opportunity.getNextSteps() != null) {
      entity.setNextSteps(opportunity.getNextSteps().value());
      if (opportunity.getNextSteps().hasDueDate()) {
        entity.setNextStepsDueDate(opportunity.getNextSteps().dueDate());
      } else {
        entity.setNextStepsDueDate(null);
      }
    }

    // Audit fields
    entity.setCreatedAt(opportunity.getCreatedAt());
    entity.setUpdatedAt(opportunity.getUpdatedAt());
    entity.setDeletedAt(opportunity.getDeletedAt());
    entity.setVersion(opportunity.getVersion());

    // Relations - solo IDs
    if (opportunity.getCustomerCompanyId() != null) {
      entity.setCustomerCompany(new CustomerCompanyEntity(opportunity.getCustomerCompanyId().getValue()));
    }

    return entity;
  }

  public Opportunity toDomain(OpportunityEntity entity) {
    if (entity == null)
      return null;

    // Build LossReason
    LossReason lossReason = null;
    if (entity.getLossReasonValue() != null && !entity.getLossReasonValue().isBlank()) {
      lossReason = LossReason.create(
          entity.getLossReasonValue(),
          entity.getLossReasonDetails());
    }

    // Build NextSteps
    NextSteps nextSteps = null;
    if (entity.getNextSteps() != null) {
      nextSteps = NextSteps.create(
          entity.getNextSteps(),
          entity.getNextStepsDueDate());
    }

    // Build Probability
    Probability probability = null;
    if (entity.getProbability() != null) {
      probability = Probability.of(entity.getProbability());
    }

    var reconstructParams = OpportunityReconstructParams.builder()
        .id(new OpportunityId(entity.getId()))
        .customerCompanyId(
            entity.getCustomerCompany() != null ? new CustomerCompanyId(entity.getCustomerCompany().getId()) : null)
        .title(entity.getTitle())
        .amount(entity.getAmount() != null ? new Amount(entity.getAmount()) : null)
        .stage(entity.getStage())
        .expectedCloseDate(
            entity.getExpectedCloseDate() != null ? ExpectedCloseDate.from(entity.getExpectedCloseDate()) : null)
        .lossReason(lossReason)
        .nextSteps(nextSteps)
        .probability(probability)
        .version(entity.getVersion())
        .deletedAt(entity.getDeletedAt())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();

    return Opportunity.reconstruct(reconstructParams);
  }

  public void updateEntity(OpportunityEntity existingEntity, Opportunity opportunity) {
    existingEntity.setTitle(opportunity.getTitle());
    existingEntity.setStage(opportunity.getStage());
    existingEntity.setProbability(opportunity.getProbability() != null ? opportunity.getProbability().value() : null);

    // Amount
    if (opportunity.getAmount() != null) {
      existingEntity.setAmount(opportunity.getAmount().value());
    }

    // Expected Close Date
    if (opportunity.getExpectedCloseDate() != null) {
      existingEntity.setExpectedCloseDate(opportunity.getExpectedCloseDate().value());
    }

    // Loss Reason
    if (opportunity.getLossReason() != null) {
      LossReason lossReason = opportunity.getLossReason();
      existingEntity.setLossReasonValue(lossReason.value());
      existingEntity.setLossReasonDetails(lossReason.details());
    }

    // Next Steps
    if (opportunity.getNextSteps() != null) {
      existingEntity.setNextSteps(opportunity.getNextSteps().value());
      if (opportunity.getNextSteps().hasDueDate()) {
        existingEntity.setNextStepsDueDate(opportunity.getNextSteps().dueDate());
      } else {
        existingEntity.setNextStepsDueDate(null);
      }
    } else {
      existingEntity.setNextSteps(null);
      existingEntity.setNextStepsDueDate(null);
    }

  }
}
