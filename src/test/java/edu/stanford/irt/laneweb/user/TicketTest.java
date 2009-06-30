package edu.stanford.irt.laneweb.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TicketTest {

    private String ezproxyKey;

    private String sunetid;

    private Ticket ticket;

    @Test
    public void testTicket() {
        try {
            new Ticket(null, this.ezproxyKey);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Ticket(this.sunetid, null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testToString() {
        assertEquals(46, this.ticket.toString().length());
    }

    @Before
    public void setUp() {
        this.sunetid = "ceyates";
        this.ezproxyKey = "boguskey";
        this.ticket = new Ticket(this.sunetid, this.ezproxyKey);
    }
}
