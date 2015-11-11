package edu.stanford.irt.laneweb.ipgroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * CIDRRange represents a range of ip addresses using CIDR notation in the constructor.
 */
public class CIDRRange {

    private String cidr;

    private long highest;

    private IPGroup ipGroup;

    private long lowest;

    private List<CIDRRange> subranges;

    /**
     * create a new CIDRRange
     *
     * @param cidr
     *            with the format "nnn.nnn.nnn.nnn/nn"
     */
    public CIDRRange(final String cidr) {
        this.cidr = cidr;
        this.subranges = new ArrayList<CIDRRange>();
        String ip = cidr.substring(0, cidr.indexOf('/'));
        int addr = ipToInt(ip);
        int mask = (-1) << (32 - Integer.parseInt(cidr.substring(ip.length() + 1)));
        this.lowest = addr & mask;
        this.highest = this.lowest + (~mask);
    }

    /**
     * create a new CIDRRange
     *
     * @param cidr
     *            with the format "nnn.nnn.nnn.nnn/nn"
     * @param ipGroup
     *            the IPGroup associated with this range
     */
    public CIDRRange(final String cidr, final IPGroup ipGroup) {
        this(cidr);
        this.ipGroup = ipGroup;
    }

    /**
     * add a CIDRRange as a subrange of this one.
     *
     * @param other
     *            the subrange to add
     */
    public void addSubrange(final CIDRRange other) {
        if (!isSubrange(other)) {
            throw new LanewebException(other + " is not a subrange of " + this);
        }
        // first see if this is a subrange of any of the current subranges:
        CIDRRange subrange = getSubrangeContaining(other);
        if (subrange != null) {
            subrange.addSubrange(other);
        } else {
            // then see if this is a parent range of any of the current ranges
            for (CIDRRange containedBy : getSubrangesContainedBy(other)) {
                this.subranges.remove(containedBy);
                other.addSubrange(containedBy);
            }
            this.subranges.add(other);
        }
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

    /**
     * determine if an address is within this CDIRRanges range.
     *
     * @param ip
     *            the String value of an ip address
     * @return true if the ip is in range, otherwise false
     */
    public boolean contains(final String ip) {
        try {
            return contains(ipToInt(ip));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * get the IPGroup associated with an ip or null if not in range.
     *
     * @param ip
     *            the int value of an ip address
     * @return the IPGroup or null
     */
    public IPGroup getIPGroup(final int ip) {
        IPGroup result = null;
        if (contains(ip)) {
            for (CIDRRange subrange : this.subranges) {
                result = subrange.getIPGroup(ip);
                if (result != null) {
                    break;
                }
            }
            if (result == null) {
                result = this.ipGroup;
            }
        }
        return result;
    }

    /**
     * get the IPGroup associated with an ip or null if not in range.
     *
     * @param ip
     *            the String value of an ip address
     * @return the IPGroup or null
     */
    public IPGroup getIPGroup(final String ip) {
        try {
            return getIPGroup(ipToInt(ip));
        } catch (NumberFormatException e) {
            return IPGroup.ERR;
        }
    }

    /**
     * Determine of another range is contained within this range
     *
     * @param other
     *            the other CIDRange
     * @return true if other is within this range, otherwise false
     */
    public boolean isSubrange(final CIDRRange other) {
        return other.lowest >= this.lowest && other.highest <= this.highest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("cidr=").append(this.cidr).append(" ipGroup=").append(this.ipGroup)
                .append(" subranges=").append(this.subranges);
        return sb.toString();
    }

    private CIDRRange getSubrangeContaining(final CIDRRange other) {
        for (CIDRRange subrange : this.subranges) {
            if (subrange.isSubrange(other)) {
                return subrange;
            }
        }
        return null;
    }

    private List<CIDRRange> getSubrangesContainedBy(final CIDRRange other) {
        List<CIDRRange> containedBy = new LinkedList<CIDRRange>();
        for (CIDRRange subrange : this.subranges) {
            if (other.isSubrange(subrange)) {
                containedBy.add(subrange);
            }
        }
        return containedBy;
    }

    private int ipToInt(final String ip) {
        StringTokenizer st = new StringTokenizer(ip, ".");
        return ((Integer.parseInt(st.nextToken()) << 24) & 0xFF000000)
                | ((Integer.parseInt(st.nextToken()) << 16) & 0xFF0000)
                | ((Integer.parseInt(st.nextToken()) << 8) & 0xFF00) | (Integer.parseInt(st.nextToken()) & 0xFF);
    }
}
