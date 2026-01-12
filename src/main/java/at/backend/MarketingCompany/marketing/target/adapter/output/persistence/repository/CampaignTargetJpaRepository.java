package at.backend.MarketingCompany.marketing.target.adapter.output.persistence.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.backend.MarketingCompany.marketing.target.adapter.output.persistence.model.CampaignTargetEntity;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.MetricType;

@Repository
public interface CampaignTargetJpaRepository extends
    JpaRepository<CampaignTargetEntity, Long>,
    JpaSpecificationExecutor<CampaignTargetEntity> {

  // Métodos básicos
  Optional<CampaignTargetEntity> findByIdAndDeletedAtIsNull(Long id);

  @Query("SELECT t FROM CampaignTargetEntity t WHERE t.campaign.id = :campaignId AND t.deletedAt IS NULL")
  Page<CampaignTargetEntity> findByCampaignId(@Param("campaignId") Long campaignId, Pageable pageable);

  @Query("SELECT t FROM CampaignTargetEntity t WHERE t.campaign.id = :campaignId AND t.metricName = :metricName AND t.deletedAt IS NULL")
  Optional<CampaignTargetEntity> findByCampaignIdAndMetricName(
      @Param("campaignId") Long campaignId,
      @Param("metricName") String metricName);

  // Métodos por estado
  @Query("SELECT t FROM CampaignTargetEntity t WHERE t.campaign.id = :campaignId AND ((:isAchieved = true AND t.status = 'ACHIEVED') OR (:isAchieved = false AND t.status <> 'ACHIEVED')) AND t.deletedAt IS NULL")
  Page<CampaignTargetEntity> findByCampaignIdAndIsAchieved(
      @Param("campaignId") Long campaignId,
      @Param("isAchieved") boolean isAchieved,
      Pageable pageable);

  @Query("SELECT t FROM CampaignTargetEntity t WHERE t.metricType = :metricType AND t.status <> 'ACHIEVED' AND t.deletedAt IS NULL")
  Page<CampaignTargetEntity> findByMetricTypeAndNotAchieved(
      @Param("metricType") MetricType metricType,
      Pageable pageable);

  // Métodos de verificación
  @Query("SELECT COUNT(t) > 0 FROM CampaignTargetEntity t WHERE t.campaign.id = :campaignId AND t.metricName = :metricName AND t.deletedAt IS NULL")
  boolean existsByCampaignIdAndMetricName(
      @Param("campaignId") Long campaignId,
      @Param("metricName") String metricName);

  @Query("SELECT COUNT(t) > 0 FROM CampaignTargetEntity t WHERE t.campaign.id = :campaignId AND t.deletedAt IS NULL")
  boolean existsByCampaignId(@Param("campaignId") Long campaignId);

  // Métodos estadísticos
  @Query("SELECT COUNT(t) FROM CampaignTargetEntity t WHERE t.campaign.id = :campaignId AND t.status = 'ACHIEVED' AND t.deletedAt IS NULL")
  Long countAchievedByCampaignId(@Param("campaignId") Long campaignId);

  @Query("SELECT AVG(t.currentValue * 100.0 / t.targetValue) FROM CampaignTargetEntity t WHERE t.campaign.id = :campaignId AND t.deletedAt IS NULL")
  Optional<BigDecimal> calculateAverageAchievementPercentage(@Param("campaignId") Long campaignId);

  @Query("SELECT COALESCE(SUM(CASE WHEN t.status = 'ACHIEVED' THEN 1 ELSE 0 END) * 100.0 / COUNT(t), 0) FROM CampaignTargetEntity t WHERE t.campaign.id = :campaignId AND t.deletedAt IS NULL")
  BigDecimal calculateAchievementRate(@Param("campaignId") Long campaignId);

  // Métodos de búsqueda
  @Query("SELECT t FROM CampaignTargetEntity t WHERE " +
      "t.campaign.id = :campaignId AND " +
      "(:metricName IS NULL OR LOWER(t.metricName) LIKE LOWER(CONCAT('%', :metricName, '%'))) AND " +
      "t.deletedAt IS NULL")
  Page<CampaignTargetEntity> searchByCampaignIdAndMetricName(
      @Param("campaignId") Long campaignId,
      @Param("metricName") String metricName,
      Pageable pageable);

  // Métodos para obtener múltiples
  @Query("SELECT t FROM CampaignTargetEntity t WHERE t.id IN :ids AND t.deletedAt IS NULL")
  List<CampaignTargetEntity> findAllByIdIn(@Param("ids") List<Long> ids);
}
