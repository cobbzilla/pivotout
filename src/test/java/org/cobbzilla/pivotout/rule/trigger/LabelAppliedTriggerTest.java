package org.cobbzilla.pivotout.rule.trigger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class LabelAppliedTriggerTest extends TriggerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(LabelAppliedTriggerTest.class);

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
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    public static final String APPLY_LABEL_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249800777</id>\n" +
            "  <version type=\"integer\">458</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 23:44:49 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;a new story to test LabelAppliedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36492027</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36492027</url>\n" +
            "      <labels>beastly,gnarly</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    private static final String REMOVE_NONMATCHING_LABEL_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249801869</id>\n" +
            "  <version type=\"integer\">459</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 23:49:10 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;a new story to test LabelAppliedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36492027</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36492027</url>\n" +
            "      <labels>gnarly</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    private static final String REMOVE_MATCHING_LABEL_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249802441</id>\n" +
            "  <version type=\"integer\">460</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 23:51:13 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;a new story to test LabelAppliedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36492027</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36492027</url>\n" +
            "      <labels></labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    private static final String READD_MATCHING_LABEL_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249802529</id>\n" +
            "  <version type=\"integer\">461</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 23:51:36 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;a new story to test LabelAppliedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36492027</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36492027</url>\n" +
            "      <labels>gnarly</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    private static final String ADD_COMMENT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249802811</id>\n" +
            "  <version type=\"integer\">462</version>\n" +
            "  <event_type>note_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 23:53:09 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added comment: &quot;adding a comment&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36492027</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36492027</url>\n" +
            "      <notes type=\"array\">\n" +
            "        <note>\n" +
            "          <id type=\"integer\">29245715</id>\n" +
            "          <text>adding a comment</text>\n" +
            "        </note>\n" +
            "      </notes>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    private static final String READD_NONMATCHING_LABEL_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249804939</id>\n" +
            "  <version type=\"integer\">463</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/22 00:01:50 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;a new story to test LabelAppliedTrigger&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36492027</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36492027</url>\n" +
            "      <labels>beastly,gnarly</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    public static final String MATCH_LABEL = "gnarly";


    @Test
    public void testLabelAppliedTrigger () throws Exception {

        final LabelAppliedTrigger trigger = new LabelAppliedTrigger();

        Properties props = new Properties();
        props.setProperty(LabelAppliedTrigger.PROP_MATCH_LABEL, MATCH_LABEL);
        trigger.configure(apiService, props);

        final TriggerTestBase.MockAction action = addRule(trigger);

        process(NEW_STORY_XML);
        assertFalse(action.performCalled);

        // this applies 2 labels, one of which matches
        process(APPLY_LABEL_XML);
        assertTrue(action.performCalled);
        action.performCalled = false; // reset

        // remove the non-matching label -- trigger should NOT get called
        process(REMOVE_NONMATCHING_LABEL_XML);
        assertFalse(action.performCalled);

        // remove the matching label -- trigger should NOT get called
        process(REMOVE_MATCHING_LABEL_XML);
        assertFalse(action.performCalled);

        // re-add the matching label -- NOW, trigger should get called
        process(READD_MATCHING_LABEL_XML);
        assertTrue(action.performCalled);
        action.performCalled = false; // reset

        // add a note
        process(ADD_COMMENT_XML);
        assertFalse(action.performCalled);

        // add an unrelated label
        process(READD_NONMATCHING_LABEL_XML);
        assertFalse(action.performCalled);
    }

}
