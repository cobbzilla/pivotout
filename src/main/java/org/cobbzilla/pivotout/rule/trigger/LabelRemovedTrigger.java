package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabelRemovedTrigger extends LabelTriggerBase  {

    private static final Logger LOG = LoggerFactory.getLogger(LabelRemovedTrigger.class);

    @Override
    public boolean occurred(Story story) {

        final String matchLabel = getMatchLabel();

        final Story currentStory = story.getCurrentActivity().getStory();
        if (!currentStory.getHasLabels()) return false; // no change to labels, so it couldn't have been removed.

        final boolean currentHasLabel = currentStory.getHasLabel(matchLabel);
        if (currentHasLabel) return false; // current still has label, no need to check previous activity

        // If we don't have the label now, but we did on a previous activity, return true
        return story.findPreviousLabelSet().contains(matchLabel);
    }

}
