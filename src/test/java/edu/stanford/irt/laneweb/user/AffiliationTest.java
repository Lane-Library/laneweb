package edu.stanford.irt.laneweb.user;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import junit.framework.TestCase;

public class AffiliationTest extends TestCase {

    public void testGetAffiliationForIP() {
        assertEquals(TrackingAffiliation.LPCH, TrackingAffiliation.getAffiliationForIP("10.252.31.112"));
    }

    public void testNotStaffIps() throws IOException {
        LineNumberReader notStaffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("not-staff-ips.txt")));
        String ip = null;
        while ((ip = notStaffIps.readLine()) != null) {
            assertNotSame(ip, TrackingAffiliation.STAFF, TrackingAffiliation.getAffiliationForIP(ip));
        }
    }

    public void testStaffIps() throws IOException {
        LineNumberReader staffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("staff-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, TrackingAffiliation.STAFF, TrackingAffiliation.getAffiliationForIP(ip));
        }
    }

    public void testToString() {
        assertEquals(TrackingAffiliation.LPCH.toString(), "LPCH");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
}
