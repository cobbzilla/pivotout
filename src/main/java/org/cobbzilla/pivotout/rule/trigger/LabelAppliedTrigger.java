package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.model.Activity;
import org.cobbzilla.pivotout.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trigger fires when a particular label is applied to a story
 */
public class LabelAppliedTrigger extends LabelTriggerBase {

    private static final Logger LOG = LoggerFactory.getLogger(LabelAppliedTrigger.class);

    @Override
    public boolean occurred(Story story) {
        final String matchLabel = getMatchLabel();
        final boolean currentHasLabel = story.getCurrentActivity().getStory().getHasLabel(matchLabel);

        if (currentHasLabel) {
            // Was this label just added *now*, or was it added before?
            for (Activity activity : story.getRelatedPastActivities()) {
                if (activity.getStory().getHasLabels()) {
                    return !activity.getStory().getHasLabel(matchLabel);
                }
            }
        }

        return currentHasLabel;
    }

}
