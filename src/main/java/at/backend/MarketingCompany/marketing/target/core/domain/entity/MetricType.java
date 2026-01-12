package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Getter;

@Getter
public enum MetricType {
  COUNT(0, 0, "0", "999999999.99", true, "units"),
  CURRENCY(2, 2, "0.01", "999999999.99", false, "$"),
  PERCENTAGE(2, 2, "0", "100", false, "%"),
  DURATION(0, 2, "0", "8760", false, "hours"),
  COST(2, 2, "0.01", "1000000", false, "$"),
  RATIO(4, 4, "0", "1000", false, ":1"),
  SCORE(2, 2, "0", "1000", false, "points");

  private final int targetScale;
  private final int currentScale;
  private final BigDecimal minValue;
  private final BigDecimal maxValue;
  private final boolean integerOnly;
  private final String defaultUnit;

  MetricType(int targetScale, int currentScale, String minValue, String maxValue,
      boolean integerOnly, String defaultUnit) {
    this.targetScale = targetScale;
    this.currentScale = currentScale;
    this.minValue = new BigDecimal(minValue);
    this.maxValue = new BigDecimal(maxValue);
    this.integerOnly = integerOnly;
    this.defaultUnit = defaultUnit;
  }

  public BigDecimal normalizeTargetValue(BigDecimal value) {
    return value.setScale(targetScale, RoundingMode.HALF_UP);
  }

  public BigDecimal normalizeCurrentValue(BigDecimal value) {
    return value.setScale(currentScale, RoundingMode.HALF_UP);
  }
}
