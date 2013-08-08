package org.cobbzilla.pivotout.config;

import org.cobbzilla.pivotout.rule.action.EmailAction;
import org.cobbzilla.pivotout.rule.trigger.LabelAppliedTrigger;
import org.cobbzilla.pivotout.rule.trigger.UnassignedTrigger;
import org.cobbzilla.pivotout.service.EventProcessingService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PivotoutConfigurationTest {

    private static final Logger LOG = LoggerFactory.getLogger(PivotoutConfigurationTest.class);

    private PivotoutConfiguration buildConfig(String xml) {
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
        return PivotoutConfiguration.buildConfig(in);
    }

    public static final String TEST_FROM_EMAIL = "from@example.com";
    public static final String TEST_TO_EMAIL = "to@example.com";
    public static final String TEST_HOSTNAME = "127.0.0.1";
    public static final String TEST_MATCHLABEL = "gnarly";

    private static final String TEST_CONFIG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<pivotout-config>\n" +
            "<rules>\n" +
            "  <rule>\n" +
            "    <trigger type='unassigned'>\n" +
            "        <![CDATA[\n" +
            "        no configuration here, but shouldn't crash things to put random stuff in\n" +
            "        ]]>\n" +
            "    </trigger>\n" +
            "    <action type='email'>\n" +
            "        smtpHost=" + TEST_HOSTNAME +  "\n" +
            "        fromEmail=" + TEST_FROM_EMAIL +  "\n" +
            "        toEmail=" + TEST_TO_EMAIL +  "\n" +
            "        subjectTemplate=Story ${story.id} was abandoned by ${story.mostRecentOwner}: ${story.mostRecentName}\n" +
            "        messageTemplate=Story URL: ${story.mostRecentUrl}\n" +
            "    </action>\n" +
            "  </rule>\n" +
            "  <rule>\n" +
            "    <trigger type='label-applied'>\n" +
            "      <config>\n" +
            "        matchLabel=" + TEST_MATCHLABEL + "\n" +
            "      </config>\n" +
            "    </trigger>\n" +
            "    <action type='email'>\n" +
            "        smtpHost=" + TEST_HOSTNAME +  "\n" +
            "        fromEmail=" + TEST_FROM_EMAIL +  "\n" +
            "        toEmail=" + TEST_TO_EMAIL +  "\n" +
            "        subjectTemplate=Story ${story.id} was labeled '"+TEST_MATCHLABEL+"' by ${story.mostRecentOwner}: ${story.mostRecentName}\n" +
            "        messageTemplate=Story URL: ${story.mostRecentUrl}\n" +
            "    </action>\n" +
            "  </rule>\n" +
            "</rules>\n" +
            "</pivotout-config>";

    @Test
    public void testConfig () throws Exception {
        PivotoutConfiguration configuration = buildConfig(TEST_CONFIG);
        assertEquals(2, configuration.getRuleConfigurations().size());
        EventProcessingService service = new EventProcessingService();
        configuration.configure(service);
        assertEquals(2, service.getRules().size());

        final UnassignedTrigger unassignedTrigger = (UnassignedTrigger) service.getRules().get(0).getTrigger();
        EmailAction emailAction = (EmailAction) service.getRules().get(0).getAction();
        assertEquals(TEST_FROM_EMAIL, emailAction.getConfiguration().getProperty(EmailAction.PROP_FROM_EMAIL));
        assertEquals(TEST_TO_EMAIL, emailAction.getConfiguration().getProperty(EmailAction.PROP_TO_EMAIL));
        assertEquals(TEST_HOSTNAME, emailAction.getConfiguration().getProperty(EmailAction.PROP_SMTP_HOST));

        final LabelAppliedTrigger labelAppliedTrigger = (LabelAppliedTrigger) service.getRules().get(1).getTrigger();
        assertEquals(TEST_MATCHLABEL, labelAppliedTrigger.getMatchLabel());
        emailAction = (EmailAction) service.getRules().get(1).getAction();
        assertEquals(TEST_FROM_EMAIL, emailAction.getConfiguration().getProperty(EmailAction.PROP_FROM_EMAIL));
        assertEquals(TEST_TO_EMAIL, emailAction.getConfiguration().getProperty(EmailAction.PROP_TO_EMAIL));
        assertEquals(TEST_HOSTNAME, emailAction.getConfiguration().getProperty(EmailAction.PROP_SMTP_HOST));
    }

    public static final String TEST_DEFAULTS_FROM_EMAIL_PROP = "ci@cobbzilla.com";
    public static final String TEST_DEFAULTS_TO_EMAIL = "pivotal-critical@cobbzilla.com";
    public static final String TEST_DEFAULTS_TEMPLATE_CONFIG_NAME = "cobbzilla-gmail";
    public static final String TEST_DEFAULTS_FROM_EMAIL = "fromEmail";

    public static final String TEST_CONFIG_WITH_DEFAULTS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<pivotout-config projects=\"608057, 481513\" token=\"85866ff33e5e1d65dbc9e5a1945a8ea5\"> <!-- cobbzilla-api's auth token -->\n" +
            "\n" +
            "    <config name=\""+TEST_DEFAULTS_TEMPLATE_CONFIG_NAME+"\">\n" +
            "        mail.smtps.socketFactory.port=587\n" +
            "        mail.smtps.socketFactory.fallback=false\n" +
            "        mail.smtp.starttls.enable=true\n" +
            "        "+EmailAction.PROP_FROM_EMAIL+"="+ TEST_DEFAULTS_FROM_EMAIL_PROP +"\n" +
            "    </config>\n" +
            "\n" +
            "    <rules>\n" +
            "\n" +
            "        <!-- someone tagged something severity_critical -->\n" +
            "        <rule>\n" +
            "            <trigger type='label-applied'>\n" +
            "                <config>matchLabel=severity_critical</config>\n" +
            "            </trigger>\n" +
            "            <action type='email' config=\""+TEST_DEFAULTS_TEMPLATE_CONFIG_NAME+"\">\n" +
            "                "+EmailAction.PROP_TO_EMAIL+"="+TEST_DEFAULTS_TO_EMAIL+"\n" +
            "                subjectTemplate=Story ${story.id} was labeled 'severity_critical' by ${story.mostRecentOwner}: ${story.mostRecentName}\n" +
            "                messageTemplate=Story URL: ${story.storyUrl}\n" +
            "            </action>\n" +
            "        </rule>\n" +
            "\n" +
            "    </rules>\n" +
            "</pivotout-config>";

    @Test
    public void testConfigWithDefaults () throws Exception {
        PivotoutConfiguration configuration = buildConfig(TEST_CONFIG_WITH_DEFAULTS);
        assertEquals(1, configuration.getTemplateConfigurationMap().size());
        assertNotNull(configuration.getTemplateConfigurationMap().get(TEST_DEFAULTS_TEMPLATE_CONFIG_NAME));
        assertEquals(TEST_DEFAULTS_FROM_EMAIL_PROP, configuration.getTemplateConfigurationMap().get(TEST_DEFAULTS_TEMPLATE_CONFIG_NAME).getProperties().getProperty(EmailAction.PROP_FROM_EMAIL));

        assertEquals(1, configuration.getRuleConfigurations().size());
        EventProcessingService service = new EventProcessingService();
        configuration.configure(service);
        assertEquals(1, service.getRules().size());

        final LabelAppliedTrigger labelAppliedTrigger = (LabelAppliedTrigger) service.getRules().get(0).getTrigger();
        EmailAction emailAction = (EmailAction) service.getRules().get(0).getAction();
        assertEquals(TEST_DEFAULTS_FROM_EMAIL_PROP, emailAction.getConfiguration().getProperty(EmailAction.PROP_FROM_EMAIL));
        assertEquals(TEST_DEFAULTS_TO_EMAIL, emailAction.getConfiguration().getProperty(EmailAction.PROP_TO_EMAIL));
    }
}
