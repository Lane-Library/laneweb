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
        ROOT.addSubrange(new CIDRRange("45.42.34.136/32", LPCH));
        ROOT.addSubrange(new CIDRRange("68.65.248.136/32", LPCH));
        ROOT.addSubrange(new CIDRRange("160.109.100.132/32", LPCH));
        ROOT.addSubrange(new CIDRRange("199.68.152.135/32", LPCH));
        ROOT.addSubrange(new CIDRRange("199.68.152.137/32", LPCH));
        ROOT.addSubrange(new CIDRRange("199.68.152.139/32", LPCH));
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
        return (other != null && other.getClass() == getClass() && other.hashCode() == hashCode());
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
