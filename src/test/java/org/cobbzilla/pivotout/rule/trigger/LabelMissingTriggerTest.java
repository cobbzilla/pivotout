package org.cobbzilla.pivotout.rule.trigger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class LabelMissingTriggerTest extends TriggerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(LabelMissingTriggerTest.class);

    private static final String NEW_STORY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249799047</id>\n" +
            "  <version type=\"integer\">457</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 23:38:29 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;a new story to test LabelAppliedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36492027</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36492027</url>\n" +
            "      <name>a new story to test LabelAppliedTrigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "      <labels>required_label,other_label</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    private static final String NEW_STORY_MISSING_LABEL_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249799048</id>\n" +
            "  <version type=\"integer\">457</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 23:38:29 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;a new story to test LabelAppliedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36492028</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36492027</url>\n" +
            "      <name>a new story to test LabelAppliedTrigger</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    private static final String MATCH_LABEL = "must_have, or_must_have,required_label , or_this_one";
    private static final String MATCH_TYPE = "feature";

    @Test
    public void testLabelMissingTrigger () throws Exception {

        final LabelMissingTrigger trigger = new LabelMissingTrigger();

        Properties props = new Properties();
        props.setProperty(LabelMissingTrigger.PROP_MATCH_LABEL, MATCH_LABEL);
        props.setProperty(LabelMissingTrigger.PROP_MATCH_STORY_TYPE, MATCH_TYPE);
        trigger.configure(apiService, props);

        final TriggerTestBase.MockAction action = addRule(trigger);

        process(NEW_STORY_XML);
        assertFalse(action.performCalled);

        process(NEW_STORY_MISSING_LABEL_XML);
        assertTrue(action.performCalled);
    }

}
