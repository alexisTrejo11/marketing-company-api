package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;

import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.AchieveTargetInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.CreateTargetInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.DeleteTargetInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.ReactivateTargetInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.RecalculateTargetInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.ResetTargetProgressInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.TargetStatusChangeInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.UpdateTargetInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input.UpdateTargetProgressInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.mapper.TargetOutputMapper;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output.TargetOutput;
import at.backend.MarketingCompany.marketing.target.core.application.command.DeleteTargetCommand;
import at.backend.MarketingCompany.marketing.target.core.application.command.TargetStatusChangeCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.ports.input.TargetCommandInputPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TargetMutationController {

  private final TargetCommandInputPort commandService;
  private final TargetOutputMapper targetMapper;

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER')")
  public TargetOutput createTarget(@Argument @Valid @NotNull CreateTargetInput input) {
    log.debug("GraphQL Mutation: createTarget with metric: {}", input.metricName());
    CampaignTarget target = commandService.createTarget(input.toCommand());
    return targetMapper.toOutput(target);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER')")
  public TargetOutput updateTarget(@Argument @Valid @NotNull UpdateTargetInput input) {
    log.debug("GraphQL Mutation: updateTarget with id: {}", input.id());
    CampaignTarget target = commandService.updateTarget(input.toCommand());
    return targetMapper.toOutput(target);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER')")
  public TargetOutput updateTargetProgress(@Argument @Valid @NotNull UpdateTargetProgressInput input) {
    log.debug("GraphQL Mutation: updateTargetProgress with id: {}, value: {}",
        input.targetId(), input.currentValue());
    CampaignTarget target = commandService.updateTargetProgress(input.toCommand());
    return targetMapper.toOutput(target);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER')")
  public TargetOutput achieveTarget(@Argument @Valid @NotNull AchieveTargetInput input) {
    log.debug("GraphQL Mutation: achieveTarget with id: {}", input.targetId());
    TargetStatusChangeCommand command = input.toCommand();
    CampaignTarget target = commandService.changeTargetStatus(command);
    return targetMapper.toOutput(target);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER')")
  public TargetOutput reactivateTarget(@Argument @Valid @NotNull ReactivateTargetInput input) {
    log.debug("GraphQL Mutation: reactivateTarget with id: {}", input.targetId());
    TargetStatusChangeCommand command = input.toCommand();
    CampaignTarget target = commandService.changeTargetStatus(command);
    return targetMapper.toOutput(target);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER')")
  public TargetOutput resetTargetProgress(@Argument @Valid @NotNull ResetTargetProgressInput input) {
    log.debug("GraphQL Mutation: resetTargetProgress with id: {}", input.targetId());

    TargetStatusChangeCommand command = input.toCommand();
    CampaignTarget target = commandService.changeTargetStatus(command);

    return targetMapper.toOutput(target);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER')")
  public TargetOutput changeTargetStatus(@Argument @Valid @NotNull TargetStatusChangeInput input) {
    log.debug("GraphQL Mutation: changeTargetStatus with id: {}, status: {}",
        input.targetId(), input.status());

    TargetStatusChangeCommand command = input.toCommand();
    CampaignTarget target = commandService.changeTargetStatus(command);

    return targetMapper.toOutput(target);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER')")
  public TargetOutput recalculateTarget(@Argument @Valid @NotNull RecalculateTargetInput input) {
    log.debug("GraphQL Mutation: recalculateTarget with id: {}", input.targetId());
    CampaignTargetId targetId = new CampaignTargetId(input.targetId());
    CampaignTarget target = commandService.recalculateTarget(targetId);
    return targetMapper.toOutput(target);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasRole('ADMIN')")
  public Boolean deleteTargetById(@Argument @NotNull DeleteTargetInput input) {
    log.debug("GraphQL Mutation: deleteTargetById with id: {}", input.targetId());

    DeleteTargetCommand command = new DeleteTargetCommand(
        new CampaignTargetId(input.targetId()),
        input.hardDelete());

    commandService.deleteTarget(command);
    return true;
  }
}
