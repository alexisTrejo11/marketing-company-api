package at.backend.MarketingCompany.marketing.target.adapter.output.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.target.adapter.output.persistence.mapper.CampaignTargetEntityMapper;
import at.backend.MarketingCompany.marketing.target.adapter.output.persistence.model.CampaignTargetEntity;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;
import at.backend.MarketingCompany.marketing.target.core.ports.output.TargetRepositoryPort;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CampaignTargetRepositoryAdapter implements TargetRepositoryPort {

  private final CampaignTargetJpaRepository jpaRepository;
  private final CampaignTargetEntityMapper mapper;

  @Override
  @Transactional
  public CampaignTarget save(CampaignTarget target) {
    CampaignTargetEntity entity = mapper.toEntity(target);
    CampaignTargetEntity savedEntity = jpaRepository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CampaignTarget> findById(CampaignTargetId targetId) {
    return jpaRepository.findByIdAndDeletedAtIsNull(targetId.getValue())
        .map(mapper::toDomain);
  }

  @Override
  @Transactional
  public void delete(CampaignTargetId targetId) {
    jpaRepository.findByIdAndDeletedAtIsNull(targetId.getValue())
        .ifPresent(entity -> {
          entity.setDeletedAt(LocalDateTime.now());
          jpaRepository.save(entity);
        });
  }

  @Override
  @Transactional
  public void hardDelete(CampaignTargetId targetId) {
    jpaRepository.deleteById(targetId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignTarget> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
    return jpaRepository.findByCampaignId(campaignId.getValue(), pageable)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignTarget> findByCampaignIdAndIsAchieved(MarketingCampaignId campaignId, boolean isAchieved,
      Pageable pageable) {
    return jpaRepository.findByCampaignIdAndIsAchieved(campaignId.getValue(), isAchieved, pageable)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignTarget> findByMetricTypeAndNotAchieved(MetricType metricType, Pageable pageable) {
    return jpaRepository.findByMetricTypeAndNotAchieved(metricType, pageable)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CampaignTarget> findByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName) {
    return jpaRepository.findByCampaignIdAndMetricName(campaignId.getValue(), metricName)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByCampaignIdAndMetricName(MarketingCampaignId campaignId, String metricName) {
    return jpaRepository.existsByCampaignIdAndMetricName(campaignId.getValue(), metricName);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByCampaignId(MarketingCampaignId campaignId) {
    return jpaRepository.existsByCampaignId(campaignId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public long countAchievedByCampaignId(MarketingCampaignId campaignId) {
    return jpaRepository.countAchievedByCampaignId(campaignId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<BigDecimal> getAverageAchievementPercentage(MarketingCampaignId campaignId) {
    return jpaRepository.calculateAverageAchievementPercentage(campaignId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getAchievementRate(MarketingCampaignId campaignId) {
    return jpaRepository.calculateAchievementRate(campaignId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CampaignTarget> findAllByIds(List<CampaignTargetId> targetIds) {
    List<Long> ids = targetIds.stream()
        .map(CampaignTargetId::getValue)
        .collect(Collectors.toList());

    return jpaRepository.findAllByIdIn(ids).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }
}
