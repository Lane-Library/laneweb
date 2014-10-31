package edu.stanford.irt.laneweb.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TicketTest {

    private String ezproxyKey;

    private String userid;

    private Ticket ticket;

    @Before
    public void setUp() {
        this.userid = "ceyates";
        this.ezproxyKey = "boguskey";
        this.ticket = new Ticket(this.userid, this.ezproxyKey);
    }

    @Test
    public void testTicket() {
        try {
            new Ticket(null, this.ezproxyKey);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Ticket(this.userid, null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testToString() {
        assertEquals(50, this.ticket.toString().length());
    }
}
