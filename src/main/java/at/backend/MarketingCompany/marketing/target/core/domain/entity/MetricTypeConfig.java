package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import lombok.Getter;

@Getter
public class MetricTypeConfig {

  private final int targetScale;
  private final int currentScale;
  private final BigDecimal minValue;
  private final BigDecimal maxValue;
  private final boolean integerOnly;
  private final String defaultUnit;

  private static final BigDecimal MAX_TARGET_VALUE = new BigDecimal("999999999.99");
  private static final BigDecimal MIN_COST_VALUE = new BigDecimal("0.01");
  private static final BigDecimal MAX_PERCENTAGE = new BigDecimal("100");
  private static final BigDecimal ONE_YEAR_HOURS = new BigDecimal("8760"); // 365 * 24
  private static final BigDecimal ONE_MILLION = new BigDecimal("1000000");
  private static final BigDecimal MAX_RATIO_VALUE = new BigDecimal("1000");
  private static final BigDecimal MAX_SCORE_VALUE = new BigDecimal("1000");
  private static final int DEFAULT_SCALE = 4;
  private static final int MAX_UNIT_LENGTH = 50;

  private static final Map<MetricType, MetricTypeConfig> CONFIG_MAP = Map.of(
      MetricType.COUNT, new MetricTypeConfig(0, 0, BigDecimal.ZERO, MAX_TARGET_VALUE, true, "units"),
      MetricType.CURRENCY, new MetricTypeConfig(2, 2, MIN_COST_VALUE, MAX_TARGET_VALUE, false, "$"),
      MetricType.PERCENTAGE, new MetricTypeConfig(2, 2, BigDecimal.ZERO, MAX_PERCENTAGE, false, "%"),
      MetricType.DURATION, new MetricTypeConfig(0, 2, BigDecimal.ZERO, ONE_YEAR_HOURS, false, "hours"),
      MetricType.COST, new MetricTypeConfig(2, 2, MIN_COST_VALUE, ONE_MILLION, false, "$"),
      MetricType.RATIO, new MetricTypeConfig(4, 4, BigDecimal.ZERO, MAX_RATIO_VALUE, false, ":1"),
      MetricType.SCORE, new MetricTypeConfig(2, 2, BigDecimal.ZERO, MAX_SCORE_VALUE, false, "points"));

  private MetricTypeConfig(int targetScale, int currentScale,
      BigDecimal minValue, BigDecimal maxValue,
      boolean integerOnly, String defaultUnit) {
    this.targetScale = targetScale;
    this.currentScale = currentScale;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.integerOnly = integerOnly;
    this.defaultUnit = defaultUnit;
  }

  public static MetricTypeConfig getConfig(MetricType metricType) {
    if (metricType == null) {
      return getDefaultConfig();
    }
    return CONFIG_MAP.getOrDefault(metricType, getDefaultConfig());
  }

  private static MetricTypeConfig getDefaultConfig() {
    return new MetricTypeConfig(
        DEFAULT_SCALE, DEFAULT_SCALE,
        BigDecimal.ZERO, MAX_TARGET_VALUE,
        false, null);
  }

  public BigDecimal normalizeTargetValue(BigDecimal value) {
    if (value == null) {
      return BigDecimal.ZERO.setScale(targetScale, RoundingMode.HALF_UP);
    }
    return value.setScale(targetScale, RoundingMode.HALF_UP);
  }

  public BigDecimal normalizeCurrentValue(BigDecimal value) {
    if (value == null) {
      return BigDecimal.ZERO.setScale(currentScale, RoundingMode.HALF_UP);
    }
    return value.setScale(currentScale, RoundingMode.HALF_UP);
  }

  public boolean isValidValue(BigDecimal value, boolean isTargetValue) {
    if (value == null) {
      return false;
    }

    int scale = isTargetValue ? targetScale : currentScale;

    if (value.scale() > scale) {
      return false;
    }

    if (value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
      return false;
    }

    if (integerOnly && value.scale() > 0) {
      return false;
    }

    return true;
  }

  public void validateValue(BigDecimal value, String valueType, MetricType metricType) {
    if (value == null) {
      throw new IllegalArgumentException(valueType + " value cannot be null for metric type: " + metricType);
    }

    int scale = valueType.equals("target") ? targetScale : currentScale;

    // Verificar escala
    if (value.scale() > scale) {
      throw new IllegalArgumentException(
          String.format("%s value for %s cannot have more than %d decimal places",
              valueType, metricType, scale));
    }

    // Verificar rango mínimo
    if (value.compareTo(minValue) < 0) {
      throw new IllegalArgumentException(
          String.format("%s value for %s cannot be less than %s",
              valueType, metricType, minValue));
    }

    // Verificar rango máximo
    if (value.compareTo(maxValue) > 0) {
      throw new IllegalArgumentException(
          String.format("%s value for %s cannot exceed %s",
              valueType, metricType, maxValue));
    }

    // Verificar si debe ser entero
    if (integerOnly && value.scale() > 0) {
      throw new IllegalArgumentException(
          String.format("%s value for %s must be a whole number", valueType, metricType));
    }
  }

  public String formatValue(BigDecimal value) {
    if (value == null) {
      return "N/A";
    }

    BigDecimal normalized = normalizeCurrentValue(value);
    String formatted = normalized.toPlainString();

    if (defaultUnit != null && !defaultUnit.isBlank()) {
      return formatted + " " + defaultUnit;
    }
    return formatted;
  }

  public int getDisplayScale() {
    return switch (this) {
      case MetricTypeConfig config when config == getConfig(MetricType.PERCENTAGE) -> 1; // 99.9%
      case MetricTypeConfig config when config == getConfig(MetricType.SCORE) -> 1; // 950.5 points
      case MetricTypeConfig config when config == getConfig(MetricType.RATIO) -> 2; // 4.50:1
      default -> Math.max(targetScale, currentScale);
    };

  }

  public boolean isMonetary() {
    return this == getConfig(MetricType.CURRENCY) || this == getConfig(MetricType.COST);
  }

  public boolean isPercentage() {
    return this == getConfig(MetricType.PERCENTAGE);
  }

  public boolean requiresPositiveValue() {
    return isMonetary();
  }

  public String getUnitSymbol() {
    if (defaultUnit == null) {
      return "";
    }

    return switch (defaultUnit) {
      case "$" -> "$";
      case "%" -> "%";
      default -> defaultUnit;
    };
  }

  public static void validateMeasurementUnit(String unit) {
    if (unit == null || unit.isBlank()) {
      return;
    }

    String trimmed = unit.trim();
    if (trimmed.length() > MAX_UNIT_LENGTH) {
      throw new IllegalArgumentException(
          String.format("Measurement unit cannot exceed %d characters", MAX_UNIT_LENGTH));
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof MetricTypeConfig))
      return false;

    MetricTypeConfig other = (MetricTypeConfig) obj;
    return this.targetScale == other.targetScale &&
        this.currentScale == other.currentScale &&
        this.minValue.equals(other.minValue) &&
        this.maxValue.equals(other.maxValue) &&
        this.integerOnly == other.integerOnly &&
        (this.defaultUnit == null ? other.defaultUnit == null : this.defaultUnit.equals(other.defaultUnit));
  }

  @Override
  public int hashCode() {
    int result = targetScale;
    result = 31 * result + currentScale;
    result = 31 * result + minValue.hashCode();
    result = 31 * result + maxValue.hashCode();
    result = 31 * result + (integerOnly ? 1 : 0);
    result = 31 * result + (defaultUnit != null ? defaultUnit.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return String.format(
        "MetricTypeConfig[targetScale=%d, currentScale=%d, minValue=%s, maxValue=%s, integerOnly=%b, defaultUnit=%s]",
        targetScale, currentScale, minValue, maxValue, integerOnly, defaultUnit);
  }
}
