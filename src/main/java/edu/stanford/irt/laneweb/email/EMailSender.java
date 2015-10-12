package edu.stanford.irt.laneweb.email;

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

public class EMailSender {

    private static final String BINDING_MAP = "org.springframework.validation.BindingResult.map";

    private static final String EMAIL = "email";

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$",
            Pattern.CASE_INSENSITIVE);

    private static final String RECIPIENT = "recipient";

    private static final String REDIRECT = "redirect";

    private static final String SUBJECT = "subject";

    private static final String[] XCLUDED_FIELDS = new String[] { SUBJECT, RECIPIENT, EMAIL, BINDING_MAP, REDIRECT };

    private Set<String> excludedFields;

    private JavaMailSender mailSender;

    private SpamFilter spamFilter;

    public EMailSender(final JavaMailSender mailSender, final SpamFilter spamFilter) {
        this.mailSender = mailSender;
        this.spamFilter = spamFilter;
        this.excludedFields = new HashSet<String>();
        for (String element : XCLUDED_FIELDS) {
            this.excludedFields.add(element);
        }
    }

    public void sendEmail(final Map<String, Object> map) {
        this.spamFilter.checkForSpam(map);
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
        System.out.println(map);
        try {
            this.mailSender.send(message);
        } catch (MailException e) {
            throw new LanewebException(map.toString(), e);
        }
    }
}
