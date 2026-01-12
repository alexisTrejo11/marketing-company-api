package at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "opportunities")
public class OpportunityEntity extends BaseJpaEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_company_id", nullable = false)
  private CustomerCompanyEntity customerCompany;

  @Column(name = "title", nullable = false, length = 200)
  private String title;

  @Column(name = "amount", precision = 15, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "stage", nullable = false, length = 20)
  private OpportunityStage stage;

  @Column(name = "expected_close_date")
  private LocalDate expectedCloseDate;

  @Column(name = "loss_reason_value", length = 100)
  private String lossReasonValue;

  @Column(name = "loss_reason_details", length = 500)
  private String lossReasonDetails;

  @Column(name = "next_steps", length = 1000)
  private String nextSteps;

  @Column(name = "next_steps_due_date")
  private LocalDateTime nextStepsDueDate;

  @Column(name = "probability")
  private Integer probability;

  public OpportunityEntity(Long id) {
    this.setId(id);
  }

  /**
   * Get customer company ID without loading the entire entity
   */
  public Long getCustomerCompanyId() {
    return customerCompany != null ? customerCompany.getId() : null;
  }

}
