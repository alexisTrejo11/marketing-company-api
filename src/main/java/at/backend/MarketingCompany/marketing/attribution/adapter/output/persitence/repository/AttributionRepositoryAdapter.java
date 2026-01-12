package at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.repository;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.mapper.AttributionEntityMapper;
import at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.model.CampaignAttributionEntity;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;
import at.backend.MarketingCompany.marketing.attribution.core.port.output.AttributionRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AttributionRepositoryAdapter implements AttributionRepositoryPort {
  private final CampaignAttributionJpaRepository jpaRepository;
  private final AttributionEntityMapper mapper;

  @Override
  @Transactional
  public CampaignAttribution save(CampaignAttribution attribution) {
    CampaignAttributionEntity entity = mapper.toEntity(attribution);
    CampaignAttributionEntity savedEntity = jpaRepository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CampaignAttribution> findById(CampaignAttributionId id) {
    return jpaRepository.findByIdAndNotDeleted(id.getValue())
        .map(mapper::toDomain);
  }

  @Override
  @Transactional
  public void delete(CampaignAttributionId id) {
    jpaRepository.deleteById(id.getValue());
  }

  @Override
  public Page<CampaignAttribution> findTopAttributedCampaigns(Pageable pageable) {
    return null;
  }

  @Override
  public Page<CampaignAttribution> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public Page<CampaignAttribution> findByFilters(DealId dealId, MarketingCampaignId campaignId,
      List<AttributionModel> attributionModel, BigDecimal minAttributionPercentage, BigDecimal maxAttributionPercentage,
      BigDecimal minAttributedRevenue, BigDecimal maxAttributedRevenue, Pageable pageable) {
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignAttribution> findByDealId(DealId dealId, Pageable pageable) {
    return jpaRepository.findByDealId(dealId.getValue(), pageable)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignAttribution> findByCampaignId(MarketingCampaignId campaignId, Pageable pageable) {
    return jpaRepository.findByCampaignId(campaignId.getValue(), pageable)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CampaignAttribution> findByDealIdAndCampaignId(DealId dealId, MarketingCampaignId campaignId) {
    return jpaRepository.findByDealIdAndCampaignId(dealId.getValue(), campaignId.getValue())
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignAttribution> findByAttributionModel(
      AttributionModel attributionModel,
      Pageable pageable) {
    return jpaRepository.findByAttributionModel(attributionModel, pageable)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal calculateTotalAttributedRevenueByCampaignId(MarketingCampaignId campaignId) {
    return jpaRepository.calculateTotalAttributedRevenueByCampaignId(campaignId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal calculateAverageAttributionPercentageByCampaignId(MarketingCampaignId campaignId) {
    return jpaRepository.calculateAverageAttributionPercentageByCampaignId(campaignId.getValue());
  }

  @Override
  @Transactional(readOnly = true)
  public long countUniqueDealsByCampaignId(MarketingCampaignId campaignId) {
    return jpaRepository.countUniqueDealsByCampaignId(campaignId.getValue());
  }

  @Override
  public Long countByCampaignId(MarketingCampaignId campaignId) {
    return 0L;
  }

  @Override
  public Long calculateTotalTouchpointsByCampaignId(MarketingCampaignId campaignId) {
    return 0L;
  }

  @Override
  public Map<Integer, Long> getTouchpointDistributionByCampaignId(MarketingCampaignId campaignId) {
    return Map.of();
  }

  @Override
  public Map<String, Long> countByAttributionModelByCampaignId(MarketingCampaignId campaignId) {
    return Map.of();
  }

  @Override
  public List<BigDecimal> getAllAttributedRevenuesByCampaignId(MarketingCampaignId campaignId) {
    return List.of();
  }

  @Override
  public Map<String, BigDecimal> calculateRevenueByModelByCampaignId(MarketingCampaignId campaignId) {
    return Map.of();
  }

  @Override
  public boolean existsByDealIdAndCampaignId(DealId dealId, MarketingCampaignId campaignId) {
    return jpaRepository.findByDealIdAndCampaignId(dealId.getValue(), campaignId.getValue()).isPresent();
  }
}
