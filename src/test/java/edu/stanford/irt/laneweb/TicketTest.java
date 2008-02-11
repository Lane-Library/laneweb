package edu.stanford.irt.laneweb;

import junit.framework.TestCase;

public class TicketTest extends TestCase {

    private Ticket ticket;

    private String sunetid;

    private String ezproxyKey;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.sunetid = "ceyates";
        this.ezproxyKey = "boguskey";
        this.ticket = new Ticket(this.sunetid, this.ezproxyKey);
    }

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

}
