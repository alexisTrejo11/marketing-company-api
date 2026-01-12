package at.backend.MarketingCompany.marketing.target.core.ports.input;

import at.backend.MarketingCompany.marketing.target.core.application.command.CreateTargetCommand;
import at.backend.MarketingCompany.marketing.target.core.application.command.DeleteTargetCommand;
import at.backend.MarketingCompany.marketing.target.core.application.command.TargetStatusChangeCommand;
import at.backend.MarketingCompany.marketing.target.core.application.command.TargetUpdateCommand;
import at.backend.MarketingCompany.marketing.target.core.application.command.UpdateTargetProgressCommand;
import at.backend.MarketingCompany.marketing.target.core.domain.entity.CampaignTarget;
import at.backend.MarketingCompany.marketing.target.core.domain.valueobject.CampaignTargetId;

public interface TargetCommandInputPort {

  CampaignTarget createTarget(CreateTargetCommand command);

  CampaignTarget updateTarget(TargetUpdateCommand command);

  CampaignTarget changeTargetStatus(TargetStatusChangeCommand command);

  CampaignTarget updateTargetProgress(UpdateTargetProgressCommand command);

  CampaignTarget recalculateTarget(CampaignTargetId targetId);

  void deleteTarget(DeleteTargetCommand command);
}
