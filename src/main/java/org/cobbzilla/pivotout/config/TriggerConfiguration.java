package org.cobbzilla.pivotout.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.cobbzilla.pivotout.rule.RuleTrigger;
import org.cobbzilla.pivotout.rule.trigger.*;
import org.cobbzilla.pivotout.service.PivotalApiService;
import org.cobbzilla.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerConfiguration.class);

    @XStreamAlias("type")
    @XStreamAsAttribute
    private String triggerType;
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }

    @XStreamAlias("config")
    private String triggerConfig;
    public String getTriggerConfig() { return triggerConfig; }
    public void setTriggerConfig(String triggerConfig) { this.triggerConfig = triggerConfig; }

    public RuleTrigger buildTrigger(PivotalApiService apiService) {
        RuleTrigger trigger;
        switch (triggerType) {
            case "unassigned":
                trigger = new UnassignedTrigger();
                break;
            case "label-applied":
                trigger = new LabelAppliedTrigger();
                break;
            case "label-removed":
                trigger = new LabelRemovedTrigger();
                break;
            case "label-required":
                trigger = new LabelRequiredTrigger();
                break;
            case "owner-has-too-many-stories":
                trigger = new OwnerHasTooManyStoriesTrigger();
                break;
            case "story-created-without-required-label":
                trigger = new LabelMissingTrigger();
                break;
            default:
                throw new IllegalArgumentException("unrecognized trigger type: "+triggerType);
        }
        trigger.configure(apiService, PropertiesUtil.propsFromString(triggerConfig));
        return trigger;
    }

}
