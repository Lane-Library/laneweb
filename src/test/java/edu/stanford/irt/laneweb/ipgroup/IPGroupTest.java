package edu.stanford.irt.laneweb.ipgroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class IPGroupTest {

    @Test
    public void testAllRanges() {
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("45.42.34.136"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("68.65.248.136"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("199.68.152.135"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.232.53.46"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.233.24.133"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.238.0.221"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.238.31.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.239.0.221"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.239.23.145"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.250.128.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.250.217.148"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.250.255.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.251.128.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.251.199.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.251.255.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.1.1"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.1.2"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.10.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.107.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.109.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.11.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.122.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.123.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.138.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.149.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.152.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.153.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.18.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.19.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.200.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.200.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.22.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.23.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.250.126"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.250.97"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.26.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.27.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.30.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.31.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.34.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.35.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.38.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.39.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.42.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.43.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.46.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.47.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.5.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.50.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.51.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.54.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.55.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.60.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.63.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.64.12"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.64.23"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.64.46"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.64.60"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.64.64"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.64.71"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.13"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.14"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.144"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.145"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.146"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.147"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.41"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.42"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.233"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.234"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.249"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.250"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.253"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.254"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.49"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.5"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.50"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.66.6"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.67.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.67.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.7.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.70.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.71.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.84.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.85.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.253.220.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.253.220.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.253.254.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.253.255.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.50.0.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.50.255.255"));
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
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.247.0.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.248.255.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.250.0.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.250.127.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.251.0.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.251.127.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.0.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.1.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.1.3"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.106.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.110.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.12.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.121.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.124.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.137.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.150.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.151.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.154.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.17.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.199.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.20.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.201.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.21.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.24.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.25.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.28.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.29.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.32.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.33.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.36.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.37.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.4.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.40.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.41.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.44.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.45.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.48.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.49.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.52.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.53.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.56.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.59.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.11"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.13"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.22"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.24"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.45"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.47"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.59"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.61"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.63"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.65"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.70"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.64.72"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.10"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.140"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.143"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.16"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.19"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.4"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.7"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.8"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.68.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.69.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.72.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.8.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.83.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.86.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.9.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.253.219.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.253.221.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.253.253.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.254.0.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.255.255.255"));
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
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("10.0.0.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("10.246.255.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("10.249.0.0"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("10.249.255.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("10.49.255.255"));
        assertEquals(IPGroup.SU, IPGroup.getGroupForIP("10.51.0.0"));
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
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.31.112"));
    }

    @Test
    public void testLPCHIps() throws IOException {
        BufferedReader lpchIps = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("lpch-ips.txt")));
        String ip = null;
        while ((ip = lpchIps.readLine()) != null) {
            assertEquals(ip, IPGroup.LPCH, IPGroup.getGroupForIP(ip));
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
            assertEquals(ip, IPGroup.SHC, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testToString() {
        assertEquals("LPCH", IPGroup.LPCH.toString());
    }
}
