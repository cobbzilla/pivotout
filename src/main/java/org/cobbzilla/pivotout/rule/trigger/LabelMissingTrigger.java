package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.model.ActivityEventType;
import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.pivotout.model.StoryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LabelMissingTrigger extends LabelTriggerBase {

    private static final Logger LOG = LoggerFactory.getLogger(LabelMissingTrigger.class);

    public static final String PROP_MATCH_STORY_TYPE = "matchStoryType";

    @Override
    public boolean occurred(Story story) {

        // only match on story create (this could be configurable in the future)
        if (story.getCurrentActivity().getActivityEventType() != ActivityEventType.story_create) return false;

        // must match story type
        if (story.getType() != StoryType.valueOf(config.getProperty(PROP_MATCH_STORY_TYPE))) return false;

        List<String> labels = getMatchLabelArray();

        boolean foundLabel = false;
        for (String label : labels) {
            if (story.getHasLabel(label)) {
                foundLabel = true;
                break;
            }
        }

        return !foundLabel;
    }

}
