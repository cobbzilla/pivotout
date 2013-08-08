package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.model.Activity;
import org.cobbzilla.pivotout.model.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class LabelRequiredTrigger extends LabelTriggerBase {

    public static final String PROP_REQUIRE_LABEL = "requireLabel";

    private static final Logger LOG = LoggerFactory.getLogger(LabelRequiredTrigger.class);

    private Pattern pattern;

    @Override
    public boolean occurred(Story story) {

        Pattern pattern = getPattern();
        final boolean currentHasLabel = story.getCurrentActivity().getStory().getHasLabelMatch(pattern);

        if (currentHasLabel) {

            // Does it most recently have a required label?
            for (String label : splitLabelList(PROP_REQUIRE_LABEL)) {
                if (story.getCurrentActivity().getStory().getHasLabel(label)) return false;
            }

            // Was it previously also missing all required labels? If so, don't send multiple alerts
            final Activity previousActivity = story.getPreviousActivity();
            if (previousActivity != null) {
                boolean missingAll = true;
                final Story previous = previousActivity.getStory();
                if (previous.getHasLabels() && previous.getLabelsList().size() == 0) return true; // it was deleted?
                for (String label : splitLabelList(PROP_REQUIRE_LABEL)) {
                    if (previous.getHasLabel(label)) {
                        missingAll = false; break;
                    }
                }
                if (missingAll) return false; // no multiple alerts
            }

            return true; // it matched the label and didn't have any of the other required labels
        }
        return false;
    }

    private Pattern getPattern() {
        if (pattern == null) {
            pattern = Pattern.compile(getMatchLabel());
        }
        return pattern;
    }
}
