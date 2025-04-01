package edu.stanford.irt.laneweb.ipgroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

public class IPGroupTest {

    @Test
    public void testAllRanges() {
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("45.42.34.136"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("68.65.248.136"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("199.68.152.135"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("159.140.183.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("159.140.183.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.112.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.112.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("171.65.126.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("171.65.126.127"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("209.11.188.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("209.11.191.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("0.0.0.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("11.0.0.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("128.11.255.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("128.13.0.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("134.78.255.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("134.80.0.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("152.130.10.127"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("152.130.10.129"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("152.131.10.127"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("152.131.10.129"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("152.132.10.127"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("152.132.10.129"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("152.133.10.127"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("152.133.10.129"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("159.140.182.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("159.140.184.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("171.63.255.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("171.65.65.46"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("171.68.0.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("204.161.119.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("204.161.121.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("209.11.183.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("209.11.192.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("9.255.255.255"));
        assertEquals(IPGroup.PAVA, IPGroup.getGroupForIP("152.130.10.128"));
        assertEquals(IPGroup.PAVA, IPGroup.getGroupForIP("152.131.10.128"));
        assertEquals(IPGroup.PAVA, IPGroup.getGroupForIP("152.132.10.128"));
        assertEquals(IPGroup.PAVA, IPGroup.getGroupForIP("152.133.10.128"));
        assertEquals(IPGroup.PAVA, IPGroup.getGroupForIP("171.65.44.0"));
        assertEquals(IPGroup.PAVA, IPGroup.getGroupForIP("171.65.44.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.115.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.115.255"));
        assertNotEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.125.0"));
        assertNotEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.125.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.127.0"));
        assertNotEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.159.255"));
        assertNotEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.168.0"));
        assertNotEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.255.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.46.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.47.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("204.161.120.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("204.161.120.255"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("209.11.184.0"));
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("209.11.187.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.20.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.4.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.95.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.40.64"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.82.167"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.167.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.179.100"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.0.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.1.212"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.160.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.163.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.65.82.102"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("128.12.0.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("128.12.255.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("134.79.0.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("134.79.255.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.64.0.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.64.255.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.66.0.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("171.67.255.255"));
    }

    @Test
    public void testEquals() {
        assertNotEquals(IPGroup.LPCH, new Object());
        assertEquals(IPGroup.SHC, new IPGroup("SHC"));
        assertNotEquals(IPGroup.SHC, new IPGroup("LPCH"));
    }

    @Test
    public void testGetipGroupForIP() {
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("199.68.152.135"));
    }

    @Test
    public void testLPCHIps() throws IOException {
        BufferedReader lpchIps = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("lpch-ips.txt")));
        String ip = null;
        while ((ip = lpchIps.readLine()) != null) {
            assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testProxyServer() {
        assertEquals(IPGroup.OTHER, IPGroup.getGroupForIP("171.65.65.46"));
    }

    @Test
    public void testSerializedFormEquals() throws IOException, ClassNotFoundException {
        IPGroup shc = IPGroup.SHC;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(shc);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Object o = ois.readObject();
        ois.close();
        assertEquals(shc, o);
    }

    @Test
    public void testSomSHCIps() throws IOException {
        BufferedReader shcIps = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("som-shc-ips.txt")));
        String ip = null;
        while ((ip = shcIps.readLine()) != null) {
            assertEquals(IPGroup.SHC, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testToString() {
        assertEquals("LPCH", IPGroup.LPCH.toString());
    }
}
