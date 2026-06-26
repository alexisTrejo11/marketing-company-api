package at.backend.MarketingCompany.marketing.target.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MetricType;
import at.backend.MarketingCompany.marketing.target.adapter.output.persitence.model.CampaignTargetEntity;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.TargetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignTargetJpaRepository extends JpaRepository<CampaignTargetEntity, Long> {

	@Query("SELECT t FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL AND t.id = :id")
	Optional<CampaignTargetEntity> findByIdAndNotDeleted(@Param("id") Long id);

	@Query("SELECT t FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL")
	Page<CampaignTargetEntity> findAllNotDeleted(Pageable pageable);

	@Query("SELECT t FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL AND t.campaign.id = :campaignId")
	Page<CampaignTargetEntity> findByCampaignId(@Param("campaignId") Long campaignId, Pageable pageable);

	@Query("SELECT t FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL AND t.campaign.id = :campaignId")
	java.util.List<CampaignTargetEntity> findAllByCampaignId(@Param("campaignId") Long campaignId);

	@Query("SELECT t FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL "
			+ "AND t.campaign.id = :campaignId AND t.status = :achievedStatus")
	Page<CampaignTargetEntity> findAchievedByCampaignId(
			@Param("campaignId") Long campaignId,
			@Param("achievedStatus") TargetStatus achievedStatus,
			Pageable pageable);

	@Query("SELECT t FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL "
			+ "AND t.campaign.id = :campaignId AND t.status <> :achievedStatus")
	Page<CampaignTargetEntity> findUnachievedByCampaignId(
			@Param("campaignId") Long campaignId,
			@Param("achievedStatus") TargetStatus achievedStatus,
			Pageable pageable);

	@Query("SELECT t FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL "
			+ "AND t.metricType = :metricType AND t.status <> :achievedStatus")
	Page<CampaignTargetEntity> findUnachievedByMetricType(
			@Param("metricType") MetricType metricType,
			@Param("achievedStatus") TargetStatus achievedStatus,
			Pageable pageable);

	@Query("SELECT t FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL "
			+ "AND t.campaign.id = :campaignId AND LOWER(t.metricName) = LOWER(:metricName)")
	Optional<CampaignTargetEntity> findByCampaignIdAndMetricName(
			@Param("campaignId") Long campaignId,
			@Param("metricName") String metricName);

	@Query("SELECT COUNT(t) > 0 FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL "
			+ "AND t.campaign.id = :campaignId AND LOWER(t.metricName) = LOWER(:metricName)")
	boolean existsByCampaignIdAndMetricName(
			@Param("campaignId") Long campaignId,
			@Param("metricName") String metricName);

	@Query("SELECT COUNT(t) FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL AND t.campaign.id = :campaignId")
	long countByCampaignId(@Param("campaignId") Long campaignId);

	@Query("SELECT COUNT(t) FROM CampaignTargetEntity t WHERE t.deletedAt IS NULL "
			+ "AND t.campaign.id = :campaignId AND t.status = :achievedStatus")
	long countAchievedByCampaignId(
			@Param("campaignId") Long campaignId,
			@Param("achievedStatus") TargetStatus achievedStatus);
}
