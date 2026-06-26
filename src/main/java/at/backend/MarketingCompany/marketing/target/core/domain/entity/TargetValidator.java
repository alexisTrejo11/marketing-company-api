package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetValidationException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class TargetValidator {

	public static void validateForCreation(CreateTargetParams params) {
		if (params == null) {
			throw new TargetValidationException("Creation parameters are required");
		}

		if (params.campaignId() == null) {
			throw new TargetValidationException("Campaign ID is required");
		}

		if (params.metricName() == null || params.metricName().isBlank()) {
			throw new TargetValidationException("Metric name is required");
		}

		if (params.metricName().length() > 100) {
			throw new TargetValidationException("Metric name cannot exceed 100 characters");
		}

		if (params.metricType() == null) {
			throw new TargetValidationException("Metric type is required");
		}

		validateTargetValue(params.targetValue(), params.metricType());

		if (params.measurementUnit() == null || params.measurementUnit().isBlank()) {
			throw new TargetValidationException("Measurement unit is required");
		}
	}

	public static void validateForUpdate(String metricName, MetricType metricType, BigDecimal targetValue, String measurementUnit) {
		if (metricName != null && metricName.isBlank()) {
			throw new TargetValidationException("Metric name cannot be blank, if provided");
		}

		if (metricName != null && metricName.length() > 100) {
			throw new TargetValidationException("Metric name cannot exceed 100 characters");
		}

		if (targetValue != null) {
			validateTargetValue(targetValue, metricType);
		}

		if (measurementUnit != null && measurementUnit.isBlank()) {
			throw new TargetValidationException("Measurement unit cannot be blank, if provided");
		}
	}

	public static void validateProgressUpdate(BigDecimal currentValue) {
		if (currentValue == null) {
			throw new TargetValidationException("Current value is required");
		}

		if (currentValue.compareTo(BigDecimal.ZERO) < 0) {
			throw new TargetValidationException("Current value cannot be negative");
		}
	}

	private static void validateTargetValue(BigDecimal targetValue, MetricType metricType) {
		if (targetValue == null) {
			throw new TargetValidationException("Target value is required");
		}

		if (targetValue.compareTo(BigDecimal.ZERO) <= 0) {
			throw new TargetValidationException("Target value must be positive");
		}

		if (metricType == MetricType.PERCENTAGE
				&& (targetValue.compareTo(BigDecimal.ZERO) < 0 || targetValue.compareTo(new BigDecimal("100")) > 0)) {
			throw new TargetValidationException("Target value for percentage metrics must be between 0 and 100");
		}
	}
}
