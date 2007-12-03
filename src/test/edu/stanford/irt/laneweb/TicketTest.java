package edu.stanford.irt.laneweb;

import junit.framework.TestCase;

public class TicketTest extends TestCase {

    private Ticket ticket;

    private String sunetid;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.sunetid = "ceyates";
        this.ticket = new Ticket(this.sunetid);
    }

    public void testTicket() {
        try {
            new Ticket(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testToString() {
        assertEquals(46, this.ticket.toString().length());
    }

}
