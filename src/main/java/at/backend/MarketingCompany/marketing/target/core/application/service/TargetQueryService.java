package at.backend.MarketingCompany.marketing.target.core.application.service;

import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.marketing.target.core.application.dto.TargetStatistics;
import at.backend.MarketingCompany.marketing.target.core.application.query.TargetQuery;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetNotFoundException;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.port.input.TargetQueryInputPort;
import at.backend.MarketingCompany.marketing.target.core.port.input.TargetStatisticInputPort;
import at.backend.MarketingCompany.marketing.target.core.port.output.TargetOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TargetQueryService implements TargetQueryInputPort, TargetStatisticInputPort {

	private final TargetOutputPort targetRepository;
	private final CampaignRepositoryPort campaignRepository;

	@Override
	@Transactional(readOnly = true)
	public CampaignTarget getTargetById(CampaignTargetId id) {
		return findTargetByIdOrThrow(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignTarget> searchTargets(TargetQuery query, Pageable pageable) {
		if (query == null || query.isEmpty()) {
			return targetRepository.findAll(pageable);
		}
		return targetRepository.findByFilters(query, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignTarget> getTargetsByCampaign(MarketingCampaignId campaignId, Pageable pageable) {
		return targetRepository.findByCampaignId(campaignId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignTarget> getAchievedTargets(MarketingCampaignId campaignId, Pageable pageable) {
		return targetRepository.findByCampaignIdAndAchieved(campaignId, true, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignTarget> getUnachievedTargets(MarketingCampaignId campaignId, Pageable pageable) {
		return targetRepository.findByCampaignIdAndAchieved(campaignId, false, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CampaignTarget> getUnachievedTargetsByMetricType(MetricType metricType, Pageable pageable) {
		return targetRepository.findByMetricTypeAndNotAchieved(metricType, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public CampaignTarget getTargetByMetricName(MarketingCampaignId campaignId, String metricName) {
		return targetRepository.findByCampaignIdAndMetricName(campaignId, metricName)
				.orElseThrow(() -> new TargetNotFoundException(campaignId, metricName));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isTargetNameAvailable(MarketingCampaignId campaignId, String metricName) {
		return !targetRepository.existsByCampaignIdAndMetricName(campaignId, metricName);
	}

	@Override
	@Transactional(readOnly = true)
	public TargetStatistics getTargetStatistics(MarketingCampaignId campaignId) {
		log.debug("Getting target statistics for campaign: {}", campaignId);

		List<CampaignTarget> targets = targetRepository.findAllByCampaignId(campaignId);
		long totalTargets = targets.size();
		long achievedTargets = targets.stream().filter(CampaignTarget::isAchieved).count();
		long unachievedTargets = totalTargets - achievedTargets;
		Double achievementRate = calculateAchievementRate(totalTargets, achievedTargets);
		BigDecimal averageAchievement = targetRepository.calculateAverageAchievementByCampaignId(campaignId);

		TargetStatistics.TargetMetricBreakdown breakdown = buildMetricBreakdown(targets);
		List<TargetStatistics.TargetProgressDetail> progressDetails = targets.stream()
				.map(this::toProgressDetail)
				.toList();

		MarketingCampaign campaign = campaignRepository.findById(campaignId).orElse(null);

		return TargetStatistics.builder()
				.campaignId(campaignId.getValue())
				.campaignName(campaign != null && campaign.getName() != null ? campaign.getName().value() : null)
				.totalTargets(totalTargets)
				.achievedTargets(achievedTargets)
				.unachievedTargets(unachievedTargets)
				.achievementRate(achievementRate)
				.averageAchievementPercentage(averageAchievement)
				.metricBreakdown(breakdown)
				.targetProgress(progressDetails)
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public Double getTargetAchievementRate(MarketingCampaignId campaignId) {
		long total = targetRepository.countByCampaignId(campaignId);
		long achieved = targetRepository.countAchievedByCampaignId(campaignId);
		return calculateAchievementRate(total, achieved);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getAverageAchievementPercentage(MarketingCampaignId campaignId) {
		return targetRepository.calculateAverageAchievementByCampaignId(campaignId);
	}

	@Override
	@Transactional(readOnly = true)
	public int countAchievedTargets(MarketingCampaignId campaignId) {
		return (int) targetRepository.countAchievedByCampaignId(campaignId);
	}

	private CampaignTarget findTargetByIdOrThrow(CampaignTargetId id) {
		return targetRepository.findById(id)
				.orElseThrow(() -> new TargetNotFoundException(id));
	}

	private Double calculateAchievementRate(long total, long achieved) {
		if (total == 0) {
			return 0.0;
		}
		return BigDecimal.valueOf(achieved)
				.divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.doubleValue();
	}

	private TargetStatistics.TargetMetricBreakdown buildMetricBreakdown(List<CampaignTarget> targets) {
		long revenue = 0, leads = 0, conversions = 0, clicks = 0, impressions = 0, engagement = 0, reach = 0, custom = 0;

		for (CampaignTarget target : targets) {
			switch (TargetMetricCategoryResolver.resolveCategory(target.getMetricName())) {
				case "revenue" -> revenue++;
				case "leads" -> leads++;
				case "conversions" -> conversions++;
				case "clicks" -> clicks++;
				case "impressions" -> impressions++;
				case "engagement" -> engagement++;
				case "reach" -> reach++;
				default -> custom++;
			}
		}

		return TargetStatistics.TargetMetricBreakdown.builder()
				.revenueTargets(revenue)
				.leadsTargets(leads)
				.conversionsTargets(conversions)
				.clicksTargets(clicks)
				.impressionsTargets(impressions)
				.engagementTargets(engagement)
				.reachTargets(reach)
				.customTargets(custom)
				.build();
	}

	private TargetStatistics.TargetProgressDetail toProgressDetail(CampaignTarget target) {
		return TargetStatistics.TargetProgressDetail.builder()
				.targetId(target.getId().getValue())
				.metricName(target.getMetricName())
				.metricType(target.getMetricType() != null ? target.getMetricType().name() : null)
				.targetValue(target.getTargetValue())
				.currentValue(target.getCurrentValue())
				.achievementPercentage(target.achievementPercentage())
				.isAchieved(target.isAchieved())
				.measurementUnit(target.getMeasurementUnit())
				.build();
	}
}
