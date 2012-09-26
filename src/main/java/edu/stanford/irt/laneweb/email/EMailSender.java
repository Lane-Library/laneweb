package edu.stanford.irt.laneweb.email;

import java.security.PrivilegedAction;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.security.auth.Subject;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ldap.SubjectSource;


public class EMailSender {
    
    private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
    
    private JavaMailSender mailSender;
    
    private SubjectSource subjectSource;
    
    private Set<String> recipients;
    
    public EMailSender(Set<String> recipients, SubjectSource subjectSource, JavaMailSender mailSender) {
        this.recipients = recipients;
        this.subjectSource = subjectSource;
        this.mailSender = mailSender;
    }
    
    public void sendEmail(Map<String, Object> map) {
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
                text.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n\n");
            }
            helper.setText(text.toString());
        } catch (MessagingException e) {
            throw new LanewebException(e);
        }
        Subject subject = this.subjectSource.getSubject();
        Subject.doAs(subject, new PrivilegedAction<Object>() {

            @Override
            public Object run() {
                EMailSender.this.mailSender.send(message);
                return null;
            }
        });
    }

    private void validateModel(Map<String, Object> map) {
        Object recipient = map.get("recipient");
        if (!this.recipients.contains(recipient)) {
            throw new LanewebException("recipient " + recipient + " not permitted");
        }
    }
}
