package edu.stanford.irt.laneweb.ipgroup;

import java.io.Serializable;

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

    private static final CIDRRange ROOT = new CIDRRange("0.0.0.0/0", OTHER) {

        @Override
        public boolean contains(final int ip) {
            return true;
        }

        @Override
        public boolean isSubrange(final CIDRRange other) {
            return true;
        }
    };

    private static final long serialVersionUID = 1L;
    static {
        ROOT.addSubrange(new CIDRRange("10.252.0.0/14", SHC));
        ROOT.addSubrange(new CIDRRange("10.0.0.0/8", SU));
        ROOT.addSubrange(new CIDRRange("159.140.183.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("209.11.188.0/22", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.1.1/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.1.2/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.5.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.6.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.10.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.18.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.22.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.26.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.30.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.34.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.38.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.42.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.46.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.50.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.54.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.60.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.63.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.12/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.23/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.46/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.60/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.64/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.71/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.5/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.6/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.9/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.13/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.14/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.17/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.18/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.41/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.42/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.141/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.142/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.145/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.146/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.5/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.6/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.49/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.50/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.233/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.234/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.249/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.250/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.253/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.254/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.67.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.70.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.84.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.107.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.108.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.109.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.122.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.123.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.138.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.140.0/22", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.144.0/22", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.147.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.148.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.149.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.152.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.153.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.200.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.97/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.98/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.99/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.100/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.104/29", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.112/29", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.120/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.124/31", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.126/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.253.220.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.253.254.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.50.0.0/16", LPCH));
        ROOT.addSubrange(new CIDRRange("10.250.128.0/17", LPCH));
        ROOT.addSubrange(new CIDRRange("10.251.128.0/17", LPCH));
        ROOT.addSubrange(new CIDRRange("152.130.10.128/32", PAVA));
        ROOT.addSubrange(new CIDRRange("152.131.10.128/32", PAVA));
        ROOT.addSubrange(new CIDRRange("152.132.10.128/32", PAVA));
        ROOT.addSubrange(new CIDRRange("152.133.10.128/32", PAVA));
        ROOT.addSubrange(new CIDRRange("204.161.120.0/24", SHC));
        ROOT.addSubrange(new CIDRRange("209.11.184.0/22", SHC));
        ROOT.addSubrange(new CIDRRange("10.247.0.0/16", SHC));
        ROOT.addSubrange(new CIDRRange("10.248.0.0/16", SHC));
        ROOT.addSubrange(new CIDRRange("10.250.0.0/17", SHC));
        ROOT.addSubrange(new CIDRRange("10.251.0.0/17", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.20.0/22", SOM_BECKMAN));
        ROOT.addSubrange(new CIDRRange("171.65.24.0/22", SOM_BECKMAN));
        ROOT.addSubrange(new CIDRRange("171.65.4.0/22", SOM_CCSR));
        ROOT.addSubrange(new CIDRRange("171.65.8.0/21", SOM_CCSR));
        ROOT.addSubrange(new CIDRRange("171.65.71.0/25", SOM_CLARK));
        ROOT.addSubrange(new CIDRRange("171.65.92.0/22", SOM_CLARK));
        ROOT.addSubrange(new CIDRRange("171.65.102.0/23", SOM_CLARK));
        ROOT.addSubrange(new CIDRRange("171.65.40.64/28", SOM_GRANT));
        ROOT.addSubrange(new CIDRRange("171.65.88.0/22", SOM_GRANT));
        ROOT.addSubrange(new CIDRRange("171.65.82.0/23", SOM_LANE));
        ROOT.addSubrange(new CIDRRange("171.65.164.0/22", SOM_LKSC));
        ROOT.addSubrange(new CIDRRange("171.65.112.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("171.65.126.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("171.65.0.0/16", SOM_OTHER));
        ROOT.addSubrange(new CIDRRange("171.65.44.0/24", PAVA));
        ROOT.addSubrange(new CIDRRange("171.65.65.46/32", OTHER));
        ROOT.addSubrange(new CIDRRange("171.65.46.0/23", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.115.0/24", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.125.0/24", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.127.0/24", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.128.0/19", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.168.0/21", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.176.0/20", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.192.0/18", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.160.0/22", SOM_SIM1));
        ROOT.addSubrange(new CIDRRange("171.65.1.213/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.11/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.15/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.21/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.24/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.42/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.56/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.68/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.74/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.89/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.92/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.95/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.98/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.109/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.112/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.141/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.143/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.144/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.163/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.166/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.168/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.179/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.207/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.212/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.217/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.218/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.232/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.234/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.237/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.239/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.240/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.82.253/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.3/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.4/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.8/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.10/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.28/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.35/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.73/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.84/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.86/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.98/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.114/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.132/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.157/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.168/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.169/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.172/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.174/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.189/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.205/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.206/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.209/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.213/32", STAFF));
        ROOT.addSubrange(new CIDRRange("171.65.83.232/32", STAFF));
        ROOT.addSubrange(new CIDRRange("128.12.0.0/16", SU));
        ROOT.addSubrange(new CIDRRange("134.79.0.0/16", SU));
        ROOT.addSubrange(new CIDRRange("171.64.0.0/14", SU));
    }

    public static IPGroup getGroupForIP(final String ip) {
        return ROOT.getIPGroup(ip);
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
