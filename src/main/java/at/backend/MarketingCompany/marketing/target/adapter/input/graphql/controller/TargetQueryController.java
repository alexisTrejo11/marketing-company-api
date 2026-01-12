package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.mapper.TargetOutputMapper;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output.TargetOutput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output.TargetStatisticsOutput;
import at.backend.MarketingCompany.marketing.target.core.application.service.TargetStatistics;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.ports.input.TargetQueryInputPort;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TargetQueryController {

  private final TargetQueryInputPort queryService;
  private final TargetOutputMapper mapper;

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public TargetOutput target(@Argument @NotNull Long id) {
    log.debug("GraphQL Query: target by id: {}", id);
    CampaignTargetId targetId = new CampaignTargetId(id);
    CampaignTarget target = queryService.getTargetById(targetId);
    return mapper.toOutput(target);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public PageResponse<TargetOutput> targetsByCampaign(
      @Argument @NotNull Long campaignId,
      @Argument @Valid PageInput pageInput) {
    log.debug("GraphQL Query: targetsByCampaign for campaign: {}", campaignId);

    var campaignIdObj = new MarketingCampaignId(campaignId);
    Pageable pageable = pageInput.toPageable();

    Page<CampaignTarget> result = queryService.getTargetsByCampaign(campaignIdObj, pageable);

    return mapper.toPageOutput(result);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public PageResponse<TargetOutput> achievedTargets(
      @Argument @NotNull Long campaignId,
      @Argument @Valid PageInput pageInput) {
    log.debug("GraphQL Query: achievedTargets for campaign: {}", campaignId);

    var campaignIdObj = new MarketingCampaignId(campaignId);
    Pageable pageable = pageInput.toPageable();

    Page<CampaignTarget> result = queryService.getAchievedTargets(campaignIdObj, pageable);

    return mapper.toPageOutput(result);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public PageResponse<TargetOutput> unachievedTargets(
      @Argument @NotNull Long campaignId,
      @Argument @Valid PageInput pageInput) {
    log.debug("GraphQL Query: unachievedTargets for campaign: {}", campaignId);

    var campaignIdObj = new MarketingCampaignId(campaignId);
    Pageable pageable = pageInput.toPageable();

    Page<CampaignTarget> result = queryService.getUnachievedTargets(campaignIdObj, pageable);

    return mapper.toPageOutput(result);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public PageResponse<TargetOutput> unachievedTargetsByMetricType(
      @Argument @NotNull MetricType metricType,
      @Argument @Valid PageInput pageInput) {
    log.debug("GraphQL Query: unachievedTargetsByMetricType for type: {}", metricType);

    Pageable pageable = pageInput.toPageable();
    Page<CampaignTarget> result = queryService.getUnachievedTargetsByMetricType(metricType, pageable);

    return mapper.toPageOutput(result);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public TargetOutput targetByMetricName(
      @Argument @NotNull Long campaignId,
      @Argument @NotNull String metricName) {
    log.debug("GraphQL Query: targetByMetricName for campaign: {}, metric: {}",
        campaignId, metricName);

    var campaignIdObj = new MarketingCampaignId(campaignId);
    CampaignTarget target = queryService.getCampaignTargetByMetricName(campaignIdObj, metricName);

    return mapper.toOutput(target);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public TargetStatisticsOutput targetStatistics(@Argument @NotNull Long campaignId) {
    log.debug("GraphQL Query: targetStatistics for campaign: {}", campaignId);
    TargetStatistics statistics = queryService.getTargetStatistics(new MarketingCampaignId(campaignId));
    return mapper.toStatisticsOutput(statistics);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public BigDecimal targetAchievementRate(@Argument @NotNull Long campaignId) {
    log.debug("GraphQL Query: targetAchievementRate for campaign: {}", campaignId);
    return queryService.getAchievementRate(new MarketingCampaignId(campaignId));
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public BigDecimal averageAchievementPercentage(@Argument @NotNull Long campaignId) {
    log.debug("GraphQL Query: averageAchievementPercentage for campaign: {}", campaignId);
    return queryService.getAverageAchievementPercentage(new MarketingCampaignId(campaignId));
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public Integer countAchievedTargets(@Argument @NotNull Long campaignId) {
    log.debug("GraphQL Query: countAchievedTargets for campaign: {}", campaignId);
    return queryService.countAchievedTargets(new MarketingCampaignId(campaignId));
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'CAMPAIGN_MANAGER', 'MANAGER', 'ANALYST')")
  public Boolean isTargetNameAvailable(
      @Argument @NotNull Long campaignId,
      @Argument @NotNull String metricName) {
    log.debug("GraphQL Query: isTargetNameAvailable for campaign: {}, metric: {}",
        campaignId, metricName);

    return queryService.isMetricNameAvailable(
        new MarketingCampaignId(campaignId),
        metricName);
  }
}
