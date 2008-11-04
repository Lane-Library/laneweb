package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import junit.framework.TestCase;

public class AffiliationTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testToString() {
        assertEquals(Affiliation.LPCH.toString(), "LPCH");
    }

    public void testGetAffiliationForIP() {
        assertEquals(Affiliation.LPCH, Affiliation.getAffiliationForIP("10.252.31.112"));
    }

    public void testStaffIps() throws IOException {
        LineNumberReader staffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("staff-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, Affiliation.STAFF, Affiliation.getAffiliationForIP(ip));
        }
    }

    public void testNotStaffIps() throws IOException {
        LineNumberReader notStaffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("not-staff-ips.txt")));
        String ip = null;
        while ((ip = notStaffIps.readLine()) != null) {
            assertNotSame(ip, Affiliation.STAFF, Affiliation.getAffiliationForIP(ip));
        }
    }

}
