package org.cobbzilla.pivotout.rule;

import org.cobbzilla.pivotout.model.Story;

import java.util.Properties;

public interface RuleAction {

    public void configure (Properties props);

    public Object perform(Story story);

}
