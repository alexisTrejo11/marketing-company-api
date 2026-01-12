package at.backend.MarketingCompany.marketing.target.adapter.output.persistence.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTargetStatus;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campaign_targets", indexes = {
  @Index(name = "idx_targets_campaign_metric", columnList = "campaign_id, metric_name"),
  @Index(name = "idx_targets_campaign_status", columnList = "campaign_id, status"),
  @Index(name = "idx_targets_metric_type", columnList = "metric_type"),
  @Index(name = "idx_targets_achievement_pct", columnList = "achievement_percentage"),
  @Index(name = "idx_targets_created_at", columnList = "created_at")
})
public class CampaignTargetEntity extends BaseJpaEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id", nullable = false)
  private MarketingCampaignEntity campaign;

  @Column(name = "metric_name", nullable = false, length = 200)
  private String metricName;

  @Enumerated(EnumType.STRING)
  @Column(name = "metric_type", nullable = false, length = 50)
  private MetricType metricType;

  @Column(name = "target_value", nullable = false, precision = 20, scale = 4)
  private BigDecimal targetValue;

  @Column(name = "current_value", nullable = false, precision = 20, scale = 4)
  private BigDecimal currentValue = BigDecimal.ZERO;

  @Column(name = "measurement_unit", length = 50)
  private String measurementUnit;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private CampaignTargetStatus status;

  public BigDecimal calculateAchievementPercentage() {
    if (targetValue.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    try {
      return currentValue.multiply(new BigDecimal("100"))
          .divide(targetValue, 2, java.math.RoundingMode.HALF_UP);
    } catch (ArithmeticException e) {
      return BigDecimal.ZERO;
    }
  }

}
