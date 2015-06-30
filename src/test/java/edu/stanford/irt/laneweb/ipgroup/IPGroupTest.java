package edu.stanford.irt.laneweb.ipgroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class IPGroupTest {

    @Test
    public void testAllRanges() {
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.250.128.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.250.217.148"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.250.255.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.251.128.0"));
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
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.141"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.142"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.145"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.146"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.17"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.18"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.41"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.42"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.5"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.6"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.65.9"));
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
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.253.220.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.253.220.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.253.254.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.253.255.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.50.0.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.50.255.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("159.140.183.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("159.140.183.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("171.65.112.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("171.65.112.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("171.65.126.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("171.65.126.255"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("209.11.188.0"));
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("209.11.191.255"));
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
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.250.127"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.250.96"));
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
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.12"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.140"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.143"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.144"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.147"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.15"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.16"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.19"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.4"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.40"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.43"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.7"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.65.8"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.232"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.235"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.248"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.251"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.252"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.4"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.48"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.51"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("10.252.66.7"));
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
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.125.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.125.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.127.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.159.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.168.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.255.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.46.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("171.65.47.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("204.161.120.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("204.161.120.255"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("209.11.184.0"));
        assertEquals(IPGroup.SHC, IPGroup.getGroupForIP("209.11.187.255"));
        assertEquals(IPGroup.SOM_BECKMAN, IPGroup.getGroupForIP("171.65.20.0"));
        assertEquals(IPGroup.SOM_BECKMAN, IPGroup.getGroupForIP("171.65.27.255"));
        assertEquals(IPGroup.SOM_CCSR, IPGroup.getGroupForIP("171.65.15.255"));
        assertEquals(IPGroup.SOM_CCSR, IPGroup.getGroupForIP("171.65.4.0"));
        assertEquals(IPGroup.SOM_CLARK, IPGroup.getGroupForIP("171.65.102.0"));
        assertEquals(IPGroup.SOM_CLARK, IPGroup.getGroupForIP("171.65.103.255"));
        assertEquals(IPGroup.SOM_CLARK, IPGroup.getGroupForIP("171.65.71.0"));
        assertEquals(IPGroup.SOM_CLARK, IPGroup.getGroupForIP("171.65.71.127"));
        assertEquals(IPGroup.SOM_CLARK, IPGroup.getGroupForIP("171.65.92.0"));
        assertEquals(IPGroup.SOM_CLARK, IPGroup.getGroupForIP("171.65.95.255"));
        assertEquals(IPGroup.SOM_GRANT, IPGroup.getGroupForIP("171.65.40.64"));
        assertEquals(IPGroup.SOM_GRANT, IPGroup.getGroupForIP("171.65.40.79"));
        assertEquals(IPGroup.SOM_GRANT, IPGroup.getGroupForIP("171.65.88.0"));
        assertEquals(IPGroup.SOM_GRANT, IPGroup.getGroupForIP("171.65.91.255"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.0"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.10"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.11"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.15"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.104"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.106"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.108"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.110"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.111"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.113"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.132"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.134"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.140"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.142"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.145"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.165"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.167"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.169"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.17"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.178"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.180"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.19"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.203"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.205"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.206"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.208"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.211"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.213"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.216"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.219"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.23"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.231"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.232"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.233"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.236"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.238"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.239"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.24"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.25"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.41"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.43"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.60"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.67"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.69"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.73"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.75"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.88"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.90"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.94"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.96"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.97"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.82.99"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.113"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.115"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.131"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.132"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.133"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.156"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.157"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.158"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.159"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.161"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.167"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.170"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.171"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.173"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.175"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.188"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.190"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.202"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.207"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.211"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.231"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.232"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.233"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.255"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.31"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.33"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.44"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.46"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.7"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.72"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.74"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.83"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.85"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.87"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.9"));
        assertEquals(IPGroup.SOM_LANE, IPGroup.getGroupForIP("171.65.83.97"));
        assertEquals(IPGroup.SOM_LKSC, IPGroup.getGroupForIP("171.65.164.0"));
        assertEquals(IPGroup.SOM_LKSC, IPGroup.getGroupForIP("171.65.167.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.0.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.1.212"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.1.214"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.101.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.104.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.111.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.113.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.114.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.116.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.124.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.16.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.19.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.28.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.3.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.40.63"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.40.80"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.43.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.45.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.45.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.48.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.65.45"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.65.47"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.70.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.71.128"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.81.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.84.0"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.87.255"));
        assertEquals(IPGroup.SOM_OTHER, IPGroup.getGroupForIP("171.65.96.0"));
        assertEquals(IPGroup.SOM_SIM1, IPGroup.getGroupForIP("171.65.160.0"));
        assertEquals(IPGroup.SOM_SIM1, IPGroup.getGroupForIP("171.65.163.255"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.102"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.12"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.112"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.14"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.144"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.148"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.168"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.191"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.194"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.21"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.27"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.217"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.235"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.240"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.242"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.62"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.82.74"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.169"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.28"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.206"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.209"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.210"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.215"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.216"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.217"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.220"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.221"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.40"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.91"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.98"));
        assertEquals(IPGroup.STAFF, IPGroup.getGroupForIP("171.65.83.99"));
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
    public void testGetipGroupForIP() {
        assertEquals(IPGroup.LPCH, IPGroup.getGroupForIP("10.252.31.112"));
    }

    @Test
    public void testNotStaffIps() throws IOException {
        BufferedReader notStaffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "not-staff-ips.txt")));
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
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "som-beckman-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_BECKMAN, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomCCSRIps() throws IOException {
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "som-ccsr-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_CCSR, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomClarkIps() throws IOException {
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "som-clark-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_CLARK, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomGrantIps() throws IOException {
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "som-grant-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_GRANT, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomLaneIps() throws IOException {
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "som-lane-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_LANE, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomLKSCIps() throws IOException {
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "som-lksc-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_LKSC, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomSHCIps() throws IOException {
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "som-shc-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SHC, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testSomSIM1Ips() throws IOException {
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "som-sim1-ips.txt")));
        String ip = null;
        while ((ip = staffIps.readLine()) != null) {
            assertEquals(ip, IPGroup.SOM_SIM1, IPGroup.getGroupForIP(ip));
        }
    }

    @Test
    public void testStaffIps() throws IOException {
        BufferedReader staffIps = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "staff-ips.txt")));
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
