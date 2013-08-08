package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.rule.RuleTrigger;
import org.cobbzilla.pivotout.service.PivotalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public abstract class RuleTriggerBase implements RuleTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(RuleTriggerBase.class);

    protected PivotalApiService pivotal;
    protected Properties config;

    @Override
    public void configure(PivotalApiService pivotal, Properties props) {
        this.pivotal = pivotal;
        this.config = props;
    }

}
