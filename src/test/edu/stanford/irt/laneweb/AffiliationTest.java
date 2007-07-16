package edu.stanford.irt.laneweb;

import junit.framework.TestCase;

public class AffiliationTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testToString() {
		assertEquals(Affiliation.LPCH.toString(),"LPCH");
	}

	public void testGetAffiliationForIP() {
		assertEquals(Affiliation.LPCH, Affiliation.getAffiliationForIP("10.252.31.112"));
	}

}
