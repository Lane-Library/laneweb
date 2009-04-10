package edu.stanford.irt.laneweb.user;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import junit.framework.TestCase;

public class IPGroupTest extends TestCase {

    public void testGetipGroupForIP() {
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.31.112"));
    }

    public void testNotStaffIps() throws IOException {
        LineNumberReader notStaffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("not-staff-ips.txt")));
        String ip = null;
        while ((ip = notStaffIps.readLine()) != null) {
            assertNotSame(ip, IPGroup.STAFF, IPGroup.getGroupForIP(ip));
        }
    }

    public void testSomBeckmanIps() throws IOException {
        LineNumberReader staffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("som-beckman-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_BECKMAN, IPGroup.getGroupForIP(ip));
        }
    }

    public void testSomCCSRIps() throws IOException {
      LineNumberReader staffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("som-ccsr-ips.txt")));
      String ip = null;
      while ((ip = staffIps.readLine()) != null) {
        assertEquals(ip, IPGroup.SOM_CCSR, IPGroup.getGroupForIP(ip));
      }
    }
    
    public void testSomClarkIps() throws IOException {
      LineNumberReader staffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("som-clark-ips.txt")));
      String ip = null;
      while ((ip = staffIps.readLine()) != null) {
        assertEquals(ip, IPGroup.SOM_CLARK, IPGroup.getGroupForIP(ip));
      }
    }
    
    public void testSomGrantIps() throws IOException {
      LineNumberReader staffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("som-grant-ips.txt")));
      String ip = null;
      while ((ip = staffIps.readLine()) != null) {
        assertEquals(ip, IPGroup.SOM_GRANT, IPGroup.getGroupForIP(ip));
      }
    }
    
    public void testSomLaneIps() throws IOException {
      LineNumberReader staffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("som-lane-ips.txt")));
      String ip = null;
      while ((ip = staffIps.readLine()) != null) {
        assertEquals(ip, IPGroup.SOM_LANE, IPGroup.getGroupForIP(ip));
      }
    }
    
    public void testStaffIps() throws IOException {
      LineNumberReader staffIps = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("staff-ips.txt")));
      String ip = null;
      while ((ip = staffIps.readLine()) != null) {
        assertEquals(ip, IPGroup.STAFF, IPGroup.getGroupForIP(ip));
      }
    }
    
    public void testToString() {
        assertEquals(IPGroup.LPCH.toString(), "LPCH");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
}
