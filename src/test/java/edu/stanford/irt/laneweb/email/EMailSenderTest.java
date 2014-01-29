package edu.stanford.irt.laneweb.email;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class EMailSenderTest {

    private EMailSender eMailSender;

    private JavaMailSender javaMailSender;

    private Map<String, Object> map;

    private MimeMessage message;

    private Set<String> spamIPs;

    private Set<String> spamReferrers;

    @Before
    public void setUp() throws Exception {
        this.javaMailSender = createMock(JavaMailSender.class);
        this.spamIPs = new HashSet<String>();
        this.spamIPs.add("127.0.0.1");
        this.spamReferrers = new HashSet<String>();
        this.spamReferrers.add("http://spam/ref.html");
        this.eMailSender = new EMailSender(Collections.singleton("recipient"), this.javaMailSender, this.spamIPs,
                this.spamReferrers);
        this.map = new HashMap<String, Object>();
        this.message = createMock(MimeMessage.class);
    }

    @Test
    public void testAddSpamIP() {
        this.eMailSender.addSpamIP("127.0.0.2");
        assertTrue(this.spamIPs.contains("127.0.0.2"));
    }

    @Test
    public void testAddSpamReferrer() {
        this.eMailSender.addSpamReferrer("http://spam/ref2.html");
        assertTrue(this.spamReferrers.contains("http://spam/ref2.html"));
    }

    @Test
    public void testRemoveSpamReferrer() {
        this.eMailSender.removeSpamReferrer("http://spam/ref.html");
        assertFalse(this.spamIPs.contains("http://spam/ref.html"));
    }

    @Test
    public void testRemoveSpanIP() {
        this.eMailSender.removeSpamIP("127.0.0.1");
        assertFalse(this.spamIPs.contains("127.0.0.1"));
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
    public void testSendEmailBadRecipient() {
        this.map.put("recipient", "badrecipient");
        replay(this.javaMailSender, this.message);
        try {
            this.eMailSender.sendEmail(this.map);
        } catch (LanewebException e) {
        }
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
    public void testSendEmailThrows() throws MessagingException {
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
    public void testSendMailSpamIP() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put(Model.REMOTE_ADDR, "127.0.0.1");
        replay(this.javaMailSender, this.message);
        try {
            this.eMailSender.sendEmail(this.map);
        } catch (LanewebException e) {
        }
        verify(this.javaMailSender, this.message);
    }

    @Test
    public void testSendMailSpamReferrer() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put(Model.REFERRER, "http://spam/ref.html");
        replay(this.javaMailSender, this.message);
        try {
            this.eMailSender.sendEmail(this.map);
        } catch (LanewebException e) {
        }
        verify(this.javaMailSender, this.message);
    }
}
