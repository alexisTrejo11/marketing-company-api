package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.application.command.UpdateTargetCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateTargetInput(
		@NotNull @Positive Long id,
		String metricName,
		MetricType metricType,
		@Positive BigDecimal targetValue,
		String measurementUnit
) {
	public UpdateTargetCommand toCommand() {
		return new UpdateTargetCommand(
				new CampaignTargetId(id),
				metricName,
				metricType,
				targetValue,
				measurementUnit
		);
	}
}
