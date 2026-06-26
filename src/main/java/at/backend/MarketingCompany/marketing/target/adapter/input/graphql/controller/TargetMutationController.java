package at.backend.MarketingCompany.marketing.target.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.target.adapter.input.graphql.mapper.TargetOutputMapper;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.port.input.TargetCommandInputPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TargetMutationController {

	private final TargetCommandInputPort commandPort;
	private final TargetOutputMapper mapper;

	@MutationMapping
	public TargetOutput createTarget(@Argument @Valid @NotNull CreateTargetInput input) {
		CampaignTarget target = commandPort.createTarget(input.toCommand());
		return mapper.toOutput(target);
	}

	@MutationMapping
	public TargetOutput updateTarget(@Argument @Valid @NotNull UpdateTargetInput input) {
		CampaignTarget target = commandPort.updateTarget(input.toCommand());
		return mapper.toOutput(target);
	}

	@MutationMapping
	public TargetOutput updateTargetProgress(@Argument @Valid @NotNull UpdateTargetProgressInput input) {
		CampaignTarget target = commandPort.updateTargetProgress(input.toCommand());
		return mapper.toOutput(target);
	}

	@MutationMapping
	public TargetOutput changeTargetStatus(@Argument @Valid @NotNull TargetStatusChangeInput input) {
		CampaignTarget target = commandPort.changeTargetStatus(input.toCommand());
		return mapper.toOutput(target);
	}

	@MutationMapping
	public TargetOutput recalculateTarget(@Argument @Valid @NotNull RecalculateTargetInput input) {
		CampaignTarget target = commandPort.recalculateTarget(input.toCommand());
		return mapper.toOutput(target);
	}

	@MutationMapping
	public Boolean deleteTarget(@Argument @Valid @NotNull DeleteTargetInput input) {
		commandPort.deleteTarget(input.toCommand());
		return true;
	}
}
