package org.cobbzilla.pivotout.service;

import org.cobbzilla.pivotout.model.Activity;
import org.cobbzilla.pivotout.model.ActivityEventType;
import org.cobbzilla.pivotout.model.Story;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class EventProcessingServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(EventProcessingServiceTest.class);

    private Random random = new Random();

    private EventProcessingService eventProcessingService = new EventProcessingService();

    @Test
    public void testEventUnassigned () throws Exception {

        // A brand activity with a brand new story
        Story story = new Story();
        story.setId(1);
        story.setCurrentState("");

        Activity activity = new Activity();
        activity.setAuthor("author");
        activity.setEventType(ActivityEventType.story_update.name());
        activity.setStory(story);

        eventProcessingService.processEvent(activity);
    }

}
