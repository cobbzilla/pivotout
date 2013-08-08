package org.cobbzilla.pivotout.rule.action;

import org.cobbzilla.pivotout.rule.RuleAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public abstract class RuleActionBase implements RuleAction {

    private static final Logger LOG = LoggerFactory.getLogger(RuleActionBase.class);

    protected Properties config;

    @Override
    public void configure(Properties props) {
        this.config = props;
    }

    public Properties getConfiguration () { return config; }

}

