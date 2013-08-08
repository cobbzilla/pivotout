package org.cobbzilla.pivotout.model;

import org.cobbzilla.pivotout.service.ModelBuilderService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

public class ModelBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelBuilderTest.class);

    // example POST borrowed from https://www.pivotaltracker.com/help/integrations#activity_web_hook
    public static final String TEST_POST
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "    <activity>\n" +
            "      <id type=\"integer\">1031</id>\n" +
            "      <version type=\"integer\">175</version>\n" +
            "      <event_type>story_update</event_type>\n" +
            "      <occurred_at type=\"datetime\">2009/12/14 14:12:09 PST</occurred_at>\n" +
            "      <author>James Kirk</author>\n" +
            "      <project_id type=\"integer\">26</project_id>\n" +
            "      <description>James Kirk accepted &quot;More power to shields&quot;</description>\n" +
            "      <stories>\n" +
            "        <story>\n" +
            "          <id type=\"integer\">109</id>\n" +
            "          <url>https:///projects/26/stories/109</url>\n" +
            "          <accepted_at type=\"datetime\">2009/12/14 22:12:09 UTC</accepted_at>\n" +
            "          <current_state>accepted</current_state>\n" +
            "        </story>\n" +
            "      </stories>\n" +
            "    </activity>";

    @Test
    public void testBasicActivityModel () throws Exception {

        ModelBuilderService builderService = new ModelBuilderService();
        InputStream in = new ByteArrayInputStream(TEST_POST.getBytes());
        Activity activity = builderService.buildActivity(in);

        assertEquals("wrong id", 1031L, activity.getId());
        assertEquals("wrong event type", "story_update", activity.getEventType());
        assertEquals("wrong ActivityEventType", ActivityEventType.story_update, activity.getActivityEventType());
        assertEquals("wrong author", "James Kirk", activity.getAuthor());
        assertEquals("wrong occurred_at", "2009/12/14 14:12:09 PST", activity.getOccurredAt());
        assertEquals("wrong project", "26", activity.getProjectId());
        assertEquals("wrong description", "James Kirk accepted \"More power to shields\"", activity.getDescription());
        assertEquals("wrong number of stories", 1, activity.getStories().size());
        assertEquals("wrong story id", 109L, activity.getStory().getId());
        assertEquals("wrong story url", "https:///projects/26/stories/109", activity.getStory().getUrl());
        assertEquals("wrong story accepted_at", "2009/12/14 22:12:09 UTC", activity.getStory().getAcceptedAt());
        assertEquals("wrong story current_state", "accepted", activity.getStory().getCurrentState());
    }

    public static final String TEST_REJECT_WITH_REASON = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249685701</id>\n" +
            "  <version type=\"integer\">441</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 19:07:00 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb rejected &quot;webhook test 3&quot; with comments: &quot;aaaazzzz&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36430933</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36430933</url>\n" +
            "      <current_state>rejected</current_state>\n" +
            "      <notes type=\"array\">\n" +
            "        <note>\n" +
            "          <id type=\"integer\">29230573</id>\n" +
            "          <text>aaaazzzz</text>\n" +
            "        </note>\n" +
            "      </notes>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    @Test
    public void testRejectWithReason () throws Exception {

        ModelBuilderService builderService = new ModelBuilderService();
        InputStream in = new ByteArrayInputStream(TEST_REJECT_WITH_REASON.getBytes());
        Activity activity = builderService.buildActivity(in);

        assertEquals("wrong id", 249685701L, activity.getId());
        assertEquals("wrong reject reason", "aaaazzzz", activity.getStory().getNotes().get(0).getText());
    }
}