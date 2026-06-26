package at.backend.MarketingCompany.marketing.target.core.domain.entity;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.TargetStatus;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Slf4j
public class CampaignTarget extends BaseDomainEntity<CampaignTargetId> {
	private MarketingCampaignId campaignId;
	private String metricName;
	private MetricType metricType;
	private BigDecimal targetValue;
	private BigDecimal currentValue;
	private String measurementUnit;
	private TargetStatus status;
	private String statusChangeReason;

	private CampaignTarget() {
		this.currentValue = BigDecimal.ZERO;
		this.status = TargetStatus.NOT_STARTED;
	}

	public CampaignTarget(CampaignTargetId id) {
		super(id);
		this.currentValue = BigDecimal.ZERO;
		this.status = TargetStatus.NOT_STARTED;
	}

	public static CampaignTarget create(CreateTargetParams params) {
		TargetValidator.validateForCreation(params);

		CampaignTarget target = new CampaignTarget(CampaignTargetId.generate());
		target.campaignId = params.campaignId();
		target.metricName = params.metricName();
		target.metricType = params.metricType();
		target.targetValue = params.targetValue();
		target.measurementUnit = params.measurementUnit();
		target.currentValue = BigDecimal.ZERO;
		target.status = TargetStatus.NOT_STARTED;

		log.debug("Created CampaignTarget: campaignId={}, metricName={}, type={}",
				target.campaignId != null ? target.campaignId.getValue() : null,
				target.metricName,
				target.metricType);

		return target;
	}

	public static CampaignTarget reconstruct(CampaignTargetReconstructParams params) {
		if (params == null) {
			return null;
		}

		CampaignTarget target = new CampaignTarget();
		target.id = params.id();
		target.campaignId = params.campaignId();
		target.metricName = params.metricName();
		target.metricType = params.metricType();
		target.targetValue = params.targetValue();
		target.currentValue = params.currentValue() != null ? params.currentValue() : BigDecimal.ZERO;
		target.measurementUnit = params.measurementUnit();
		target.status = params.status() != null ? params.status() : TargetStatus.NOT_STARTED;
		target.statusChangeReason = params.statusChangeReason();
		target.createdAt = params.createdAt();
		target.updatedAt = params.updatedAt();
		target.deletedAt = params.deletedAt();
		target.version = params.version();

		return target;
	}

	public void update(String metricName, MetricType metricType, BigDecimal targetValue, String measurementUnit) {
		TargetValidator.validateForUpdate(metricName, metricType != null ? metricType : this.metricType, targetValue, measurementUnit);

		if (metricName != null) {
			this.metricName = metricName;
		}
		if (metricType != null) {
			this.metricType = metricType;
		}
		if (targetValue != null) {
			this.targetValue = targetValue;
		}
		if (measurementUnit != null) {
			this.measurementUnit = measurementUnit;
		}

		recalculateStatus();
	}

	public void updateProgress(BigDecimal newCurrentValue) {
		TargetValidator.validateProgressUpdate(newCurrentValue);
		this.currentValue = newCurrentValue;
		recalculateStatus();
	}

	public void changeStatus(TargetStatus newStatus, String reason) {
		if (newStatus == null) {
			throw new IllegalArgumentException("Target status is required");
		}
		this.status = newStatus;
		this.statusChangeReason = reason;
	}

	public void recalculate() {
		recalculateStatus();
	}

	public boolean isAchieved() {
		return status == TargetStatus.ACHIEVED || currentValue.compareTo(targetValue) >= 0;
	}

	public BigDecimal achievementPercentage() {
		if (targetValue == null || targetValue.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal current = currentValue != null ? currentValue : BigDecimal.ZERO;
		try {
			return current.divide(targetValue, 4, RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(100));
		} catch (ArithmeticException ex) {
			log.warn("Arithmetic error while calculating achievement percentage for target id={}: {}",
					id != null ? id.getValue() : null, ex.getMessage());
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal remainingValue() {
		if (targetValue == null) {
			return BigDecimal.ZERO;
		}
		BigDecimal current = currentValue != null ? currentValue : BigDecimal.ZERO;
		BigDecimal remaining = targetValue.subtract(current);
		return remaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : remaining;
	}

	private void recalculateStatus() {
		if (status == TargetStatus.FAILED) {
			return;
		}

		if (isAchieved()) {
			this.status = TargetStatus.ACHIEVED;
			return;
		}

		BigDecimal current = currentValue != null ? currentValue : BigDecimal.ZERO;
		if (current.compareTo(BigDecimal.ZERO) > 0) {
			this.status = TargetStatus.IN_PROGRESS;
		} else {
			this.status = TargetStatus.NOT_STARTED;
		}
	}
}
