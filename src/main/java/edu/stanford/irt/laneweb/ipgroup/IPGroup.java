package edu.stanford.irt.laneweb.ipgroup;

import java.io.Serializable;

public final class IPGroup implements Serializable {

    public static final IPGroup ERR = new IPGroup("ERR");

    public static final IPGroup LPCH = new IPGroup("LPCH");

    public static final IPGroup OTHER = new IPGroup("OTHER");

    public static final IPGroup PAVA = new IPGroup("PAVA");

    public static final IPGroup SHC = new IPGroup("SHC");

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
        ROOT.addSubrange(new CIDRRange("45.42.34.136/32", LPCH));
        ROOT.addSubrange(new CIDRRange("68.65.248.136/32", LPCH));
        ROOT.addSubrange(new CIDRRange("160.109.100.132/32", LPCH));
        ROOT.addSubrange(new CIDRRange("199.68.152.135/32", LPCH));
        ROOT.addSubrange(new CIDRRange("199.68.152.137/32", LPCH));
        ROOT.addSubrange(new CIDRRange("199.68.152.139/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.232.0.0/13", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.1.1/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.1.2/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.5.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.6.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.10.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.14.0/23", LPCH));
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
        ROOT.addSubrange(new CIDRRange("10.252.62.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.12/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.17/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.23/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.41/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.46/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.60/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.69/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.64/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.64.71/32", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.12/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.40/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.144/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.65.164/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.4/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.48/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.232/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.248/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.66.252/30", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.67.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.70.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.84.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.97.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.107.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.108.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.109.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.122.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.123.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.138.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.140.0/22", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.144.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.146.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.147.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.148.0/23", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.152.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.153.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.200.0/24", LPCH));
        ROOT.addSubrange(new CIDRRange("10.252.250.96/27", LPCH));
        ROOT.addSubrange(new CIDRRange("10.50.0.0/16", LPCH));
        ROOT.addSubrange(new CIDRRange("10.250.128.0/17", LPCH));
        ROOT.addSubrange(new CIDRRange("10.251.128.0/17", LPCH));
        ROOT.addSubrange(new CIDRRange("152.130.10.128/32", PAVA));
        ROOT.addSubrange(new CIDRRange("152.131.10.128/32", PAVA));
        ROOT.addSubrange(new CIDRRange("152.132.10.128/32", PAVA));
        ROOT.addSubrange(new CIDRRange("152.133.10.128/32", PAVA));
        ROOT.addSubrange(new CIDRRange("96.47.48.129/32", SHC));
        ROOT.addSubrange(new CIDRRange("96.47.48.130/32", SHC));
        ROOT.addSubrange(new CIDRRange("96.47.50.129/32", SHC));
        ROOT.addSubrange(new CIDRRange("96.47.50.130/32", SHC));
        ROOT.addSubrange(new CIDRRange("96.47.52.128/28", SHC));
        ROOT.addSubrange(new CIDRRange("96.47.57.128/28", SHC));
        ROOT.addSubrange(new CIDRRange("67.114.200.17/32", SHC));
        ROOT.addSubrange(new CIDRRange("67.114.200.33/32", SHC));
        ROOT.addSubrange(new CIDRRange("108.171.135.165/32", SHC));
        ROOT.addSubrange(new CIDRRange("10.39.8.0/21", SHC));
        ROOT.addSubrange(new CIDRRange("10.39.16.0/22", SHC));
        ROOT.addSubrange(new CIDRRange("10.39.20.0/22", SHC));
        ROOT.addSubrange(new CIDRRange("10.243.0.0/16", SHC));
        ROOT.addSubrange(new CIDRRange("10.247.0.0/16", SHC));
        ROOT.addSubrange(new CIDRRange("10.248.0.0/16", SHC));
        ROOT.addSubrange(new CIDRRange("10.250.0.0/17", SHC));
        ROOT.addSubrange(new CIDRRange("10.251.0.0/17", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.112.0/24", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.116.0/22", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.126.0/25", LPCH));
        ROOT.addSubrange(new CIDRRange("171.65.44.0/24", PAVA));
        ROOT.addSubrange(new CIDRRange("171.65.65.46/32", OTHER));
        ROOT.addSubrange(new CIDRRange("171.65.46.0/23", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.115.0/24", SHC));
        ROOT.addSubrange(new CIDRRange("171.65.127.0/24", SHC));
        ROOT.addSubrange(new CIDRRange("128.12.0.0/16", SU));
        ROOT.addSubrange(new CIDRRange("134.79.0.0/16", SU));
        ROOT.addSubrange(new CIDRRange("171.64.0.0/14", SU));
    }

    private String stringValue;

    protected IPGroup(final String stringValue) {
        this.stringValue = stringValue;
    }

    public static IPGroup getGroupForIP(final String ip) {
        return ROOT.getIPGroup(ip);
    }

    @Override
    public boolean equals(final Object other) {
        boolean equals = false;
        if (other != null && other.getClass() == getClass() && other.hashCode() == hashCode()) {
            return true;
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
