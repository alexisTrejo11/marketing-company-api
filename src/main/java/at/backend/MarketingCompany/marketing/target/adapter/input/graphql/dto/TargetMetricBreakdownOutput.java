package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto;

public record TargetMetricBreakdownOutput(
		Long revenueTargets,
		Long leadsTargets,
		Long conversionsTargets,
		Long clicksTargets,
		Long impressionsTargets,
		Long engagementTargets,
		Long reachTargets,
		Long customTargets
) {
}
