package org.cobbzilla.pivotout.service;

import org.cobbzilla.pivotout.model.Activity;
import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.pivotout.rule.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventProcessingService {

    private static final Logger LOG = LoggerFactory.getLogger(EventProcessingService.class);

    private List<Rule> rules = new ArrayList<>();
    public List<Rule> getRules () { return rules; }
    public void addRule (Rule rule) {
        rules.add(rule);
    }

    public void processEvent (Activity activity) {

        for (Story story : activity.getStories()) {
            story = findStory(story.getId());
            if (story == null) {
                story = addNewStory(activity);
            }
            story.addRelatedActivity(activity);

            if (activity.getActivityEventType().isDelete()) {
                LOG.warn("processEvent("+activity+"): story deleted, bailing out");
                storyMap.remove(story.getId());
                continue;
            }

            for (Rule rule : rules) {
                try {
                    if (rule.getProjectMatches(activity.getProjectId()) && rule.getTrigger().occurred(story)) {
                        rule.performAction(story);
                    }
                } catch (Exception e) {
                    LOG.error("Error evaluating rule: "+e, e);
                }
            }
        }
    }

    public Story addNewStory(Activity activity) {
        final Story story = activity.getStory();
        addNewStory(story);
        return story;
    }

    public Story addNewStory(Story story) {
        return storyMap.put(story.getId(), story);
    }

    public void initializeStory (Story story) {
        Activity activity = new Activity();
        activity.setStory(story);
        story.addRelatedActivity(activity);
        addNewStory(story);
    }

    private Map<Long, Story> storyMap = new HashMap<>();
    private Story findStory(long id) {
        return storyMap.get(id);
    }
}
