package org.cobbzilla.pivotout.rule.trigger;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class LabelRequiredTriggerTest extends TriggerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(LabelRequiredTriggerTest.class);

    private static final String MATCH_LABEL = ".+_maintenance";
    private static final String REQUIRE_LABEL = "endup, fuse";

    // first case
    private static final String TEST1_NEW_STORY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257521349</id>\n" +
            "  <version type=\"integer\">547</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:09:03 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;test story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484703</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484703</url>\n" +
            "      <name>test story</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>";
    private static final String TEST1_ADD_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257521577</id>\n" +
            "  <version type=\"integer\">548</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:09:49 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484703</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484703</url>\n" +
            "      <labels>endup</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>";
    private static final String TEST1_ADD_MAINTENANCE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257522275</id>\n" +
            "  <version type=\"integer\">549</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:13:30 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484703</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484703</url>\n" +
            "      <labels>dada_maintenance,endup</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST1_REMOVE_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257522337</id>\n" +
            "  <version type=\"integer\">550</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:13:53 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484703</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484703</url>\n" +
            "      <labels>dada_maintenance</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST1_RANDOM_LABEL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257522435</id>\n" +
            "  <version type=\"integer\">551</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:14:23 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484703</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484703</url>\n" +
            "      <labels>dada_maintenance,gnarly</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST1_READD_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257522499</id>\n" +
            "  <version type=\"integer\">552</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:14:48 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484703</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484703</url>\n" +
            "      <labels>dada_maintenance,endup,gnarly</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST1_REMOVE_MAINTENANCE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257522579</id>\n" +
            "  <version type=\"integer\">553</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:15:10 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484703</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484703</url>\n" +
            "      <labels>endup,gnarly</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    // second case
    private static final String TEST2_NEW_STORY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257523017</id>\n" +
            "  <version type=\"integer\">555</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:17:12 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;test2 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484823</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484823</url>\n" +
            "      <name>test2 story</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST2_ADD_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257523105</id>\n" +
            "  <version type=\"integer\">556</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:17:37 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test2 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484823</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484823</url>\n" +
            "      <labels>endup</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST2_REMOVE_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257523593</id>\n" +
            "  <version type=\"integer\">557</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:19:58 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test2 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484823</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484823</url>\n" +
            "      <labels></labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST2_ADD_MAINTENANCE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257524569</id>\n" +
            "  <version type=\"integer\">558</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:25:51 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test2 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484823</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484823</url>\n" +
            "      <labels>dada_maintenance</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST2_READD_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257524627</id>\n" +
            "  <version type=\"integer\">559</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:26:13 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test2 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484823</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484823</url>\n" +
            "      <labels>dada_maintenance,endup</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST2_REMOVE_MAINTENANCE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257524719</id>\n" +
            "  <version type=\"integer\">560</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:26:45 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test2 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484823</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484823</url>\n" +
            "      <labels>endup</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST2_REREMOVE_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257524767</id>\n" +
            "  <version type=\"integer\">561</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:27:03 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test2 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484823</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484823</url>\n" +
            "      <labels></labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST2_READD_MAINTENANCE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257524819</id>\n" +
            "  <version type=\"integer\">562</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:27:20 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test2 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37484823</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37484823</url>\n" +
            "      <labels>dada_maintenance</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";

    // third case
    private static final String TEST3_NEW_STORY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257524993</id>\n" +
            "  <version type=\"integer\">563</version>\n" +
            "  <event_type>story_create</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:28:11 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb added &quot;test3 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37485051</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37485051</url>\n" +
            "      <name>test3 story</name>\n" +
            "      <story_type>feature</story_type>\n" +
            "      <labels>dada_maintenance</labels>\n" +
            "      <current_state>unscheduled</current_state>\n" +
            "      <requested_by>Jonathan Cobb</requested_by>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST3_ADD_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257525111</id>\n" +
            "  <version type=\"integer\">564</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:28:52 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test3 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37485051</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37485051</url>\n" +
            "      <labels>dada_maintenance,endup</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST3_REMOVE_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257525185</id>\n" +
            "  <version type=\"integer\">565</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:29:20 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test3 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37485051</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37485051</url>\n" +
            "      <labels>dada_maintenance</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST3_READD_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257525273</id>\n" +
            "  <version type=\"integer\">566</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:29:45 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test3 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37485051</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37485051</url>\n" +
            "      <labels>dada_maintenance,endup</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST3_REMOVE_MAINTENANCE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257525349</id>\n" +
            "  <version type=\"integer\">567</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:30:08 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test3 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37485051</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37485051</url>\n" +
            "      <labels>endup</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST3_REREMOVE_EPIC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257525415</id>\n" +
            "  <version type=\"integer\">568</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:30:31 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test3 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37485051</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37485051</url>\n" +
            "      <labels></labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";
    private static final String TEST3_READD_MAINTENANCE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<activity>\n" +
            "  <id type=\"integer\">257525547</id>\n" +
            "  <version type=\"integer\">569</version>\n" +
            "  <event_type>story_update</event_type>\n" +
            "  <occurred_at type=\"datetime\">2012/10/10 06:31:02 UTC</occurred_at>\n" +
            "  <author>Jonathan Cobb</author>\n" +
            "  <project_id type=\"integer\">481513</project_id>\n" +
            "  <description>Jonathan Cobb edited &quot;test3 story&quot;</description>\n" +
            "  <stories type=\"array\">\n" +
            "    <story>\n" +
            "      <id type=\"integer\">37485051</id>\n" +
            "      <url>http://www.pivotaltracker.com/services/v3/projects/481513/stories/37485051</url>\n" +
            "      <labels>dada_maintenance</labels>\n" +
            "    </story>\n" +
            "  </stories>\n" +
            "</activity>\n";


    @Before public void setUp() { action = initAction(); }

    private MockAction action;
    private MockAction initAction() {
        final LabelRequiredTrigger trigger = new LabelRequiredTrigger();

        Properties props = new Properties();
        props.setProperty(LabelRequiredTrigger.PROP_MATCH_LABEL, MATCH_LABEL);
        props.setProperty(LabelRequiredTrigger.PROP_REQUIRE_LABEL, REQUIRE_LABEL);
        trigger.configure(apiService, props);

        return addRule(trigger);
    }

    @Test
    public void testLabelRequiredTrigger () throws Exception {

        // a story is created -- no alert
        process(TEST1_NEW_STORY);
        assertFalse(action.performCalled);

        // the story has an epic label applied -- no alert
        process(TEST1_ADD_EPIC);
        assertFalse(action.performCalled);

        // the story has a maintenance label applied -- no alert
        process(TEST1_ADD_MAINTENANCE);
        assertFalse(action.performCalled);

        // the story has it's epic label removed -- should alert here
        process(TEST1_REMOVE_EPIC);
        assertTrue(action.performCalled);
        action.performCalled = false;

        // an unrelated tag is applied, it should not alert again
        process(TEST1_RANDOM_LABEL);
        assertFalse(action.performCalled);

        // the story has it's epic label re-added -- no alert here
        process(TEST1_READD_EPIC);
        assertFalse(action.performCalled);

        // the story has it's maintenance label removed -- no alert here
        process(TEST1_REMOVE_MAINTENANCE);
        assertFalse(action.performCalled);
    }

    // needs a better name
    @Test
    public void testAnotherLabelRequiredTrigger () throws Exception {

        // create a story -- no alert
        process(TEST2_NEW_STORY);
        assertFalse(action.performCalled);

        // add the epic tag -- no alert
        process(TEST2_ADD_EPIC);
        assertFalse(action.performCalled);

        // remove the epic tag -- no alert
        process(TEST2_REMOVE_EPIC);
        assertFalse(action.performCalled);

        // add the maintenance tag -- alert
        process(TEST2_ADD_MAINTENANCE);
        assertTrue(action.performCalled);
        action.performCalled = false;

        // re-add the epic tag -- no alert
        process(TEST2_READD_EPIC);
        assertFalse(action.performCalled);

        // remove the maintenance tag -- no alert
        process(TEST2_REMOVE_MAINTENANCE);
        assertFalse(action.performCalled);

        // remove the epic tag -- no alert
        process(TEST2_REREMOVE_EPIC);
        assertFalse(action.performCalled);

        // re-add the maintenance tag -- alert
        process(TEST2_READD_MAINTENANCE);
        assertTrue(action.performCalled);
        action.performCalled = false;
    }

    @Test
    public void testThirdStrategy () throws Exception {

        // create a story with the maintenance tag but no epic tag -- should alert
        process(TEST3_NEW_STORY);
        assertTrue(action.performCalled);
        action.performCalled = false;

        // add the epic tag -- no alert
        process(TEST3_ADD_EPIC);
        assertFalse(action.performCalled);

        // remove the epic tag -- alert
        process(TEST3_REMOVE_EPIC);
        assertTrue(action.performCalled);
        action.performCalled = false;

        // re-add the epic tag -- no alert
        process(TEST3_READD_EPIC);
        assertFalse(action.performCalled);

        // remove the maintenance tag -- no alert
        process(TEST3_REMOVE_MAINTENANCE);
        assertFalse(action.performCalled);

        // remove the epic tag -- no alert
        process(TEST3_REREMOVE_EPIC);
        assertFalse(action.performCalled);

        // re-add the maintenance tag -- alert
        process(TEST3_READD_MAINTENANCE);
        assertTrue(action.performCalled);
        action.performCalled = false;
    }

}
