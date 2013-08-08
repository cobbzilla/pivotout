package org.cobbzilla.pivotout.rule.action;

import org.apache.commons.mail.EmailException;
import org.cobbzilla.pivotout.model.Story;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;

public class EmailActionTest {

    private static final Logger LOG = LoggerFactory.getLogger(EmailActionTest.class);

    private static final String TEST_STORY_NAME = "a sample story for testing";
    private static final String TEST_STORY_URL = "https://www.pivotaltracker.com/story/show/"+System.currentTimeMillis();
    private static final String TEST_FROM = "max.cavalera@example.com";
    private static final String TEST_FROM_NAME = "Max Cavalera";
    private static final String TEST_TO = "igor.cavalera@example.com";
    private static final String TEST_SUBJECT_TEMPLATE = "story name is: ${story.mostRecentName}";
    private static final String TEST_MESSAGE_TEMPLATE  = "story URL is: ${story.storyUrl}";

    /** Primarily for testing the velocity substitution stuff. */
    @Test
    public void testEmail () throws Exception {
        Properties props = new Properties();
        props.setProperty(EmailAction.PROP_FROM_EMAIL, TEST_FROM);
        props.setProperty(EmailAction.PROP_FROM_NAME, TEST_FROM_NAME);
        props.setProperty(EmailAction.PROP_TO_EMAIL, TEST_TO);
        props.setProperty(EmailAction.PROP_SUBJECT_TEMPLATE, TEST_SUBJECT_TEMPLATE);
        props.setProperty(EmailAction.PROP_MESSAGE_TEMPLATE, TEST_MESSAGE_TEMPLATE);
        final String[] subject = new String[1];
        final String[] message = new String[1];
        final InternetAddress[] from = new InternetAddress[1];
        EmailAction action = new EmailAction() {
            @Override
            protected void sendMessage(EmailActionMessage email) throws EmailException {
                subject[0] = email.getSubject();
                message[0] = email.getMessage();
                from[0] = email.getFromAddress();
            }
        };
        action.configure(props);
        action.perform(new Story() {
            @Override public String getName() { return TEST_STORY_NAME; }
            @Override public String getStoryUrl() { return TEST_STORY_URL; }
        });
        assertEquals(TEST_SUBJECT_TEMPLATE.replace("${story.mostRecentName}", TEST_STORY_NAME), subject[0]);
        assertEquals(TEST_MESSAGE_TEMPLATE.replace("${story.storyUrl}", TEST_STORY_URL), message[0]);
        assertEquals(TEST_FROM, from[0].getAddress());
        assertEquals(TEST_FROM_NAME, from[0].getPersonal());
    }

}
