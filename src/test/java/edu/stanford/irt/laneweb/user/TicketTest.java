package edu.stanford.irt.laneweb.user;

import junit.framework.TestCase;

public class TicketTest extends TestCase {

    private String ezproxyKey;

    private String sunetid;

    private Ticket ticket;

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

    public void testToString() {
        assertEquals(46, this.ticket.toString().length());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.sunetid = "ceyates";
        this.ezproxyKey = "boguskey";
        this.ticket = new Ticket(this.sunetid, this.ezproxyKey);
    }
}
