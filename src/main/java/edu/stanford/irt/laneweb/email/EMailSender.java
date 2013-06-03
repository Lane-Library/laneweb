package edu.stanford.irt.laneweb.email;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class EMailSender {

    private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$",
            Pattern.CASE_INSENSITIVE);

    private static final String[] EXCLUDED_FIELDS = new String[] { "subject", "recipient", "email" };

    private Set<String> excludedFields;

    private JavaMailSender mailSender;

    private Set<String> recipients;

    private Set<String> spamIps;

    public EMailSender(final Set<String> recipients, final JavaMailSender mailSender, final Set<String> spamIps) {
        this.recipients = recipients;
        this.mailSender = mailSender;
        this.spamIps = spamIps;
        this.excludedFields = new HashSet<String>();
        for (String element : EXCLUDED_FIELDS) {
            this.excludedFields.add(element);
        }
    }

    public void sendEmail(final Map<String, Object> map) {
        validateModel(map);
        final MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setSubject((String) map.get("subject"));
            helper.setTo((String) map.get("recipient"));
            String from = (String) map.get("email");
            if (from == null || !EMAIL.matcher(from).matches()) {
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
        this.mailSender.send(message);
    }

    private void validateModel(final Map<String, Object> map) {
        Object recipient = map.get("recipient");
        if (!this.recipients.contains(recipient)) {
            throw new LanewebException("recipient " + recipient + " not permitted");
        }
        Object remoteIp = map.get(Model.REMOTE_ADDR);
        if (this.spamIps.contains(remoteIp)) {
            throw new LanewebException(remoteIp + " is in the spam list");
        }
    }
}
