package at.backend.MarketingCompany.marketing.target.core.application.service;

public record TargetMetricBreakdown(
    int revenueTargets,
    int leadsTargets,
    int conversionsTargets,
    int clicksTargets,
    int impressionsTargets,
    int engagementTargets,
    int reachTargets,
    int customTargets) {
  public static TargetMetricBreakdown empty() {
    return new TargetMetricBreakdown(0, 0, 0, 0, 0, 0, 0, 0);
  }
}
