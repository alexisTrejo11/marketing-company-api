package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetAchievementException;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetStateException;
import at.backend.MarketingCompany.marketing.target.core.domain.exception.TargetValidationException;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

@Getter
public class CampaignTarget extends BaseDomainEntity<CampaignTargetId> {

  private MarketingCampaignId campaignId;
  private String metricName;
  private MetricType metricType;
  private BigDecimal targetValue;
  private BigDecimal currentValue;
  private String measurementUnit;
  private CampaignTargetStatus status;
  private static final BigDecimal ARCHIVE_THRESHOLD = new BigDecimal("90");
  private static final BigDecimal ZERO = BigDecimal.ZERO;
  private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

  protected CampaignTarget(CampaignTargetId id) {
    super(id);
    this.currentValue = ZERO;
    this.status = CampaignTargetStatus.PENDING;
  }

  public static CampaignTarget create(CreateTargetParams params) {
    Objects.requireNonNull(params, "CreateTargetParams cannot be null");

    CampaignTarget target = new CampaignTarget(CampaignTargetId.generate());
    target.initialize(params);
    target.validateState();

    return target;
  }

  private void initialize(CreateTargetParams params) {
    validateParams(params);

    this.campaignId = params.campaignId();
    this.metricName = normalizeMetricName(params.metricName());
    this.metricType = params.metricType();

    MetricTypeConfig config = MetricTypeConfig.getConfig(metricType);
    this.targetValue = normalizeValue(params.targetValue(), config.getTargetScale());
    this.currentValue = ZERO.setScale(config.getCurrentScale(), RoundingMode.HALF_UP);
    this.measurementUnit = determineMeasurementUnit(params.measurementUnit(), config);
  }

  private void validateMetricName(String metricName) {
    if (metricName == null || metricName.isBlank()) {
      throw new TargetValidationException("Metric name is required");
    }

    String trimmed = metricName.trim();
    if (trimmed.length() < 3) {
      throw new TargetValidationException("Metric name must be at least 3 characters");
    }
    if (trimmed.length() > 200) {
      throw new TargetValidationException("Metric name cannot exceed 200 characters");
    }
  }

  private void validateTargetValue(BigDecimal targetValue, MetricType metricType) {
    if (targetValue == null) {
      throw new TargetValidationException("Target value is required");
    }

    MetricTypeConfig config = MetricTypeConfig.getConfig(metricType);

    if (targetValue.compareTo(config.getMinValue()) < 0) {
      throw new TargetValidationException(
          String.format("Target value must be at least %s for %s",
              config.getMinValue(), metricType));
    }

    if (targetValue.compareTo(config.getMaxValue()) > 0) {
      throw new TargetValidationException(
          String.format("Target value cannot exceed %s for %s",
              config.getMaxValue(), metricType));
    }

    if (config.isIntegerOnly() && targetValue.scale() > 0) {
      throw new TargetValidationException(
          String.format("Target value for %s must be a whole number", metricType));
    }
  }

  public void updateCurrentValue(BigDecimal newValue, String reason) {
    Objects.requireNonNull(newValue, "New value cannot be null");
    requireNonEmpty(reason, "Update reason is required");
    validateCurrentValue(newValue, metricType);

    MetricTypeConfig config = MetricTypeConfig.getConfig(metricType);
    this.currentValue = normalizeValue(newValue, config.getCurrentScale());
  }

  private void validateCurrentValue(BigDecimal value, MetricType metricType) {
    MetricTypeConfig config = MetricTypeConfig.getConfig(metricType);

    if (value.compareTo(config.getMinValue()) < 0) {
      throw new TargetValidationException(
          String.format("Current value cannot be less than %s", config.getMinValue()));
    }
  }

  public BigDecimal achievementPercentage() {
    if (targetValue.compareTo(ZERO) == 0) {
      return ZERO;
    }

    try {
      return currentValue.multiply(ONE_HUNDRED)
          .divide(targetValue, 2, RoundingMode.HALF_UP);
    } catch (ArithmeticException e) {
      return ZERO;
    }
  }

  public BigDecimal getRemainingValue() {
    if (isAchieved()) {
      return ZERO;
    }

    BigDecimal remaining = targetValue.subtract(currentValue);
    return remaining.max(ZERO);
  }

  public void achieved(String reason) {
    if (isAchieved()) {
      throw new TargetAchievementException("Target is already achieved");
    }

    validateReason(reason, "achieve");

    BigDecimal achievement = achievementPercentage();
    if (achievement.compareTo(ARCHIVE_THRESHOLD) < 0) {
      throw new TargetAchievementException(
          String.format("Cannot archive target with less than %s%% achievement", ARCHIVE_THRESHOLD));
    }

    this.status = CampaignTargetStatus.ACHIEVED;
  }

  public void cancel(String reason) {
    if (status == CampaignTargetStatus.CANCELLED) {
      throw new TargetStateException("Target is already cancelled");
    }

    validateReason(reason, "cancel");

    this.status = CampaignTargetStatus.CANCELLED;
  }

  public void fail(String reason) {
    if (status == CampaignTargetStatus.FAILED) {
      throw new TargetStateException("Target is already marked as failed");
    }

    if (status == CampaignTargetStatus.ACHIEVED) {
      throw new TargetStateException("Achieved targets cannot be marked as failed");
    }

    validateReason(reason, "mark as failed");

    this.status = CampaignTargetStatus.FAILED;
  }

  public void start(String reason) {
    validateReason(reason, "start");
    if (status != CampaignTargetStatus.PENDING) {
      throw new TargetStateException("Only pending targets can be marked as in progress");
    }

    this.status = CampaignTargetStatus.IN_PROGRESS;
  }

  public void reset(String reason) {
    if (!isAchieved()) {
      throw new TargetStateException("Only achieved targets can be reset");
    }

    validateReason(reason, "reset");

    this.currentValue = ZERO;
    this.status = CampaignTargetStatus.PENDING;
  }

  public void start() {
    if (status != CampaignTargetStatus.PENDING) {
      throw new TargetStateException("Only pending targets can be started");
    }

    this.status = CampaignTargetStatus.IN_PROGRESS;
  }

  public void updateDetails(String metricName, BigDecimal targetValue, String measurementUnit) {
    if (metricName != null) {
      validateMetricName(metricName);
      this.metricName = normalizeMetricName(metricName);
    }

    if (targetValue != null) {
      validateTargetValue(targetValue, this.metricType);
      MetricTypeConfig config = MetricTypeConfig.getConfig(this.metricType);
      this.targetValue = normalizeValue(targetValue, config.getTargetScale());
    }

    if (measurementUnit != null && !measurementUnit.isBlank()) {
      this.measurementUnit = measurementUnit.trim();
    }

    validateAchievementConsistency();
  }

  private void validateState() {
    if (currentValue.compareTo(targetValue) > 0) {
      throw new TargetStateException("Current value cannot exceed target value at creation");
    }
  }

  private void validateAchievementConsistency() {
    BigDecimal achievement = achievementPercentage();

    if (achievement.compareTo(ONE_HUNDRED) > 0 && !isAchieved()) {
      throw new TargetStateException(
          "Inconsistent state: Achievement > 100% but target not marked as achieved");
    }

    if (isAchieved() && achievement.compareTo(ONE_HUNDRED) < 0) {
      throw new TargetStateException(
          "Inconsistent state: Target marked as achieved but achievement < 100%");
    }

  }

  public static CampaignTarget reconstruct(CampaignTargetReconstructParams params) {
    Objects.requireNonNull(params, "Reconstruct params cannot be null");

    CampaignTarget target = new CampaignTarget(params.id());
    target.reinitialize(params);
    target.validateConsistency();

    return target;
  }

  private void reinitialize(CampaignTargetReconstructParams params) {
    this.id = params.id();
    this.campaignId = params.campaignId();
    this.metricName = params.metricName();
    this.metricType = params.metricType();

    MetricTypeConfig config = MetricTypeConfig.getConfig(metricType);

    this.targetValue = params.targetValue() != null
        ? normalizeValue(params.targetValue(), config.getTargetScale())
        : ZERO;

    this.currentValue = params.currentValue() != null
        ? normalizeValue(params.currentValue(), config.getCurrentScale())
        : ZERO;

    this.measurementUnit = params.measurementUnit();
    this.status = params.status();
    this.createdAt = params.createdAt();
    this.updatedAt = params.updatedAt();
    this.deletedAt = params.deletedAt();
    this.version = params.version();
  }

  public void validateConsistency() {
    validateAchievementConsistency();

    if (currentValue.compareTo(targetValue) > 0 && !isAchieved()) {
      throw new TargetStateException(
          "Inconsistent state: Current value exceeds target but target not marked as achieved");
    }
  }

  private String normalizeMetricName(String name) {
    return name != null ? name.trim() : null;
  }

  private BigDecimal normalizeValue(BigDecimal value, int scale) {
    return value.setScale(scale, RoundingMode.HALF_UP);
  }

  private String determineMeasurementUnit(String providedUnit, MetricTypeConfig config) {
    if (providedUnit != null && !providedUnit.isBlank()) {
      String trimmed = providedUnit.trim();
      if (trimmed.length() > 50) {
        throw new TargetValidationException("Measurement unit cannot exceed 50 characters");
      }
      return trimmed;
    }
    return config.getDefaultUnit();
  }

  private static void requireNonEmpty(String value, String message) {
    if (value == null || value.trim().isEmpty()) {
      throw new TargetValidationException(message);
    }
  }

  private void validateReason(String reason, String operation) {
    requireNonEmpty(reason,
        String.format("%s reason is required",
            operation.substring(0, 1).toUpperCase() + operation.substring(1)));
  }

  private void validateCampaignId(MarketingCampaignId campaignId) {
    if (campaignId == null) {
      throw new TargetValidationException("Campaign ID is required");
    }
  }

  private void validateMetricType(MetricType metricType) {
    if (metricType == null) {
      throw new TargetValidationException("Metric type is required");
    }
  }

  private void validateParams(CreateTargetParams params) {
    validateCampaignId(params.campaignId());
    validateMetricName(params.metricName());
    validateMetricType(params.metricType());
    validateTargetValue(params.targetValue(), params.metricType());
  }

  public boolean isAchieved() {
    return status == CampaignTargetStatus.ACHIEVED;
  }

  public BigDecimal getAchievementPercentage() {
    return ARCHIVE_THRESHOLD;
  }

}
