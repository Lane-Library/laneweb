package edu.stanford.irt.laneweb.email;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class EMailSender {

    private static final String BINDING_MAP = "org.springframework.validation.BindingResult.map";

    // one hour
    private static final long COUNT_CHECK_INTERVAL = 1000L * 60L * 60L;

    private static final String EMAIL = "email";

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$",
            Pattern.CASE_INSENSITIVE);

    private static final int MAX_MAILS_PER_IP = 10;

    private static final String RECIPIENT = "recipient";

    private static final String REDIRECT = "redirect";

    private static final String SUBJECT = "subject";

    private static final String[] XCLUDED_FIELDS = new String[] { SUBJECT, RECIPIENT, EMAIL, BINDING_MAP, REDIRECT };

    private Set<String> excludedFields;

    private long lastUpdate = 0;

    private JavaMailSender mailSender;

    private Set<String> recipients;

    private String remoteIp;

    private Map<String, Integer> sentMailCounter = new HashMap<String, Integer>();

    private Set<String> spamIps;

    private Set<String> spamReferrers;

    public EMailSender(final Set<String> recipients, final JavaMailSender mailSender, final Set<String> spamIps,
            final Set<String> spamReferrers) {
        this.recipients = recipients;
        this.mailSender = mailSender;
        this.spamIps = spamIps;
        this.spamReferrers = spamReferrers;
        this.excludedFields = new HashSet<String>();
        for (String element : XCLUDED_FIELDS) {
            this.excludedFields.add(element);
        }
    }

    public void addSpamIP(final String spamIP) {
        this.spamIps.add(spamIP);
    }

    public void addSpamReferrer(final String spamReferrer) {
        this.spamReferrers.add(spamReferrer);
    }

    public boolean removeSpamIP(final String spamIP) {
        return this.spamIps.remove(spamIP);
    }

    public boolean removeSpamReferrer(final String spamReferrer) {
        return this.spamReferrers.remove(spamReferrer);
    }

    public void sendEmail(final Map<String, Object> map) {
        validateModel(map);
        final MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setSubject((String) map.get(SUBJECT));
            helper.setTo((String) map.get(RECIPIENT));
            String from = (String) map.get(EMAIL);
            if (from == null || !EMAIL_PATTERN.matcher(from).matches()) {
                from = "MAILER-DAEMON@stanford.edu";
            }
            helper.setFrom(from);
            StringBuilder text = new StringBuilder();
            for (Entry<String, Object> entry : map.entrySet()) {
                String field = entry.getKey();
                if (!this.excludedFields.contains(field)) {
                    text.append(field).append(": ").append(entry.getValue()).append("\n\n");
                }
            }
            helper.setText(text.toString());
        } catch (MessagingException e) {
            throw new LanewebException(e);
        }
        maybeBlockBeforeSend();
        try {
            this.mailSender.send(message);
        } catch (MailException e) {
            throw new LanewebException(map.toString(), e);
        }
    }

    private synchronized void maybeBlockBeforeSend() {
        updateSentCountsIfNecessary();
        int sent = 0;
        if (this.sentMailCounter.containsKey(this.remoteIp)) {
            sent = this.sentMailCounter.get(this.remoteIp).intValue();
        }
        this.sentMailCounter.put(this.remoteIp, Integer.valueOf(++sent));
        if (sent > MAX_MAILS_PER_IP) {
            throw new LanewebException("too many emails from IP: " + this.remoteIp + "; # sent: " + sent);
        }
    }

    private synchronized void updateSentCountsIfNecessary() {
        long now = System.currentTimeMillis();
        if (now > this.lastUpdate + COUNT_CHECK_INTERVAL) {
            for (Entry<String, Integer> entry : this.sentMailCounter.entrySet()) {
                int newCount = entry.getValue().intValue() - MAX_MAILS_PER_IP;
                if (newCount <= 0) {
                    this.sentMailCounter.remove(entry.getKey());
                } else {
                    this.sentMailCounter.put(entry.getKey(), Integer.valueOf(newCount));
                }
            }
            this.lastUpdate = now;
        }
    }

    private void validateModel(final Map<String, Object> map) {
        Object recipient = map.get(RECIPIENT);
        if (!this.recipients.contains(recipient)) {
            throw new LanewebException(RECIPIENT + " " + recipient + " not permitted");
        }
        this.remoteIp = (String) map.get(Model.REMOTE_ADDR);
        if (this.spamIps.contains(this.remoteIp)) {
            throw new LanewebException(this.remoteIp + " is in the spam IP list");
        }
        Object referrer = map.get(Model.REFERRER);
        if (this.spamReferrers.contains(referrer)) {
            throw new LanewebException(referrer + " is in the spam referrer list");
        }
    }
}
