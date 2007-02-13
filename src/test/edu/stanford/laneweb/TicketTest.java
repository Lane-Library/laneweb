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
		fail("Not yet implemented"); // TODO
	}

	public void testToString() {
		fail("Not yet implemented"); // TODO
	}

}
