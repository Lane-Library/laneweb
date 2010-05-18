package edu.stanford.irt.laneweb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class IPGroupTest {

    @Test
    public void testGetipGroupForIP() {
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.31.112"));
    }

    @Test
    public void testNotStaffIps() throws IOException {
        BufferedReader notStaffIps =
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("not-staff-ips.txt")));
        String ip = null;
        while ((ip = notStaffIps.readLine()) != null) {
            assertNotSame(ip, IPGroup.STAFF, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testProxyServer() {
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("171.65.65.46"));
    }

    @Test
    public void testSomBeckmanIps() throws IOException {
        BufferedReader staffIps =
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("som-beckman-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_BECKMAN, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomCCSRIps() throws IOException {
        BufferedReader staffIps =
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("som-ccsr-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_CCSR, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomClarkIps() throws IOException {
        BufferedReader staffIps =
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("som-clark-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_CLARK, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomGrantIps() throws IOException {
        BufferedReader staffIps =
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("som-grant-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_GRANT, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomLaneIps() throws IOException {
        BufferedReader staffIps =
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("som-lane-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_LANE, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomLKSCIps() throws IOException {
        BufferedReader staffIps =
            new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("som-lksc-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_LKSC, IPGroup.getGroupForIP(ip));
        }
    }
    
    @Test
    public void testSomSIM1Ips() throws IOException {
        BufferedReader staffIps =
            new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("som-sim1-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_SIM1, IPGroup.getGroupForIP(ip));
        }
    }
    
    @Test
    public void testSomSHCIps() throws IOException {
        BufferedReader staffIps =
            new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("som-shc-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SHC, IPGroup.getGroupForIP(ip));
        }
    }
    
    @Test
    public void testStaffIps() throws IOException {
        BufferedReader staffIps =
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("staff-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.STAFF, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testToString() {
        assertEquals(IPGroup.LPCH.toString(), "LPCH");
    }
}
