package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.model.Activity;
import org.cobbzilla.pivotout.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trigger fires when a story has its owner removed
 */
public class UnassignedTrigger extends RuleTriggerBase {

    private static final Logger LOG = LoggerFactory.getLogger(UnassignedTrigger.class);

    @Override
    public boolean occurred(Story story) {

        Activity currentActivity = story.getCurrentActivity();
        if (currentActivity == null) return false; // should never happen

        return currentActivity.getStory().getWasOwnerRemoved();
    }

}
