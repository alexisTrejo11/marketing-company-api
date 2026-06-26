package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;

public record TargetOutput(
		Long id,
		Long campaignId,
		String metricName,
		MetricType metricType,
		String measurementUnit,
		String status,
		TargetProgressOutput progress,
		String createdAt,
		String updatedAt
) {
}
