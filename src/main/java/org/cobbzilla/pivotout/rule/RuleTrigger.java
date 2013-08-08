package org.cobbzilla.pivotout.rule;

import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.pivotout.service.PivotalApiService;

import java.util.Properties;

public interface RuleTrigger {

    public void configure(PivotalApiService pivotal, Properties props);

    public boolean occurred (Story story);

}
