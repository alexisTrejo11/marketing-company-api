package at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.CampaignType;
import at.backend.MarketingCompany.marketing.activity.adapter.output.persitence.model.CampaignActivityEntity;
import at.backend.MarketingCompany.marketing.asset.adapter.output.persitence.model.MarketingAssetEntity;
import at.backend.MarketingCompany.marketing.attribution.adapter.output.persitence.model.CampaignAttributionEntity;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model.MarketingChannelEntity;
import at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.model.CampaignInteractionEntity;
import at.backend.MarketingCompany.marketing.metric.adapter.output.persitence.model.CampaignMetricEntity;
import at.backend.MarketingCompany.marketing.target.adapter.output.persistence.model.CampaignTargetEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "marketing_campaigns", indexes = {
    @Index(name = "idx_campaigns_status_date", columnList = "status, start_date"),
    @Index(name = "idx_campaigns_type_budget", columnList = "campaign_type, total_budget"),
    @Index(name = "idx_campaigns_channel", columnList = "primary_channel_id")
})
public class MarketingCampaignEntity extends BaseJpaEntity {

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = 1000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "campaign_type", nullable = false, length = 50)
  private CampaignType campaignType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private CampaignStatus status;

  @Column(name = "total_budget", nullable = false, precision = 15, scale = 2)
  private BigDecimal totalBudget;

  @Column(name = "spent_amount", precision = 15, scale = 2)
  private BigDecimal spentAmount = BigDecimal.ZERO;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "budget_allocations", columnDefinition = "jsonb")
  private Map<String, Object> budgetAllocations;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "target_audience_demographics", columnDefinition = "jsonb")
  private Map<String, Object> targetAudienceDemographics;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "target_locations", columnDefinition = "jsonb")
  private Map<String, Object> targetLocations;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "target_interests", columnDefinition = "jsonb")
  private Map<String, Object> targetInterests;

  @Column(name = "primary_goal", length = 200)
  private String primaryGoal;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "secondary_goals", columnDefinition = "jsonb")
  private Map<String, Object> secondaryGoals;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "success_metrics", columnDefinition = "jsonb")
  private Map<String, Object> successMetrics;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "primary_channel_id")
  private MarketingChannelEntity primaryChannel;

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CampaignTargetEntity> targets = new HashSet<>();

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<MarketingAssetEntity> assets = new HashSet<>();

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CampaignActivityEntity> activities = new HashSet<>();

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CampaignInteractionEntity> interactions = new HashSet<>();

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CampaignMetricEntity> metrics = new HashSet<>();

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CampaignAttributionEntity> attributions = new HashSet<>();

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AbTestEntity> abTests = new HashSet<>();

  public MarketingCampaignEntity(Long id) {
    this.id = id;
  }
}
