package edu.stanford.irt.laneweb.ipgroup;

import java.io.Serializable;
import java.util.StringTokenizer;

public final class IPGroup implements Serializable {

    public static final IPGroup ERR = new IPGroup("ERR");

    public static final IPGroup LPCH = new IPGroup("LPCH");

    public static final IPGroup OTHER = new IPGroup("OTHER");

    public static final IPGroup PAVA = new IPGroup("PAVA");

    public static final IPGroup SHC = new IPGroup("SHC");

    public static final IPGroup SOM_BECKMAN = new IPGroup("SOM_BECKMAN");

    public static final IPGroup SOM_CCSR = new IPGroup("SOM_CCSR");

    public static final IPGroup SOM_CLARK = new IPGroup("SOM_CLARK");

    public static final IPGroup SOM_GRANT = new IPGroup("SOM_GRANT");

    public static final IPGroup SOM_LANE = new IPGroup("SOM_LANE");

    public static final IPGroup SOM_LKSC = new IPGroup("SOM_LKSC");

    public static final IPGroup SOM_OTHER = new IPGroup("SOM_OTHER");

    public static final IPGroup SOM_SIM1 = new IPGroup("SOM_SIM1");

    public static final IPGroup STAFF = new IPGroup("STAFF");

    public static final IPGroup SU = new IPGroup("SU");

    private static final CIDRRange HOSPITAL_NET = new CIDRRange("10.252.0.0/14");

    private static final CIDRRange LOCALNET = new CIDRRange("10.0.0.0/8");

    private static final CIDRRange LPCH_4 = new CIDRRange("159.140.183.0/24");

    private static final CIDRRange LPCH_5 = new CIDRRange("209.11.188.0/22");

    private static final CIDRRange[] LPCH_HOSPITALNET = { new CIDRRange("10.252.1.1/32"), new CIDRRange("10.252.1.2/32"),
            new CIDRRange("10.252.5.0/24"), new CIDRRange("10.252.6.0/23"), new CIDRRange("10.252.10.0/23"),
            new CIDRRange("10.252.18.0/23"), new CIDRRange("10.252.22.0/23"), new CIDRRange("10.252.26.0/23"),
            new CIDRRange("10.252.30.0/23"), new CIDRRange("10.252.34.0/23"), new CIDRRange("10.252.38.0/23"),
            new CIDRRange("10.252.42.0/23"), new CIDRRange("10.252.46.0/23"), new CIDRRange("10.252.50.0/23"),
            new CIDRRange("10.252.54.0/23"), new CIDRRange("10.252.60.0/23"), new CIDRRange("10.252.63.0/23"),
            new CIDRRange("10.252.64.12/32"), new CIDRRange("10.252.64.23/32"), new CIDRRange("10.252.64.46/32"),
            new CIDRRange("10.252.64.60/32"), new CIDRRange("10.252.64.64/32"), new CIDRRange("10.252.64.71/32"),
            new CIDRRange("10.252.65.5/32"), new CIDRRange("10.252.65.6/32"), new CIDRRange("10.252.65.9/32"),
            new CIDRRange("10.252.65.13/32"), new CIDRRange("10.252.65.14/32"), new CIDRRange("10.252.65.17/32"),
            new CIDRRange("10.252.65.18/32"), new CIDRRange("10.252.65.41/32"), new CIDRRange("10.252.65.42/32"),
            new CIDRRange("10.252.65.141/32"), new CIDRRange("10.252.65.142/32"), new CIDRRange("10.252.65.145/32"),
            new CIDRRange("10.252.65.146/32"), new CIDRRange("10.252.66.5/32"), new CIDRRange("10.252.66.6/32"),
            new CIDRRange("10.252.66.49/32"), new CIDRRange("10.252.66.50/32"), new CIDRRange("10.252.66.233/32"),
            new CIDRRange("10.252.66.234/32"), new CIDRRange("10.252.66.249/32"), new CIDRRange("10.252.66.250/32"),
            new CIDRRange("10.252.66.253/32"), new CIDRRange("10.252.66.254/32"), new CIDRRange("10.252.67.0/24"),
            new CIDRRange("10.252.70.0/23"), new CIDRRange("10.252.84.0/23"), new CIDRRange("10.252.107.0/24"),
            new CIDRRange("10.252.108.0/24"), new CIDRRange("10.252.109.0/24"), new CIDRRange("10.252.122.0/24"),
            new CIDRRange("10.252.123.0/24"), new CIDRRange("10.252.138.0/23"), new CIDRRange("10.252.140.0/22"),
            new CIDRRange("10.252.144.0/22"), new CIDRRange("10.252.147.0/24"), new CIDRRange("10.252.148.0/24"),
            new CIDRRange("10.252.149.0/24"), new CIDRRange("10.252.152.0/24"), new CIDRRange("10.252.153.0/24"),
            new CIDRRange("10.252.200.0/24"), new CIDRRange("10.252.250.97/32"), new CIDRRange("10.252.250.98/32"),
            new CIDRRange("10.252.250.99/32"), new CIDRRange("10.252.250.100/30"), new CIDRRange("10.252.250.104/29"),
            new CIDRRange("10.252.250.112/29"), new CIDRRange("10.252.250.120/30"), new CIDRRange("10.252.250.124/31"),
            new CIDRRange("10.252.250.126/32"), new CIDRRange("10.253.220.0/24"), new CIDRRange("10.253.254.0/23") };

    private static final CIDRRange[] LPCH_LOCALNET = { new CIDRRange("10.50.0.0/16"), new CIDRRange("10.250.128.0/17"),
            new CIDRRange("10.251.128.0/17") };

    private static final CIDRRange PAVA_1 = new CIDRRange("152.130.10.128/32");

    private static final CIDRRange PAVA_2 = new CIDRRange("152.131.10.128/32");

    private static final CIDRRange PAVA_3 = new CIDRRange("152.132.10.128/32");

    private static final CIDRRange PAVA_4 = new CIDRRange("152.133.10.128/32");

    private static final long serialVersionUID = 1L;

    private static final CIDRRange SHC_5 = new CIDRRange("204.161.120.0/24");

    private static final CIDRRange SHC_6 = new CIDRRange("209.11.184.0/22");

    private static final CIDRRange[] SHC_LOCALNET = { new CIDRRange("10.247.0.0/16"), new CIDRRange("10.248.0.0/16"),
            new CIDRRange("10.250.0.0/17"), new CIDRRange("10.251.0.0/17") };

    private static final CIDRRange[] SOM_BECKMAN_RANGES = { new CIDRRange("171.65.20.0/22"), new CIDRRange("171.65.24.0/22") };

    private static final CIDRRange[] SOM_CCSR_RANGES = { new CIDRRange("171.65.4.0/22"), new CIDRRange("171.65.8.0/21") };

    private static final CIDRRange[] SOM_CLARK_RANGES = { new CIDRRange("171.65.71.0/25"), new CIDRRange("171.65.92.0/22"),
            new CIDRRange("171.65.102.0/23") };

    private static final CIDRRange[] SOM_GRANT_RANGES = { new CIDRRange("171.65.40.64/28"), new CIDRRange("171.65.88.0/22") };

    private static final CIDRRange SOM_LANE_RANGE = new CIDRRange("171.65.82.0/23");

    private static final CIDRRange SOM_LKSC_RANGE = new CIDRRange("171.65.164.0/22");

    private static final CIDRRange[] SOM_LPCH_RANGES = { new CIDRRange("171.65.112.0/24"), new CIDRRange("171.65.126.0/24") };

    private static final CIDRRange SOM_NET = new CIDRRange("171.65.0.0/16");

    private static final CIDRRange SOM_PAVA_RANGE = new CIDRRange("171.65.44.0/24");

    private static final CIDRRange SOM_PROXY_SERVER = new CIDRRange("171.65.65.46/32");

    private static final CIDRRange[] SOM_SHC_RANGES = { new CIDRRange("171.65.46.0/23"), new CIDRRange("171.65.115.0/24"),
            new CIDRRange("171.65.125.0/24"), new CIDRRange("171.65.127.0/24"), new CIDRRange("171.65.128.0/19"),
            new CIDRRange("171.65.168.0/21"), new CIDRRange("171.65.176.0/20"), new CIDRRange("171.65.192.0/18") };

    private static final CIDRRange SOM_SIM1_RANGE = new CIDRRange("171.65.160.0/22");

    private static final CIDRRange[] STAFF_RANGES = { new CIDRRange("171.65.1.213/32"), new CIDRRange("171.65.82.11/32"),
            new CIDRRange("171.65.82.15/32"), new CIDRRange("171.65.82.16/32"), new CIDRRange("171.65.82.20/32"),
            new CIDRRange("171.65.82.24/32"), new CIDRRange("171.65.82.42/32"), new CIDRRange("171.65.82.61/32"),
            new CIDRRange("171.65.82.68/32"), new CIDRRange("171.65.82.74/32"), new CIDRRange("171.65.82.89/32"),
            new CIDRRange("171.65.82.95/32"), new CIDRRange("171.65.82.98/32"), new CIDRRange("171.65.82.105/32"),
            new CIDRRange("171.65.82.109/32"), new CIDRRange("171.65.82.112/32"), new CIDRRange("171.65.82.133/32"),
            new CIDRRange("171.65.82.141/32"), new CIDRRange("171.65.82.143/32"), new CIDRRange("171.65.82.144/32"),
            new CIDRRange("171.65.82.166/32"), new CIDRRange("171.65.82.168/32"), new CIDRRange("171.65.82.179/32"),
            new CIDRRange("171.65.82.204/32"), new CIDRRange("171.65.82.207/32"), new CIDRRange("171.65.82.212/32"),
            new CIDRRange("171.65.82.217/32"), new CIDRRange("171.65.82.218/32"), new CIDRRange("171.65.82.232/32"),
            new CIDRRange("171.65.82.234/32"), new CIDRRange("171.65.82.237/32"), new CIDRRange("171.65.82.239/32"),
            new CIDRRange("171.65.83.8/32"), new CIDRRange("171.65.83.32/32"), new CIDRRange("171.65.83.45/32"),
            new CIDRRange("171.65.83.73/32"), new CIDRRange("171.65.83.84/32"), new CIDRRange("171.65.83.86/32"),
            new CIDRRange("171.65.83.98/32"), new CIDRRange("171.65.83.114/32"), new CIDRRange("171.65.83.132/32"),
            new CIDRRange("171.65.83.157/32"), new CIDRRange("171.65.83.160/32"), new CIDRRange("171.65.83.168/32"),
            new CIDRRange("171.65.83.169/32"), new CIDRRange("171.65.83.172/32"), new CIDRRange("171.65.83.174/32"),
            new CIDRRange("171.65.83.189/32"), new CIDRRange("171.65.83.203/32"), new CIDRRange("171.65.83.204/32"),
            new CIDRRange("171.65.83.205/32"), new CIDRRange("171.65.83.206/32"), new CIDRRange("171.65.83.208/32"),
            new CIDRRange("171.65.83.209/32"), new CIDRRange("171.65.83.212/32"), new CIDRRange("171.65.83.213/32"),
            new CIDRRange("171.65.83.214/32"), new CIDRRange("171.65.83.215/32"), new CIDRRange("171.65.83.232/32") };

    private static final CIDRRange SU_1 = new CIDRRange("128.12.0.0/16");

    private static final CIDRRange SU_2 = new CIDRRange("134.79.0.0/16");

    private static final CIDRRange SUNET = new CIDRRange("171.64.0.0/14");

    public static IPGroup getGroupForIP(final String ip) {
        int addr = ipToInt(ip);
        if (LOCALNET.contains(addr)) {
            return getGroupForLocalNet(addr);
        } else if (SU_1.contains(addr) || SU_2.contains(addr)) {
            return SU;
        } else if (PAVA_1.contains(addr) || PAVA_2.contains(addr) || PAVA_3.contains(addr) || PAVA_4.contains(addr)) {
            return PAVA;
        } else if (LPCH_4.contains(addr) || LPCH_5.contains(addr)) {
            return LPCH;
        } else if (SUNET.contains(addr)) {
            return getGroupForSUNet(addr);
        } else if (SHC_5.contains(addr) || SHC_6.contains(addr)) {
            return SHC;
        }
        return OTHER;
    }

    private static IPGroup getGroupForHospitalNet(final int addr) {
        for (CIDRRange element : LPCH_HOSPITALNET) {
            if (element.contains(addr)) {
                return LPCH;
            }
        }
        return SHC;
    }

    private static IPGroup getGroupForLocalNet(final int addr) {
        if (HOSPITAL_NET.contains(addr)) {
            return getGroupForHospitalNet(addr);
        }
        for (CIDRRange element : SHC_LOCALNET) {
            if (element.contains(addr)) {
                return SHC;
            }
        }
        for (CIDRRange element : LPCH_LOCALNET) {
            if (element.contains(addr)) {
                return LPCH;
            }
        }
        return SU;
    }

    private static IPGroup getGroupForSOMNet(final int addr) {
        for (CIDRRange element : STAFF_RANGES) {
            if (element.contains(addr)) {
                return STAFF;
            }
        }
        for (CIDRRange element : SOM_CCSR_RANGES) {
            if (element.contains(addr)) {
                return SOM_CCSR;
            }
        }
        for (CIDRRange element : SOM_BECKMAN_RANGES) {
            if (element.contains(addr)) {
                return SOM_BECKMAN;
            }
        }
        for (CIDRRange element : SOM_GRANT_RANGES) {
            if (element.contains(addr)) {
                return SOM_GRANT;
            }
        }
        if (SOM_PAVA_RANGE.contains(addr)) {
            return PAVA;
        }
        for (CIDRRange element : SOM_SHC_RANGES) {
            if (element.contains(addr)) {
                return SHC;
            }
        }
        for (CIDRRange element : SOM_CLARK_RANGES) {
            if (element.contains(addr)) {
                return SOM_CLARK;
            }
        }
        if (SOM_LANE_RANGE.contains(addr)) {
            return SOM_LANE;
        }
        for (CIDRRange element : SOM_LPCH_RANGES) {
            if (element.contains(addr)) {
                return LPCH;
            }
        }
        if (SOM_SIM1_RANGE.contains(addr)) {
            return SOM_SIM1;
        }
        if (SOM_LKSC_RANGE.contains(addr)) {
            return SOM_LKSC;
        }
        if (SOM_PROXY_SERVER.contains(addr)) {
            return OTHER;
        }
        return SOM_OTHER;
    }

    private static IPGroup getGroupForSUNet(final int addr) {
        if (SOM_NET.contains(addr)) {
            return getGroupForSOMNet(addr);
        }
        return SU;
    }

    private static int ipToInt(final String ip) {
        StringTokenizer st = new StringTokenizer(ip, ".");
        return ((Integer.parseInt(st.nextToken()) << 24) & 0xFF000000) | ((Integer.parseInt(st.nextToken()) << 16) & 0xFF0000)
                | ((Integer.parseInt(st.nextToken()) << 8) & 0xFF00) | (Integer.parseInt(st.nextToken()) & 0xFF);
    }

    private String stringValue;

    private IPGroup(final String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
