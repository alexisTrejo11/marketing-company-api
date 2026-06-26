package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.application.command.CreateTargetCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTargetInput(
		@NotNull @Positive Long campaignId,
		@NotBlank String metricName,
		@NotNull MetricType metricType,
		@NotNull @Positive BigDecimal targetValue,
		@NotBlank String measurementUnit
) {
	public CreateTargetCommand toCommand() {
		return new CreateTargetCommand(
				new MarketingCampaignId(campaignId),
				metricName,
				metricType,
				targetValue,
				measurementUnit
		);
	}
}
