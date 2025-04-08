package edu.stanford.irt.laneweb.email;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class EMailSender {

    private static final String BINDING_MAP = "org.springframework.validation.BindingResult.map";

    private static final String DEV_EMAIL = "lane-crm-dev@stanford.edu";

    private static final String EMAIL = "email";

    // "name@domain.com" or "jane doe <name@domain.com>"
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^(?:[\\w \\-'\"\\.\\(\\)]+ <)?[\\w\\.%\\+\\-]+@[\\w.-]+\\.[A-Z]{2,4}>?$", Pattern.CASE_INSENSITIVE);

    private static final String LANE_STANFORD_EDU = "lane.stanford.edu";

    private static final String RECIPIENT = "recipient";

    private static final String REDIRECT = "redirect";

    private static final String SUBJECT = "subject";

    private static final String[] XCLUDED_FIELDS = new String[] { SUBJECT, RECIPIENT, EMAIL, BINDING_MAP, REDIRECT };

    private Set<String> excludedFields;

    private JavaMailSender mailSender;

    public EMailSender(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.excludedFields = new HashSet<>();
        for (String element : XCLUDED_FIELDS) {
            this.excludedFields.add(element);
        }
    }

    public void sendEmail(final Map<String, Object> map) {
        sendEmail(map, null);
    }

    public void sendEmail(final Map<String, Object> map, final File[] files) {
        final MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, (files != null));
            helper.setSubject((String) map.get(SUBJECT));
            helper.setTo(getRecipient(map));
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
            if (files != null) {
                for (File file : files) {
                    if (file != null) {
                        helper.addAttachment(file.getName(), file);
                    }
                }
            }
        } catch (MessagingException e) {
            throw new LanewebException(e);
        }
        try {
            this.mailSender.send(message);
        } catch (MailException e) {
            throw new LanewebException(map.toString(), e);
        }
    }

    /*
     * Send email to lane-crm-dev@stanford.edu if the host is not lane.stanford.edu. emailDataBinder (gets host from
     * HttpServletRequest) is required when using EMailSender
     */
    private String getRecipient(final Map<String, Object> map) {
        String host = (String) map.get(Model.HOST);
        if (!LANE_STANFORD_EDU.equals(host)) {
            return DEV_EMAIL;
        }
        return (String) map.get(RECIPIENT);
    }
}
