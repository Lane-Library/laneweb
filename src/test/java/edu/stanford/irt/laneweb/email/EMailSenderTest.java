package edu.stanford.irt.laneweb.email;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import edu.stanford.irt.laneweb.LanewebException;
import jakarta.mail.Address;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EMailSenderTest {

    private EMailSender eMailSender;

    private JavaMailSender javaMailSender;

    private Map<String, Object> map;

    private MimeMessage message;

    @BeforeEach
    public void setUp() throws Exception {
        this.javaMailSender = mock(JavaMailSender.class);
        this.eMailSender = new EMailSender(this.javaMailSender);
        this.map = new HashMap<>();
        this.message = mock(MimeMessage.class);
    }

    @Test
    public void testSendEmail() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put("subject", "subject");
        this.map.put("email", "email@foo.com");
        this.map.put("message", "message");
        expect(this.javaMailSender.createMimeMessage()).andReturn(this.message);
        this.message.setSubject("subject");
        this.message.setRecipient(eq(RecipientType.TO), isA(Address.class));
        this.message.setFrom(isA(Address.class));
        this.message.setText("message: message\n\n");
        this.javaMailSender.send(this.message);
        replay(this.javaMailSender, this.message);
        this.eMailSender.sendEmail(this.map);
        verify(this.javaMailSender, this.message);
    }

    @Test
    public void testSendEmailBadEmail() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put("subject", "subject");
        this.map.put("email", "email");
        this.map.put("message", "message");
        expect(this.javaMailSender.createMimeMessage()).andReturn(this.message);
        this.message.setSubject("subject");
        this.message.setRecipient(eq(RecipientType.TO), isA(Address.class));
        this.message.setFrom(isA(Address.class));
        this.message.setText("message: message\n\n");
        this.javaMailSender.send(this.message);
        replay(this.javaMailSender, this.message);
        this.eMailSender.sendEmail(this.map);
        verify(this.javaMailSender, this.message);
    }

    @Test
    public void testSendEmailInValidFromPattern() throws MessagingException {
        String from = "[bad] <email@foo.com>";
        this.map.put("recipient", "recipient");
        this.map.put("subject", "subject");
        this.map.put("email", from);
        this.map.put("message", "message");
        expect(this.javaMailSender.createMimeMessage()).andReturn(this.message);
        this.message.setSubject("subject");
        this.message.setRecipient(eq(RecipientType.TO), isA(Address.class));
        this.message.setFrom(InternetAddress.parse("MAILER-DAEMON@stanford.edu")[0]);
        this.message.setText("message: message\n\n");
        this.javaMailSender.send(this.message);
        replay(this.javaMailSender, this.message);
        this.eMailSender.sendEmail(this.map);
        verify(this.javaMailSender, this.message);
    }

    @Test
    public void testSendEmailNullEmail() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put("subject", "subject");
        this.map.put("message", "message");
        expect(this.javaMailSender.createMimeMessage()).andReturn(this.message);
        this.message.setSubject("subject");
        this.message.setRecipient(eq(RecipientType.TO), isA(Address.class));
        this.message.setFrom(isA(Address.class));
        this.message.setText("message: message\n\n");
        this.javaMailSender.send(this.message);
        replay(this.javaMailSender, this.message);
        this.eMailSender.sendEmail(this.map);
        verify(this.javaMailSender, this.message);
    }

    @Test
    public void testSendEmailSendingThrows() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put("subject", "subject");
        this.map.put("email", "email@foo.com");
        this.map.put("message", "message");
        expect(this.javaMailSender.createMimeMessage()).andReturn(this.message);
        this.message.setSubject("subject");
        this.message.setRecipient(eq(RecipientType.TO), isA(Address.class));
        this.message.setFrom(isA(Address.class));
        this.message.setText("message: message\n\n");
        this.javaMailSender.send(this.message);
        expectLastCall().andThrow(new MailSendException("oops"));
        replay(this.javaMailSender, this.message);
        try {
            this.eMailSender.sendEmail(this.map);
        } catch (LanewebException e) {
        }
        verify(this.javaMailSender, this.message);
    }

    @Test
    public void testSendEmailSetSubjectThrows() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put("subject", "subject");
        this.map.put("email", "email@foo.com");
        this.map.put("message", "message");
        expect(this.javaMailSender.createMimeMessage()).andReturn(this.message);
        this.message.setSubject("subject");
        expectLastCall().andThrow(new MessagingException());
        replay(this.javaMailSender, this.message);
        try {
            this.eMailSender.sendEmail(this.map);
        } catch (LanewebException e) {
        }
        verify(this.javaMailSender, this.message);
    }

    @Test
    public void testSendEmailValidFromPattern() throws MessagingException {
        String from = "test-er t. person with \"nickname\" (john) <email@foo.com>";
        this.map.put("recipient", "recipient");
        this.map.put("subject", "subject");
        this.map.put("email", from);
        this.map.put("message", "message");
        expect(this.javaMailSender.createMimeMessage()).andReturn(this.message);
        this.message.setSubject("subject");
        this.message.setRecipient(eq(RecipientType.TO), isA(Address.class));
        InternetAddress[] fromAddress = InternetAddress.parse(from);
        this.message.setFrom(fromAddress[0]);
        this.message.setText("message: message\n\n");
        this.javaMailSender.send(this.message);
        replay(this.javaMailSender, this.message);
        this.eMailSender.sendEmail(this.map);
        verify(this.javaMailSender, this.message);
    }
}
