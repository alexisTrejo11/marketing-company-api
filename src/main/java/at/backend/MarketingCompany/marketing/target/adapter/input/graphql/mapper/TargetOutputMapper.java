package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.target.core.application.dto.TargetStatistics;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TargetOutputMapper {

	public TargetOutput toOutput(CampaignTarget target) {
		return new TargetOutput(
				target.getId().getValue(),
				target.getCampaignId().getValue(),
				target.getMetricName(),
				target.getMetricType(),
				target.getMeasurementUnit(),
				target.getStatus() != null ? target.getStatus().name() : null,
				toProgressOutput(target),
				target.getCreatedAt() != null ? target.getCreatedAt().toString() : null,
				target.getUpdatedAt() != null ? target.getUpdatedAt().toString() : null
		);
	}

	public TargetProgressOutput toProgressOutput(CampaignTarget target) {
		return new TargetProgressOutput(
				target.getTargetValue(),
				target.getCurrentValue(),
				target.achievementPercentage(),
				target.isAchieved(),
				target.remainingValue()
		);
	}

	public PageResponse<TargetOutput> toPageResponse(Page<CampaignTarget> targetPage) {
		if (targetPage == null) {
			return null;
		}
		return PageResponse.of(targetPage.map(this::toOutput));
	}

	public TargetStatisticsOutput toStatisticsOutput(TargetStatistics statistics) {
		if (statistics == null) {
			return null;
		}

		TargetMetricBreakdownOutput breakdown = null;
		if (statistics.metricBreakdown() != null) {
			breakdown = new TargetMetricBreakdownOutput(
					statistics.metricBreakdown().revenueTargets(),
					statistics.metricBreakdown().leadsTargets(),
					statistics.metricBreakdown().conversionsTargets(),
					statistics.metricBreakdown().clicksTargets(),
					statistics.metricBreakdown().impressionsTargets(),
					statistics.metricBreakdown().engagementTargets(),
					statistics.metricBreakdown().reachTargets(),
					statistics.metricBreakdown().customTargets()
			);
		}

		var progressDetails = statistics.targetProgress() != null
				? statistics.targetProgress().stream()
				.map(detail -> new TargetProgressDetailOutput(
						detail.targetId(),
						detail.metricName(),
						detail.metricType(),
						detail.targetValue(),
						detail.currentValue(),
						detail.achievementPercentage(),
						detail.isAchieved(),
						detail.measurementUnit()
				))
				.toList()
				: java.util.List.<TargetProgressDetailOutput>of();

		return new TargetStatisticsOutput(
				statistics.campaignId(),
				statistics.campaignName(),
				statistics.totalTargets(),
				statistics.achievedTargets(),
				statistics.unachievedTargets(),
				statistics.achievementRate(),
				statistics.averageAchievementPercentage(),
				breakdown,
				progressDetails
		);
	}
}
