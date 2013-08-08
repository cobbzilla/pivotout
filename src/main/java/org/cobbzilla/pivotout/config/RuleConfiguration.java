package org.cobbzilla.pivotout.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.cobbzilla.pivotout.rule.Rule;
import org.cobbzilla.pivotout.rule.RuleAction;
import org.cobbzilla.pivotout.rule.RuleTrigger;
import org.cobbzilla.pivotout.service.PivotalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

@XStreamAlias("rule")
public class RuleConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RuleConfiguration.class);

    @XStreamAlias("projectId")
    @XStreamAsAttribute
    private String projectId;
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    @XStreamAlias("trigger")
    private TriggerConfiguration triggerConfiguration;
    public TriggerConfiguration getTriggerConfiguration() { return triggerConfiguration; }
    public void setTriggerConfiguration(TriggerConfiguration triggerConfiguration) { this.triggerConfiguration = triggerConfiguration; }

    @XStreamAlias("action")
    private ActionConfiguration actionConfiguration;
    public ActionConfiguration getActionConfiguration() { return actionConfiguration; }
    public void setActionConfiguration(ActionConfiguration actionConfiguration) { this.actionConfiguration = actionConfiguration; }

    public Rule buildRule(PivotalApiService apiService, Map<String, TemplateConfiguration> templateConfigurationMap) {

        final TemplateConfiguration templateConfiguration = templateConfigurationMap.get(actionConfiguration.getConfig());
        Properties defaultProperties = (templateConfiguration != null) ? templateConfiguration.getProperties() : null;

        RuleTrigger trigger = triggerConfiguration.buildTrigger(apiService);
        RuleAction action = actionConfiguration.buildAction(defaultProperties);

        Rule rule = new Rule();
        rule.setProjectId(projectId);
        rule.setTrigger(trigger);
        rule.setAction(action);
        return rule;
    }
}
