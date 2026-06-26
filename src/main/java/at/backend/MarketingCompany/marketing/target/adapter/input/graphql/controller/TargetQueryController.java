package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.TargetFilterInput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.TargetOutput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.TargetStatisticsOutput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.mapper.TargetOutputMapper;
import at.backend.MarketingCompany.marketing.target.core.application.query.TargetQuery;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.port.input.TargetQueryInputPort;
import at.backend.MarketingCompany.marketing.target.core.port.input.TargetStatisticInputPort;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class TargetQueryController {

	private final TargetQueryInputPort queryPort;
	private final TargetStatisticInputPort statisticPort;
	private final TargetOutputMapper mapper;

	@QueryMapping
	public TargetOutput target(@Argument @Valid @NotNull @Positive Long id) {
		CampaignTarget target = queryPort.getTargetById(new CampaignTargetId(id));
		return mapper.toOutput(target);
	}

	@QueryMapping
	public PageResponse<TargetOutput> searchTargets(
			@Argument TargetFilterInput filter,
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		TargetQuery query = filter != null ? filter.toQuery() : TargetQuery.empty();
		Page<CampaignTarget> targetPage = queryPort.searchTargets(query, pageable);
		return mapper.toPageResponse(targetPage);
	}

	@QueryMapping
	public PageResponse<TargetOutput> targetsByCampaign(
			@Argument @Valid @NotNull @Positive Long campaignId,
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		Page<CampaignTarget> targetPage = queryPort.getTargetsByCampaign(new MarketingCampaignId(campaignId), pageable);
		return mapper.toPageResponse(targetPage);
	}

	@QueryMapping
	public PageResponse<TargetOutput> achievedTargets(
			@Argument @Valid @NotNull @Positive Long campaignId,
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		Page<CampaignTarget> targetPage = queryPort.getAchievedTargets(new MarketingCampaignId(campaignId), pageable);
		return mapper.toPageResponse(targetPage);
	}

	@QueryMapping
	public PageResponse<TargetOutput> unachievedTargets(
			@Argument @Valid @NotNull @Positive Long campaignId,
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		Page<CampaignTarget> targetPage = queryPort.getUnachievedTargets(new MarketingCampaignId(campaignId), pageable);
		return mapper.toPageResponse(targetPage);
	}

	@QueryMapping
	public PageResponse<TargetOutput> unachievedTargetsByMetricType(
			@Argument @Valid @NotNull MetricType metricType,
			@Argument @Valid @NotNull PageInput pageInput) {
		Pageable pageable = pageInput.toPageable();
		Page<CampaignTarget> targetPage = queryPort.getUnachievedTargetsByMetricType(metricType, pageable);
		return mapper.toPageResponse(targetPage);
	}

	@QueryMapping
	public TargetOutput targetByMetricName(
			@Argument @Valid @NotNull @Positive Long campaignId,
			@Argument @Valid @NotNull String metricName) {
		CampaignTarget target = queryPort.getTargetByMetricName(new MarketingCampaignId(campaignId), metricName);
		return mapper.toOutput(target);
	}

	@QueryMapping
	public TargetStatisticsOutput targetStatistics(@Argument @Valid @NotNull @Positive Long campaignId) {
		return mapper.toStatisticsOutput(statisticPort.getTargetStatistics(new MarketingCampaignId(campaignId)));
	}

	@QueryMapping
	public Double targetAchievementRate(@Argument @Valid @NotNull @Positive Long campaignId) {
		return statisticPort.getTargetAchievementRate(new MarketingCampaignId(campaignId));
	}

	@QueryMapping
	public BigDecimal averageAchievementPercentage(@Argument @Valid @NotNull @Positive Long campaignId) {
		return statisticPort.getAverageAchievementPercentage(new MarketingCampaignId(campaignId));
	}

	@QueryMapping
	public Integer countAchievedTargets(@Argument @Valid @NotNull @Positive Long campaignId) {
		return statisticPort.countAchievedTargets(new MarketingCampaignId(campaignId));
	}

	@QueryMapping
	public Boolean isTargetNameAvailable(
			@Argument @Valid @NotNull @Positive Long campaignId,
			@Argument @Valid @NotNull String metricName) {
		return queryPort.isTargetNameAvailable(new MarketingCampaignId(campaignId), metricName);
	}
}
