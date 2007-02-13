package edu.stanford.laneweb;

import junit.framework.TestCase;

public class TicketTest extends TestCase {

	private Ticket ticket;
	private String sunetid;
	private String ezproxyKey;
	protected void setUp() throws Exception {
		super.setUp();
		this.sunetid = "ceyates";
		this.ezproxyKey = "abcdefg";
		this.ticket = new Ticket(this.ezproxyKey, this.sunetid);
	}

	public void testTicket() {
		try {
			new Ticket(this.ezproxyKey, null);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
		try {
			new Ticket(null, this.sunetid);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
	}

	public void testToString() {
		assertEquals(46,this.ticket.toString().length());
	}

}
