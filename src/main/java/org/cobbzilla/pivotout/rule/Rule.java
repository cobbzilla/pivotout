package org.cobbzilla.pivotout.rule;

import org.cobbzilla.pivotout.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rule {

    private static final Logger LOG = LoggerFactory.getLogger(Rule.class);

    private String projectId;
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    private RuleTrigger trigger;
    public RuleTrigger getTrigger() { return trigger; }
    public void setTrigger(RuleTrigger trigger) { this.trigger = trigger; }

    private RuleAction action;
    public RuleAction getAction() { return action; }
    public void setAction(RuleAction action) { this.action = action; }

    public void performAction(Story story) { action.perform(story); }

    /** @return true if this Rule has no projectId (matches all projects), OR if it has a projectId that matches the parameter */
    public boolean getProjectMatches(String projectId) {
        return this.projectId == null || (projectId != null && this.projectId.equals(projectId));
    }
}
