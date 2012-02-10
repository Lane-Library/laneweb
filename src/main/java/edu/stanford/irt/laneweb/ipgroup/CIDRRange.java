package edu.stanford.irt.laneweb.ipgroup;

import java.util.StringTokenizer;

/**
 * CIDRRange represents a range of ip addresses using CIDR notation in the
 * constructor.
 */
public class CIDRRange {

    private int highest;

    private int lowest;

    /**
     * create a new CIDRRange
     * 
     * @param cidr
     *            with the format "nnn.nnn.nnn.nnn/nn"
     */
    public CIDRRange(final String cidr) {
        String ip = cidr.substring(0, cidr.indexOf('/'));
        int addr = ipToInt(ip);
        int mask = (-1) << (32 - Integer.parseInt(cidr.substring(ip.length() + 1)));
        this.lowest = addr & mask;
        this.highest = this.lowest + (~mask);
    }

    /**
     * determine if an address with in this CDIRRanges range.
     * 
     * @param ip
     *            the 32 bit integer representation of an ip address
     * @return true if the ip is in range, otherwise false
     */
    public boolean contains(final int ip) {
        return ip >= this.lowest && ip <= this.highest;
    }

    private int ipToInt(final String ip) {
        StringTokenizer st = new StringTokenizer(ip, ".");
        return ((Integer.parseInt(st.nextToken()) << 24) & 0xFF000000) | ((Integer.parseInt(st.nextToken()) << 16) & 0xFF0000)
                | ((Integer.parseInt(st.nextToken()) << 8) & 0xFF00) | (Integer.parseInt(st.nextToken()) & 0xFF);
    }
}
