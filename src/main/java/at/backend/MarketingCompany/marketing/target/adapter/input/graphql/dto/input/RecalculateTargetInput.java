package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecalculateTargetInput(
    @NotNull @Positive Long targetId) {
}
