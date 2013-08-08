package org.cobbzilla.pivotout.rule.trigger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class UnassignedTriggerTest extends TriggerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(UnassignedTriggerTest.class);

    // start the job
    private static final String START_JOB_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249702607</id>\n" +
            "  <version type=\"integer\">452</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 19:42:01 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;webhook test 3&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36430933</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36430933</url>\n" +
            "      <current_state>started</current_state>\n" +
            "      <owned_by>Jonathan Cobb</owned_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n"
            ;

    // abandon ownership
    private static final String ABANDON_OWNER_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249703093</id>\n" +
            "  <version type=\"integer\">453</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 19:42:57 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;webhook test 3&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36430933</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36430933</url>\n" +
            "      <owned_by></owned_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    // another random change just to sanity check, add a comment
    private static final String ADD_COMMENT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249703301</id>\n" +
            "  <version type=\"integer\">454</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 19:43:26 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;webhook test 3&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36430933</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36430933</url>\n" +
            "      <description>hrm, why was this abandoned</description>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    // re-add the owner
    private static final String START_AGAIN_WITH_OWNER_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249782573</id>\n" +
            "  <version type=\"integer\">455</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 22:39:41 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;webhook test 3&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36430933</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36430933</url>\n" +
            "      <owned_by>Jonathan Cobb</owned_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    // change title, but doesn't change ownership status
    private static final String CHANGE_TITLE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">249782841</id>\n" +
            "  <version type=\"integer\">456</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/09/21 22:40:35 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;webhook test 3 - changed title&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">36430933</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/36430933</url>\n" +
            "      <name>webhook test 3 - changed title</name>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    @Test
    public void testUnassignedTrigger () throws Exception {

        final MockAction action = addRule(new UnassignedTrigger());

        // make sure it actually works -- start a job and then abandon it
        process(START_JOB_XML);
        assertFalse(action.performCalled);

        process(ABANDON_OWNER_XML);
        assertTrue(action.performCalled);
        action.performCalled = false; // reset

        // next 3 are to make sure we don't get any false positives
        process(ADD_COMMENT_XML);
        assertFalse(action.performCalled);

        process(START_AGAIN_WITH_OWNER_XML);
        assertFalse(action.performCalled);

        process(CHANGE_TITLE_XML);
        assertFalse(action.performCalled);
    }

}
