package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.pivotout.model.StoryState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Trigger fires when an owner owns more than a certain number of stories
 */
public class OwnerHasTooManyStoriesTrigger extends RuleTriggerBase {

    public static final String PROP_STORY_LIMIT = "storyLimit";

    private static final Logger LOG = LoggerFactory.getLogger(OwnerHasTooManyStoriesTrigger.class);

    public int getStoryLimit () { return Integer.parseInt(config.getProperty(PROP_STORY_LIMIT)); }

    @Override
    public boolean occurred(Story story) {
        if (story.getCurrentActivity().getStory().getHasOwner()) {
            // how many stories does this owner already have in the project?
            int numOwned = countStoriesByOwner(story.getOwnedBy(), story.getProjectId());
            LOG.info("occurred ("+story+"): found "+numOwned+" stories owned by "+story.getOwnedBy()+" in project "+story.getProjectId());
            return numOwned >= getStoryLimit();
        }
        return false;
    }

    private int countStoriesByOwner(String ownedBy, String projectId) {
        try {
            final List<Story> stories = pivotal.findStoriesByOwnerAndProject(ownedBy, projectId);
            if (stories == null) return 0;

            int size = 0;
            for (Story story : stories) {
                if (StoryState.isActive(story.getMostRecentState())) {
                    size++;
                }
            }
            LOG.info("countStoriesByOwner: found "+size+" active stories for owner="+ownedBy+" in project="+projectId);
            return size;

        } catch (IOException e) {
            throw new IllegalStateException("findStoriesByOwnerAndProject: "+e, e);
        }
    }
}
