package at.backend.MarketingCompany.marketing.target.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.adapter.output.persitence.mapper.TargetEntityMapper;
import at.backend.MarketingCompany.marketing.target.adapter.output.persitence.model.CampaignTargetEntity;
import at.backend.MarketingCompany.marketing.target.core.application.query.TargetQuery;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.TargetStatus;
import at.backend.MarketingCompany.marketing.target.core.port.output.TargetOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TargetOutputAdapter implements TargetOutputPort {

	private final CampaignTargetJpaRepository jpaRepository;
	private final TargetEntityMapper mapper;

	@Override
	@Transactional
	public CampaignTarget save(CampaignTarget target) {
		CampaignTargetEntity entity = mapper.toEntity(target);
		CampaignTargetEntity savedEntity = jpaRepository.save(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	public Optional<CampaignTarget> findById(CampaignTargetId id) {
		return jpaRepository.findByIdAndNotDeleted(id.getValue())
				.map(mapper::toDomain);
	}

	@Override
	@Transactional
	public void hardDelete(CampaignTargetId id) {
		jpaRepository.deleteById(id.getValue());
	}

	@Override
	public Page<CampaignTarget> findAll(Pageable pageable) {
		return jpaRepository.findAllNotDeleted(pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignTarget> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
		return jpaRepository.findByCampaignId(campaignId.getValue(), pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignTarget> findByCampaignIdAndAchieved(MarketingCampaignId campaignId, boolean achieved, Pageable pageable) {
		if (achieved) {
			return jpaRepository.findAchievedByCampaignId(campaignId.getValue(), TargetStatus.ACHIEVED, pageable)
					.map(mapper::toDomain);
		}
		return jpaRepository.findUnachievedByCampaignId(campaignId.getValue(), TargetStatus.ACHIEVED, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<CampaignTarget> findByMetricTypeAndNotAchieved(MetricType metricType, Pageable pageable) {
		return jpaRepository.findUnachievedByMetricType(metricType, TargetStatus.ACHIEVED, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Optional<CampaignTarget> findByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName) {
		return jpaRepository.findByCampaignIdAndMetricName(campaignId.getValue(), metricName)
				.map(mapper::toDomain);
	}

	@Override
	public List<CampaignTarget> findAllByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.findAllByCampaignId(campaignId.getValue()).stream()
				.map(mapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Page<CampaignTarget> findByFilters(TargetQuery query, Pageable pageable) {
		Page<CampaignTargetEntity> page;
		if (query.campaignId() != null) {
			page = jpaRepository.findByCampaignId(query.campaignId(), pageable);
		} else {
			page = jpaRepository.findAllNotDeleted(pageable);
		}

		List<CampaignTarget> filtered = page.stream()
				.filter(entity -> matchesFilters(entity, query))
				.map(mapper::toDomain)
				.collect(Collectors.toList());

		return new PageImpl<>(filtered, pageable, page.getTotalElements());
	}

	@Override
	public long countByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countByCampaignId(campaignId.getValue());
	}

	@Override
	public long countAchievedByCampaignId(MarketingCampaignId campaignId) {
		return jpaRepository.countAchievedByCampaignId(campaignId.getValue(), TargetStatus.ACHIEVED);
	}

	@Override
	public boolean existsByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName) {
		return jpaRepository.existsByCampaignIdAndMetricName(campaignId.getValue(), metricName);
	}

	@Override
	public BigDecimal calculateAverageAchievementByCampaignId(MarketingCampaignId campaignId) {
		List<CampaignTargetEntity> entities = jpaRepository.findAllByCampaignId(campaignId.getValue());
		if (entities.isEmpty()) {
			return BigDecimal.ZERO;
		}

		BigDecimal sum = BigDecimal.ZERO;
		for (CampaignTargetEntity entity : entities) {
			BigDecimal percent = BigDecimal.ZERO;
			if (entity.getTargetValue() != null
					&& entity.getTargetValue().compareTo(BigDecimal.ZERO) != 0
					&& entity.getCurrentValue() != null) {
				percent = entity.getCurrentValue()
						.divide(entity.getTargetValue(), 6, RoundingMode.HALF_UP)
						.multiply(BigDecimal.valueOf(100));
			}
			sum = sum.add(percent);
		}

		return sum.divide(BigDecimal.valueOf(entities.size()), 4, RoundingMode.HALF_UP);
	}

	private boolean matchesFilters(CampaignTargetEntity entity, TargetQuery query) {
		if (query.metricTypes() != null && !query.metricTypes().isEmpty()) {
			if (entity.getMetricType() == null || !query.metricTypes().contains(entity.getMetricType())) {
				return false;
			}
		}

		boolean achieved = entity.getStatus() == TargetStatus.ACHIEVED
				|| (entity.getCurrentValue() != null
				&& entity.getTargetValue() != null
				&& entity.getCurrentValue().compareTo(entity.getTargetValue()) >= 0);

		if (query.isAchieved() != null && !Objects.equals(achieved, query.isAchieved())) {
			return false;
		}

		if (query.minTargetValue() != null) {
			if (entity.getTargetValue() == null || entity.getTargetValue().compareTo(query.minTargetValue()) < 0) {
				return false;
			}
		}
		if (query.maxTargetValue() != null) {
			if (entity.getTargetValue() == null || entity.getTargetValue().compareTo(query.maxTargetValue()) > 0) {
				return false;
			}
		}

		if (query.minCurrentValue() != null) {
			if (entity.getCurrentValue() == null || entity.getCurrentValue().compareTo(query.minCurrentValue()) < 0) {
				return false;
			}
		}
		if (query.maxCurrentValue() != null) {
			if (entity.getCurrentValue() == null || entity.getCurrentValue().compareTo(query.maxCurrentValue()) > 0) {
				return false;
			}
		}

		BigDecimal achievementPercentage = calculateAchievementPercentage(entity);
		if (query.minAchievementPercentage() != null
				&& achievementPercentage.compareTo(query.minAchievementPercentage()) < 0) {
			return false;
		}
		if (query.maxAchievementPercentage() != null
				&& achievementPercentage.compareTo(query.maxAchievementPercentage()) > 0) {
			return false;
		}

		if (query.searchTerm() != null && !query.searchTerm().isBlank()) {
			String term = query.searchTerm().toLowerCase();
			return entity.getMetricName() != null && entity.getMetricName().toLowerCase().contains(term);
		}

		return true;
	}

	private BigDecimal calculateAchievementPercentage(CampaignTargetEntity entity) {
		if (entity.getTargetValue() == null || entity.getTargetValue().compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal current = entity.getCurrentValue() != null ? entity.getCurrentValue() : BigDecimal.ZERO;
		return current.divide(entity.getTargetValue(), 4, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100));
	}
}
