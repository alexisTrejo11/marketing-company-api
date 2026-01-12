
package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record TargetOutput(
    String id,
    String campaignId,
    String metricName,
    String metricType,
    String measurementUnit,
    String status,
    TargetProgressOutput progress,
    String createdAt,
    String updatedAt) {
}
