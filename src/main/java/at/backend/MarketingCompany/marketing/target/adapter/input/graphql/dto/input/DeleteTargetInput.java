package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeleteTargetInput(
    @NotNull @Positive Long targetId, @NotNull Boolean hardDelete) {

}
