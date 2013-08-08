package org.cobbzilla.pivotout.rule.action;

import org.cobbzilla.pivotout.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogAction extends RuleActionBase {

    private static final Logger LOG = LoggerFactory.getLogger(LogAction.class);

    private static final String PROP_PREFIX = "prefix";

    public String getPrefix () { return config.getProperty(PROP_PREFIX); }

    @Override
    public Object perform(Story story) {
        LOG.info(getPrefix()+": story="+story);
        return story;
    }
}
