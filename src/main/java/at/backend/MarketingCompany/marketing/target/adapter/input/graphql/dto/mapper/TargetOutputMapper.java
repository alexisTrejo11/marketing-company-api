package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.mapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output.TargetMetricBreakdownOutput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output.TargetOutput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output.TargetProgressDetailOutput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output.TargetProgressOutput;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output.TargetStatisticsOutput;
import at.backend.MarketingCompany.marketing.target.core.application.service.TargetMetricBreakdown;
import at.backend.MarketingCompany.marketing.target.core.application.service.TargetProgressDetail;
import at.backend.MarketingCompany.marketing.target.core.application.service.TargetStatistics;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.shared.PageResponse;

@Component
public class TargetOutputMapper {
  public TargetOutput toOutput(CampaignTarget target) {
    var progress = TargetProgressOutput.builder()
        .targetValue(target.getTargetValue())
        .currentValue(target.getCurrentValue())
        .achievementPercentage(target.getAchievementPercentage())
        .isAchieved(target.isAchieved())
        .remainingValue(target.getRemainingValue())
        .build();

    return TargetOutput.builder()
        .id(target.getId().getValue().toString())
        .campaignId(target.getCampaignId().getValue().toString())
        .metricName(target.getMetricName())
        .metricType(target.getMetricType().name())
        .measurementUnit(target.getMeasurementUnit())
        .status(target.getStatus().name())
        .progress(progress)
        .createdAt(target.getCreatedAt() != null ? target.getCreatedAt().toString() : null)
        .updatedAt(target.getUpdatedAt() != null ? target.getUpdatedAt().toString() : null)
        .build();
  }

  public PageResponse<TargetOutput> toPageOutput(Page<CampaignTarget> page) {
    if (page == null) {
      return null;
    }

    return PageResponse.of(page.map(this::toOutput));
  }

  public TargetProgressOutput toProgressOutput(CampaignTarget target) {
    if (target == null) {
      return null;
    }

    return TargetProgressOutput.builder()
        .targetValue(target.getTargetValue())
        .currentValue(target.getCurrentValue())
        .achievementPercentage(target.getAchievementPercentage())
        .isAchieved(target.isAchieved())
        .remainingValue(target.getRemainingValue())
        .build();
  }

  public TargetProgressOutput toProgressOutput(
      String targetId,
      String metricName,
      String metricType,
      java.math.BigDecimal targetValue,
      java.math.BigDecimal currentValue,
      java.math.BigDecimal achievementPercentage,
      boolean isAchieved,
      String measurementUnit) {
    return TargetProgressOutput.builder()
        .targetValue(targetValue)
        .currentValue(currentValue)
        .achievementPercentage(achievementPercentage)
        .isAchieved(isAchieved)
        .remainingValue(targetValue.subtract(currentValue))
        .build();
  }

  public TargetStatisticsOutput toStatisticsOutput(TargetStatistics statistics) {
    if (statistics == null) {
      return null;
    }

    return new TargetStatisticsOutput(
        statistics.campaignId(),
        statistics.campaignName(),
        statistics.totalTargets(),
        statistics.achievedTargets(),
        statistics.unachievedTargets(),
        statistics.achievementRate(),
        statistics.averageAchievementPercentage(),
        toMetricBreakdownOutput(statistics.metricBreakdown()),
        toProgressDetailOutputList(statistics.targetProgress()));
  }

  private TargetMetricBreakdownOutput toMetricBreakdownOutput(TargetMetricBreakdown breakdown) {
    if (breakdown == null) {
      return new TargetMetricBreakdownOutput(0, 0, 0, 0, 0, 0, 0, 0);
    }
    return new TargetMetricBreakdownOutput(
        breakdown.revenueTargets(),
        breakdown.leadsTargets(),
        breakdown.conversionsTargets(),
        breakdown.clicksTargets(),
        breakdown.impressionsTargets(),
        breakdown.engagementTargets(),
        breakdown.reachTargets(),
        breakdown.customTargets());
  }

  private List<TargetProgressDetailOutput> toProgressDetailOutputList(List<TargetProgressDetail> details) {
    if (details == null) {
      return List.of();
    }

    return details.stream()
        .map(this::toProgressDetailOutput)
        .toList();
  }

  private TargetProgressDetailOutput toProgressDetailOutput(TargetProgressDetail detail) {
    if (detail == null) {
      return null;
    }

    return new TargetProgressDetailOutput(
        detail.targetId(),
        detail.metricName(),
        detail.metricType(),
        detail.targetValue(),
        detail.currentValue(),
        detail.achievementPercentage(),
        detail.isAchieved(),
        detail.measurementUnit());
  }

}
