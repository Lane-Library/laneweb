package edu.stanford.irt.laneweb.email;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class SpamFilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Map<String, Object> map;

    private SpamFilter spamFilter;

    private Set<String> spamIPs;

    private Set<String> spamReferrers;

    @Before
    public void setUp() throws Exception {
        this.spamIPs = new HashSet<>();
        this.spamIPs.add("127.0.0.1");
        this.spamReferrers = new HashSet<>();
        this.spamReferrers.add("http://spam/ref.html");
        this.spamFilter = new SpamFilter(Collections.singleton("recipient"), this.spamIPs, this.spamReferrers);
        this.map = new HashMap<>();
    }

    @Test
    public void testAddSpamIP() {
        this.spamFilter.addSpamIP("127.0.0.2");
        assertTrue(this.spamIPs.contains("127.0.0.2"));
    }

    @Test
    public void testAddSpamReferrer() {
        this.spamFilter.addSpamReferrer("http://spam/ref2.html");
        assertTrue(this.spamReferrers.contains("http://spam/ref2.html"));
    }

    @Test
    public void testRemoveSpamReferrer() {
        this.spamFilter.removeSpamReferrer("http://spam/ref.html");
        assertFalse(this.spamIPs.contains("http://spam/ref.html"));
    }

    @Test
    public void testRemoveSpanIP() {
        this.spamFilter.removeSpamIP("127.0.0.1");
        assertFalse(this.spamIPs.contains("127.0.0.1"));
    }

    @Test(expected = LanewebException.class)
    public void testSendEmailBadRecipient() {
        this.map.put("recipient", "badrecipient");
        this.spamFilter.checkForSpam(this.map);
    }

    @Test(expected = LanewebException.class)
    public void testSendMailSpamIP() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put(Model.REMOTE_ADDR, "127.0.0.1");
        this.spamFilter.checkForSpam(this.map);
    }

    @Test(expected = LanewebException.class)
    public void testSendMailSpamReferrer() throws MessagingException {
        this.map.put("recipient", "recipient");
        this.map.put(Model.REFERRER, "http://spam/ref.html");
        this.spamFilter.checkForSpam(this.map);
    }

    @Test
    public void testSendMailTooManyMessages() throws MessagingException {
        this.thrown.expect(LanewebException.class);
        this.thrown.expectMessage("too many emails from IP: null; # sent: 11");
        this.map.put("recipient", "recipient");
        this.map.put("subject", "subject");
        this.map.put("email", "email@foo.com");
        this.map.put("message", "message");
        for (int i = 0; i <= 10; i++) {
            this.spamFilter.checkForSpam(this.map);
        }
    }
}
