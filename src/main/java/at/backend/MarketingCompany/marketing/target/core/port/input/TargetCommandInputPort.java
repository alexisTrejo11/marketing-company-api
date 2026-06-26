package at.backend.MarketingCompany.marketing.target.core.port.input;

import at.backend.MarketingCompany.marketing.target.core.application.command.*;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;

public interface TargetCommandInputPort {

	CampaignTarget createTarget(CreateTargetCommand command);

	CampaignTarget updateTarget(UpdateTargetCommand command);

	CampaignTarget updateTargetProgress(UpdateTargetProgressCommand command);

	CampaignTarget changeTargetStatus(ChangeTargetStatusCommand command);

	CampaignTarget recalculateTarget(RecalculateTargetCommand command);

	void deleteTarget(DeleteTargetCommand command);
}
