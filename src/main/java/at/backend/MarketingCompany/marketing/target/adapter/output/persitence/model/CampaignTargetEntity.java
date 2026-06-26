package at.backend.MarketingCompany.marketing.target.adapter.output.persitence.model;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.TargetStatus;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campaign_targets", indexes = {
		@Index(name = "idx_targets_campaign_status", columnList = "campaign_id, status"),
		@Index(name = "idx_targets_campaign_metric_type", columnList = "campaign_id, metric_type")
}, uniqueConstraints = {
		@UniqueConstraint(name = "uk_campaign_targets_campaign_metric_name", columnNames = {"campaign_id", "metric_name"})
})
public class CampaignTargetEntity extends BaseJpaEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaign_id", nullable = false)
	private MarketingCampaignEntity campaign;

	@Column(name = "metric_name", nullable = false, length = 100)
	private String metricName;

	@Enumerated(EnumType.STRING)
	@Column(name = "metric_type", nullable = false, length = 30)
	private MetricType metricType;

	@Column(name = "target_value", nullable = false, precision = 15, scale = 4)
	private BigDecimal targetValue;

	@Column(name = "current_value", precision = 15, scale = 4)
	private BigDecimal currentValue = BigDecimal.ZERO;

	@Column(name = "measurement_unit", nullable = false, length = 50)
	private String measurementUnit;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 30)
	private TargetStatus status = TargetStatus.NOT_STARTED;

	@Column(name = "status_change_reason", length = 500)
	private String statusChangeReason;

	public CampaignTargetEntity(Long id) {
		this.id = id;
	}
}
