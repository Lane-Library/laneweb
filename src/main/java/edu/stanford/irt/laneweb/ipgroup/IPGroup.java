/**
 * 
 */
package edu.stanford.irt.laneweb.ipgroup;

import java.util.regex.Pattern;

/**
 * @author ceyates
 */
public final class IPGroup {

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

    public static final IPGroup SOM_SIM1 = new IPGroup("SOM_SIM1");
    
    public static final IPGroup SOM_OTHER = new IPGroup("SOM_OTHER");
    
    public static final IPGroup STAFF = new IPGroup("STAFF");

    public static final IPGroup SU = new IPGroup("SU");

    private static final String _0_TO_255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    private static final String DOT = "\\.";

    private static final Pattern IP_ADDRESS = Pattern.compile("^" + _0_TO_255 + DOT + _0_TO_255 + DOT + _0_TO_255 + DOT + _0_TO_255 + "$");

    private static final Pattern LPCH_159_PATTERN = Pattern.compile("^159" + DOT + "140" + DOT + "183" + DOT + _0_TO_255 + "$");

    private static final String LPCH_252_1 = "(?:5|6|7|10|11|18|19|22|23|26|27|30|31|34|35|38|39|42|43|46|47|50|51|54|55|60|61|62|63|67|70|71|84|85|107|108|109|122|123|13[89]|14[0-9]|15[23]|200)"
            + DOT + _0_TO_255;

    private static final String LPCH_252_2 = "250" + DOT + "(?:0?9[7-9]|1[01][0-9]|12[0-6])";

    private static final String LPCH_252_3 = "1" + DOT + "[12]";

    private static final String LPCH_252_4 = "64" + DOT + "(?:12|23|46|60|64|71)";

    private static final String LPCH_252_5 = "65" + DOT + "(?:5|6|9|13|14|17|18|41|42|141|142|145|146)";

    private static final String LPCH_252_6 = "66" + DOT + "(?:5|6|49|50|233|234|249|250|253|254)";

    private static final String LPCH_1 = "(?:50" + DOT + _0_TO_255 + DOT + _0_TO_255 + "|25[01]" + DOT + "(?:25[0-5]|2[0-4][0-9]|1[3-9][0-9]|12[89])" + DOT
            + _0_TO_255 + ")";

    private static final String LPCH_2 = "252" + DOT + "(?:" + LPCH_252_1 + "|" + LPCH_252_2 + "|" + LPCH_252_3 + "|" + LPCH_252_4 + "|" + LPCH_252_5 + "|"
            + LPCH_252_6 + ")";

    private static final String LPCH_3 = "253" + DOT + "2(?:20|54|55)" + DOT + _0_TO_255;

    private static final Pattern LPCH_PATTERN = Pattern.compile("^(?:" + LPCH_1 + "|" + LPCH_2 + "|" + LPCH_3 + ")$");

    private static final Pattern LPCH_209_PATTERN = Pattern.compile("^209" + DOT + "11" + DOT + "(?:18[89]|19[01])" + DOT + _0_TO_255 + "$");

    private static final Pattern NOT_OTHER = Pattern.compile("^(?:10|128|134|152|159|171|204|209)" + DOT + _0_TO_255 + DOT + _0_TO_255 + DOT + _0_TO_255 + "$");

    private static final Pattern PAVA_PATTERN = Pattern.compile("^152" + DOT + "13[0-3]" + DOT + "10" + DOT + "128$");

    private static final Pattern SHC_204_PATTERN = Pattern.compile("^204" + DOT + "161" + DOT + "120.*");

    private static final Pattern SHC_209_PATTERN = Pattern.compile("^209" + DOT + "11" + DOT + "18[4-7].*");

    private static final Pattern SHC_PATTERN = Pattern.compile("^2(?:(?:5[2-5]|4[78])" + DOT + _0_TO_255 + "|5[01]" + DOT + "(?:12[0-7]|1[01][0-9]|[0]?[0-9][0-9]?))"
            + DOT + _0_TO_255 + "$");

    private static final Pattern SOM_BECKMAN_PATTERN = Pattern.compile("^2[0-7]" + DOT + _0_TO_255 + "$");

    private static final Pattern SOM_CCSR_PATTERN = Pattern.compile("^(?:1[0-5]|[4-9])" + DOT + _0_TO_255 + "$");

    private static final String SOM_CLARK_1 = "(?:9[2-5]|10[23])" + DOT + _0_TO_255;

    private static final String SOM_CLARK_2 = "71" + DOT + "(?:12[0-7]|1[01][0-9]|[0-9][0-9]?)";

    private static final Pattern SOM_CLARK_PATTERN = Pattern.compile("^(?:" + SOM_CLARK_1 + "|" + SOM_CLARK_2 + ")$");

    private static final String SOM_GRANT_1 = "(?:8[89]|9[01])" + DOT + _0_TO_255;

    private static final String SOM_GRANT_2 = "^40" + DOT + "(?:7[0-9]|6[4-9])";

    private static final Pattern SOM_GRANT_PATTERN = Pattern.compile("^(?:" + SOM_GRANT_1 + "|" + SOM_GRANT_2 + ")$");

    private static final Pattern SOM_LANE_PATTERN = Pattern.compile("^8[23]" + DOT + _0_TO_255 + "$");

    private static final String SOM_LANE_STAFF_1 = "82" + DOT
            + "(?:11|15|16|20|24|42|61|68|74|89|95|98|105|109|112|133|141|143|144|166|168|179|204|207|212|217|218|232|234|237|239)";

    private static final String SOM_LANE_STAFF_2 = "83" + DOT
            + "(?:8|32|45|73|84|86|98|114|132|157|160|168|169|172|174|189|203|204|205|206|208|209|212|213|214|215|232)";

    private static final String SOM_LANE_STAFF_3 = "1" + DOT + "213";

    private static final Pattern SOM_LANE_STAFF_PATTERN = Pattern.compile("^(?:" + SOM_LANE_STAFF_1 + "|" + SOM_LANE_STAFF_2 + "|" + SOM_LANE_STAFF_3 + ")$");

    private static final Pattern SOM_LPCH_PATTERN = Pattern.compile("^(?:112|126)" + DOT + _0_TO_255 + "$");

    private static final Pattern SOM_LKSC_PATTERN = Pattern.compile("^(?:16[4-7])" + DOT + _0_TO_255 + "$");

    private static final Pattern SOM_PATTERN = Pattern.compile("^171" + DOT + "65" + DOT + _0_TO_255 + DOT + _0_TO_255 + "$");

    private static final Pattern SOM_PAVA_PATTERN = Pattern.compile("^44" + DOT + _0_TO_255 + "$");
    
    private static final Pattern SOM_SIM1_PATTERN = Pattern.compile("^(?:16[0-3])" + DOT + _0_TO_255 + "$");
    
    private static final Pattern EZPROXY_SERVER_PATTERN = Pattern.compile("^65.46$");

    private static final Pattern SOM_SHC_PATTERN = Pattern.compile("^(?:(?:1[12]5|12[7-9]|1[3-57-9][0-9]|16[8-9]|25[0-5]|2[0-4][0-9])|4[6-7])" + DOT + _0_TO_255 + "$");

    private static final Pattern STANFORD_10_PATTERN = Pattern.compile("^10" + DOT + _0_TO_255 + DOT + _0_TO_255 + DOT + _0_TO_255 + "$");

    private static final Pattern STANFORD_128_PATTERN = Pattern.compile("^128" + DOT + "12" + DOT + _0_TO_255 + DOT + _0_TO_255 + "$");

    private static final Pattern STANFORD_134_PATTERN = Pattern.compile("^134" + DOT + "79" + DOT + _0_TO_255 + DOT + _0_TO_255 + "$");

    private static final Pattern STANFORD_171_PATTERN = Pattern.compile("^171" + DOT + "6[4-7]" + DOT + _0_TO_255 + DOT + _0_TO_255 + "$");

    public static IPGroup getGroupForIP(final String ip) {
        if (!IP_ADDRESS.matcher(ip).matches()) {
            return IPGroup.ERR;
        }
        if (!NOT_OTHER.matcher(ip).matches()) {
            return IPGroup.OTHER;
        }
        if (SOM_PATTERN.matcher(ip).matches()) {
            return getGroupForSOMSubnet(ip.substring(7));
        }
        if (STANFORD_10_PATTERN.matcher(ip).matches()) {
            return getGroupForHospitalSubnet(ip.substring(3));
        }
        if (PAVA_PATTERN.matcher(ip).matches()) {
            return IPGroup.PAVA;
        }
        if (STANFORD_171_PATTERN.matcher(ip).matches()) {
            return IPGroup.SU;
        }
        if (STANFORD_128_PATTERN.matcher(ip).matches()) {
            return IPGroup.SU;
        }
        if (SHC_209_PATTERN.matcher(ip).matches()) {
            return IPGroup.SHC;
        }
        if (STANFORD_134_PATTERN.matcher(ip).matches()) {
            return IPGroup.SU;
        }
        if (SHC_204_PATTERN.matcher(ip).matches()) {
            return IPGroup.SHC;
        }
        if (LPCH_209_PATTERN.matcher(ip).matches()) {
            return IPGroup.LPCH;
        }
        if (LPCH_159_PATTERN.matcher(ip).matches()) {
            return IPGroup.LPCH;
        }
        return IPGroup.OTHER;
    }

    private static IPGroup getGroupForHospitalSubnet(final String ip) {
        if (LPCH_PATTERN.matcher(ip).matches()) {
            return IPGroup.LPCH;
        }
        if (SHC_PATTERN.matcher(ip).matches()) {
            return IPGroup.SHC;
        }
        return IPGroup.SU;
    }

    private static IPGroup getGroupForSOMSubnet(final String ip) {
        if (SOM_LANE_STAFF_PATTERN.matcher(ip).matches()) {
            return IPGroup.STAFF;
        }
        if (SOM_LANE_PATTERN.matcher(ip).matches()) {
            return IPGroup.SOM_LANE;
        }
        if (SOM_CCSR_PATTERN.matcher(ip).matches()) {
            return IPGroup.SOM_CCSR;
        }
        if (SOM_BECKMAN_PATTERN.matcher(ip).matches()) {
            return IPGroup.SOM_BECKMAN;
        }
        if (SOM_GRANT_PATTERN.matcher(ip).matches()) {
            return IPGroup.SOM_GRANT;
        }
        if (SOM_CLARK_PATTERN.matcher(ip).matches()) {
            return IPGroup.SOM_CLARK;
        }
        if (SOM_LPCH_PATTERN.matcher(ip).matches()) {
            return IPGroup.LPCH;
        }
        if (SOM_LKSC_PATTERN.matcher(ip).matches()) {
            return IPGroup.SOM_LKSC;
        }
        if (SOM_PAVA_PATTERN.matcher(ip).matches()) {
            return IPGroup.PAVA;
        }
        if (SOM_SHC_PATTERN.matcher(ip).matches()) {
            return IPGroup.SHC;
        }
        if (SOM_SIM1_PATTERN.matcher(ip).matches()) {
            return IPGroup.SOM_SIM1;
        }
        if (EZPROXY_SERVER_PATTERN.matcher(ip).matches()) {
            return IPGroup.OTHER;
        }
        return IPGroup.SOM_OTHER;
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
