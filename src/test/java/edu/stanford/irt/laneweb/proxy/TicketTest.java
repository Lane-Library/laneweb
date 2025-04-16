package edu.stanford.irt.laneweb.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketTest {

    private String ezproxyKey;

    private Ticket ticket;

    private String userid;

    @BeforeEach
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
