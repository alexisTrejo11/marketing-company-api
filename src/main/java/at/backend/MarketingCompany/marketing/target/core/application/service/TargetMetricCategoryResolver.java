package at.backend.MarketingCompany.marketing.target.core.application.service;

public final class TargetMetricCategoryResolver {

	private TargetMetricCategoryResolver() {
	}

	public static String resolveCategory(String metricName) {
		if (metricName == null || metricName.isBlank()) {
			return "custom";
		}

		String normalized = metricName.toLowerCase();
		if (normalized.contains("revenue") || normalized.contains("roi") || normalized.contains("sales")) {
			return "revenue";
		}
		if (normalized.contains("lead")) {
			return "leads";
		}
		if (normalized.contains("conversion")) {
			return "conversions";
		}
		if (normalized.contains("click")) {
			return "clicks";
		}
		if (normalized.contains("impression")) {
			return "impressions";
		}
		if (normalized.contains("engagement")) {
			return "engagement";
		}
		if (normalized.contains("reach")) {
			return "reach";
		}
		return "custom";
	}
}
