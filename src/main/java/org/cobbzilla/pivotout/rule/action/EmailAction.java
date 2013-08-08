package org.cobbzilla.pivotout.rule.action;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.cobbzilla.pivotout.model.Story;
import org.cobbzilla.util.MD5Util;
import org.cobbzilla.util.VelocityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class EmailAction extends RuleActionBase {

    public static final String PROP_SMTP_HOST = "smtpHost";
    private static final String PROP_SMTP_PORT = "smtpPort";
    public static final String PROP_SMTP_USER = "smtpUser";
    public static final String PROP_SMTP_PASSWORD = "smtpPassword";
    public static final String PROP_TO_EMAIL = "toEmail";
    public static final String PROP_FROM_EMAIL = "fromEmail";
    public static final String PROP_FROM_NAME = "fromName";
    public static final String PROP_SUBJECT_TEMPLATE = "subjectTemplate";
    public static final String PROP_MESSAGE_TEMPLATE = "messageTemplate";
    public static final String PROP_USE_TLS = "useTLS";
    public static final String MAILPROP_PREFIX = "mail.";

    private static final Logger LOG = LoggerFactory.getLogger(EmailAction.class);

    private Map<String, Long> sendThrottler = new HashMap<>();
    private static final long THROTTLE_LIMIT = 1000 * 60 * 60 * 24; // 1 day

    @Override
    public Object perform(Story story) {
        EmailActionMessage email = new EmailActionMessage();
        try {
            final String smtpUser = config.getProperty(PROP_SMTP_USER);
            final String smtpPassword = config.getProperty(PROP_SMTP_PASSWORD);
            final String smtpPortString = config.getProperty(PROP_SMTP_PORT);

            email.setHostName(config.getProperty(PROP_SMTP_HOST));
            if (smtpPortString != null) email.setSmtpPort(Integer.parseInt(smtpPortString));
            if (smtpUser != null) email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPassword));
            email.setDebug(true);
            email.setTLS(Boolean.parseBoolean(config.getProperty(PROP_USE_TLS)));

            Enumeration<String> propNames = (Enumeration<String>) config.propertyNames();
            while (propNames.hasMoreElements()) {
                String propName = propNames.nextElement();
                if (propName.startsWith(MAILPROP_PREFIX)) {
                    email.getMailSession().getProperties().put(propName, config.getProperty(propName));
                }
            }

            String fromName = config.getProperty(PROP_FROM_NAME);
            if (fromName == null) {
                email.setFrom(config.getProperty(PROP_FROM_EMAIL));
            } else {
                email.setFrom(config.getProperty(PROP_FROM_EMAIL), fromName);
            }
            email.setSubject(VelocityUtil.merge(story, config.getProperty(PROP_SUBJECT_TEMPLATE)));
            email.setMsg(VelocityUtil.merge(story, config.getProperty(PROP_MESSAGE_TEMPLATE)));
            email.addTo(config.getProperty(PROP_TO_EMAIL));

            sendMessage(email);
            return email;

        } catch (EmailException e) {
            final String message = "Error sending email: " + e;
            LOG.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }

    protected void sendMessage(EmailActionMessage email) throws EmailException {

        String hash = MD5Util.md5hex(email.getSubject() + "\n" + email.getMessage());
        Long lastSent = sendThrottler.get(hash);
        final long now = System.currentTimeMillis();

        if (lastSent == null || now - lastSent > THROTTLE_LIMIT) {
            LOG.info("Sending email: "+email);
            email.send();
            sendThrottler.put(hash, now);

        } else {
            LOG.warn("NOT sending email, we've sent the same one very recently: "+email);
        }
    }

    protected static class EmailActionMessage extends SimpleEmail {

        private String internalMessage;

        public String toString () {
            return "{SimpleEmail hostname: "+getHostName()+" from="+getFromAddress()+" subject="+getSubject()+"}";
        }

        @Override
        public Email setMsg(String msg) throws EmailException {
            this.internalMessage = msg;
            return super.setMsg(msg);
        }

        public String getMessage () { return internalMessage; }
    }
}
