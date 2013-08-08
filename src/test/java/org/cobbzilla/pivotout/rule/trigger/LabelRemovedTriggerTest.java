package org.cobbzilla.pivotout.rule.trigger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class LabelRemovedTriggerTest extends TriggerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(LabelRemovedTriggerTest.class);

    public static final String MATCH_LABEL = "beastly";

    public static final String NEW_STORY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">251083503</id>\n" +
            "  <version type=\"integer\">537</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 19:41:16 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;test story for LabelRemovedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36659437</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36659437</url>\n" +
            "      <name>test story for LabelRemovedTrigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <labels>beastly</labels>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    public static final String REMOVE_LABEL_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">251083817</id>\n" +
            "  <version type=\"integer\">538</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 19:41:47 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test story for LabelRemovedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36659437</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36659437</url>\n" +
            "      <labels></labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    public static  final String UPDATE_STORY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">251084213</id>\n" +
            "  <version type=\"integer\">539</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/25 19:42:34 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test story for LabelRemovedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36659437</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36659437</url>\n" +
            "      <description>a random update. should not cause LabelRemovedTrigger to fire</description>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    @Test
    public void testLabelRemoved () throws Exception {

        final LabelRemovedTrigger trigger = new LabelRemovedTrigger();

        Properties props = new Properties();
        props.setProperty(LabelRemovedTrigger.PROP_MATCH_LABEL, MATCH_LABEL);
        trigger.configure(apiService, props);

        final TriggerTestBase.MockAction action = addRule(trigger);

        // this applies the label
        process(NEW_STORY_XML);
        assertFalse(action.performCalled);

        // remove the label, should trigger
        process(REMOVE_LABEL_XML);
        assertTrue(action.performCalled);
        action.performCalled = false; // reset

        // make another update, should NOT trigger
        process(UPDATE_STORY_XML);
        assertFalse(action.performCalled);
    }

    @Test
    public void testLabelRemovedAfterUpdate () throws Exception {

        final LabelRemovedTrigger trigger = new LabelRemovedTrigger();

        Properties props = new Properties();
        props.setProperty(LabelRemovedTrigger.PROP_MATCH_LABEL, MATCH_LABEL);
        trigger.configure(apiService, props);

        final TriggerTestBase.MockAction action = addRule(trigger);

        // initialize a story with the label
        process(NEW_STORY_XML);
        assertFalse(action.performCalled);

        // make a change that doesn't include label stuff, should NOT trigger
        process(UPDATE_STORY_XML);
        assertFalse(action.performCalled);

        // make another update, remove the label, should now trigger.
        process(REMOVE_LABEL_XML);
        assertTrue(action.performCalled);
        action.performCalled = false; // reset
    }

}
