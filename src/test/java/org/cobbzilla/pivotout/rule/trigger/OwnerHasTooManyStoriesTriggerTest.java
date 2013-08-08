package org.cobbzilla.pivotout.rule.trigger;

import org.cobbzilla.pivotout.model.Activity;
import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.pivotout.service.ModelBuilderService;
import org.cobbzilla.pivotout.service.PivotalApiService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class OwnerHasTooManyStoriesTriggerTest extends TriggerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(OwnerHasTooManyStoriesTriggerTest.class);

    public static final String NEW_STORY_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">250609339</id>\n" +
            "  <version type=\"integer\">491</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 01:16:30 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;first story, should not trigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36600879</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36600879</url>\n" +
            "      <name>first story, should not trigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>started</current_state>\n" +
            "      <owned_by>Jonathan Cobb</owned_by>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>";

    public static final String ACCEPTED_STORY_2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">250609340</id>\n" +
            "  <version type=\"integer\">492</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 01:16:30 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;second story, should not trigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36600880</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36600880</url>\n" +
            "      <name>second story, should not trigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>accepted</current_state>\n" +
            "      <owned_by>Jonathan Cobb</owned_by>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>";

    public static final String NEW_STORY_3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">250609341</id>\n" +
            "  <version type=\"integer\">492</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 01:16:30 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;third story, should trigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36600881</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36600881</url>\n" +
            "      <name>third story, should trigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>started</current_state>\n" +
            "      <owned_by>Jonathan Cobb</owned_by>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>";

    public static final String NEW_STORY_4 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">250609342</id>\n" +
            "  <version type=\"integer\">492</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 01:16:30 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;4th story, should trigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36600882</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36600882</url>\n" +
            "      <name>4th story, should trigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <owned_by>Jonathan Cobb</owned_by>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>";

    public static final String OTHER_PROJECT_STORY_5 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">250609343</id>\n" +
            "  <version type=\"integer\">492</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 01:16:30 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">4815</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;5th story but on another project, should NOT trigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36600883</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36600883</url>\n" +
            "      <name>5th story but on another project, should NOT trigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <owned_by>Jonathan Cobb</owned_by>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>";

    public static final String OTHER_OWNER_STORY_6 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">250609344</id>\n" +
            "  <version type=\"integer\">492</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 01:16:30 UTC</occurred_at>\n" +
            "  <author>Someone</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Someone added &quot;6th story but with another owner, should NOT trigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36600884</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36600884</url>\n" +
            "      <name>6th story but with another owner, should NOT trigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <owned_by>Someone</owned_by>\n" +
            "      <requested_by>Someone</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>";

    @Test
    public void testTooManyStories () throws Exception {

        MockApi api = new MockApi();
        ModelBuilderService modelBuilderService = new ModelBuilderService();

        final OwnerHasTooManyStoriesTrigger trigger = new OwnerHasTooManyStoriesTrigger();
        Properties props = new Properties();
        props.setProperty(OwnerHasTooManyStoriesTrigger.PROP_STORY_LIMIT, String.valueOf(2));
        trigger.configure(api, props);
        final MockAction action = addRule(trigger);

        // first story will not cause the trigger to fire
        addStory(api, modelBuilderService, NEW_STORY_1);
        assertFalse(action.performCalled);

        // second story won't either, because it's in a non-active state ("accepted")
        addStory(api, modelBuilderService, ACCEPTED_STORY_2);
        assertFalse(action.performCalled);

        // this third story should trigger the action
        addStory(api, modelBuilderService, NEW_STORY_3);
        assertTrue(action.performCalled);
        action.performCalled = false;

        // and this 4th one too
        addStory(api, modelBuilderService, NEW_STORY_4);
        assertTrue(action.performCalled);
        action.performCalled = false;

        // but these next 2 should not, since they do not match owner+project
        addStory(api, modelBuilderService, OTHER_PROJECT_STORY_5);
        assertFalse(action.performCalled);

        addStory(api, modelBuilderService, OTHER_OWNER_STORY_6);
        assertFalse(action.performCalled);
    }

    private void addStory(MockApi api, ModelBuilderService modelBuilderService, String xml) {
        final Activity activity = modelBuilderService.buildActivity(xml);
        activity.getStory().addRelatedActivity(activity);
        api.addStory(activity.getStory());
        process(xml);
    }

    private static class MockApi extends PivotalApiService {

        private List<Story> stories = new ArrayList<Story>();

        public void addStory (Story story) { stories.add(story); }

        @Override
        public List<Story> findStoriesByOwnerAndProject(String ownedBy, String projectId) throws IOException {
            List<Story> matches = new ArrayList<>();
            for (Story story : stories) {
                if (story.getOwnedBy().equals(ownedBy) && story.getProjectId().equals(projectId)) {
                    matches.add(story);
                }
            }
            return matches;
        }
    }
}
