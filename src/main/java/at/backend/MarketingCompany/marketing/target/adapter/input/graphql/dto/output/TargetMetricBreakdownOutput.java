package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.output;

public record TargetMetricBreakdownOutput(
    int revenueTargets,
    int leadsTargets,
    int conversionsTargets,
    int clicksTargets,
    int impressionsTargets,
    int engagementTargets,
    int reachTargets,
    int customTargets) {
}
