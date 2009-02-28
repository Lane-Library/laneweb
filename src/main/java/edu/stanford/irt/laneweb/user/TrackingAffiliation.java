/**
 * 
 */
package edu.stanford.irt.laneweb.user;

import java.util.StringTokenizer;

/**
 * @author ceyates
 */
public class TrackingAffiliation {

    public static final TrackingAffiliation ERR = new TrackingAffiliation("ERR");

    public static final TrackingAffiliation LANE = new TrackingAffiliation("LANE");

    public static final TrackingAffiliation LPCH = new TrackingAffiliation("LPCH");

    public static final TrackingAffiliation OTHER = new TrackingAffiliation("OTHER");

    public static final TrackingAffiliation PAVA = new TrackingAffiliation("PAVA");

    public static final TrackingAffiliation SHC = new TrackingAffiliation("SHC");

    public static final TrackingAffiliation SOM = new TrackingAffiliation("SOM");

    public static final TrackingAffiliation STAFF = new TrackingAffiliation("STAFF");

    public static final TrackingAffiliation SU = new TrackingAffiliation("SU");

    public static TrackingAffiliation getAffiliationForIP(final String ip) {
        int[] d = new int[4];
        int index = 0;
        for (StringTokenizer tokenizer = new StringTokenizer(ip, "."); tokenizer.hasMoreTokens();) {
            try {
                d[index++] = Integer.parseInt(tokenizer.nextToken());
            } catch (NumberFormatException e) {
                return TrackingAffiliation.ERR;
            }
        }
        if (d[0] == 10) {
            if (d[1] == 50) {
                return TrackingAffiliation.LPCH;
            }
            if ((d[1] == 247) || (d[1] == 248)) {
                return TrackingAffiliation.SHC;
            }
            if ((d[1] == 250) || (d[1] == 251)) {
                if (d[2] < 128) {
                    return TrackingAffiliation.SHC;
                }
                return TrackingAffiliation.LPCH;
            }
            if (d[1] == 252) {
                if ((d[2] == 1) && ((d[3] == 1) || (d[3] == 2))) {
                    return TrackingAffiliation.LPCH;
                }
                if ((d[2] == 5) || (d[2] == 6) || (d[2] == 7) || (d[2] == 10) || (d[2] == 11) || (d[2] == 18) || (d[2] == 19) || (d[2] == 22) || (d[2] == 23)
                        || (d[2] == 26) || (d[2] == 27) || (d[2] == 30) || (d[2] == 31) || (d[2] == 34) || (d[2] == 35) || (d[2] == 38) || (d[2] == 39)
                        || (d[2] == 42) || (d[2] == 43) || (d[2] == 46) || (d[2] == 47) || (d[2] == 50) || (d[2] == 51) || (d[2] == 54) || (d[2] == 55)
                        || (d[2] == 60) || (d[2] == 61) || (d[2] == 62) || (d[2] == 63)) {
                    return TrackingAffiliation.LPCH;
                }
                if ((d[2] == 64) && ((d[3] == 12) || (d[3] == 23) || (d[3] == 46) || (d[3] == 60) || (d[3] == 64) || (d[3] == 71))) {
                    return TrackingAffiliation.LPCH;
                }
                if ((d[2] == 65)
                        && ((d[3] == 5) || (d[3] == 6) || (d[3] == 9) || (d[3] == 13) || (d[3] == 14) || (d[3] == 17) || (d[3] == 18) || (d[3] == 41)
                                || (d[3] == 42) || (d[3] == 141) || (d[3] == 142) || (d[3] == 145) || (d[3] == 146))) {
                    return TrackingAffiliation.LPCH;
                }
                if ((d[2] == 66)
                        && ((d[3] == 5) || (d[3] == 6) || (d[3] == 49) || (d[3] == 50) || (d[3] == 233) || (d[3] == 234) || (d[3] == 249) || (d[3] == 250)
                                || (d[3] == 253) || (d[3] == 254))) {
                    return TrackingAffiliation.LPCH;
                }
                if ((d[2] == 67) || (d[2] == 70) || (d[2] == 71) || (d[2] == 84) || (d[2] == 85) || (d[2] == 107) || (d[2] == 108) || (d[2] == 109)
                        || (d[2] == 122) || (d[2] == 123)) {
                    return TrackingAffiliation.LPCH;
                }
                if ((d[2] >= 138) && (d[2] <= 149)) {
                    return TrackingAffiliation.LPCH;
                }
                if ((d[2] == 152) || (d[2] == 153) || (d[2] == 200)) {
                    return TrackingAffiliation.LPCH;
                }
                if (d[2] == 250) {
                    if ((d[3] >= 97) && (d[3] <= 126)) {
                        return TrackingAffiliation.LPCH;
                    }
                }
                return TrackingAffiliation.SHC;
            }
            if (d[1] == 253) {
                if ((d[2] == 220) || (d[2] == 254) || (d[2] == 255)) {
                    return TrackingAffiliation.LPCH;
                }
                return TrackingAffiliation.SHC;
            }
            if (d[1] > 253) {
                return TrackingAffiliation.SHC;
            }
            return TrackingAffiliation.OTHER;
        }
        if (d[0] == 128) {
            if (d[1] == 12) {
                return TrackingAffiliation.SU;
            }
            return TrackingAffiliation.OTHER;
        }
        if (d[0] == 134) {
            if (d[1] == 79) {
                return TrackingAffiliation.SU;
            }
            return TrackingAffiliation.OTHER;
        }
        if ((d[0] == 152) && (d[2] == 10) && (d[3] == 128) && (d[1] >= 130) && (d[1] <= 133)) {
            return TrackingAffiliation.PAVA;
        }
        if ((d[0] == 159) && (d[1] == 140) && (d[2] == 183)) {
            return TrackingAffiliation.LPCH;
        }
        if (d[0] == 171) {
            if (d[1] == 64) {
                return TrackingAffiliation.SU;
            }
            if (d[1] == 65) {
                if ((d[2] == 1) && (d[3] == 213)) {
                    return TrackingAffiliation.STAFF;
                }
                if ((d[2] >= 0) && (d[2] <= 43)) {
                    return TrackingAffiliation.SOM;
                }
                if ((d[2] >= 44) && (d[2] <= 47)) {
                    return TrackingAffiliation.SHC;
                }
                if ((d[2] >= 48) && (d[2] <= 81)) {
                    return TrackingAffiliation.SOM;
                }
                if (d[2] == 82) {
                    if ((d[3] == 11) || (d[3] == 15) || (d[3] == 16) || (d[3] == 20) || (d[3] == 24) || (d[3] == 25) || (d[3] == 27) || (d[3] == 31)
                            || (d[3] == 42) || (d[3] == 43) || (d[3] == 46) || (d[3] == 47) || (d[3] == 48) || (d[3] == 59) || (d[3] == 60) || (d[3] == 68)
                            || (d[3] == 74) || (d[3] == 78) || (d[3] == 95) || (d[3] == 105) || (d[3] == 119) || (d[3] == 129) || (d[3] == 135)
                            || (d[3] == 143) || (d[3] == 144) || (d[3] == 166) || (d[3] == 168) || (d[3] == 179) || (d[3] == 204) || (d[3] == 232)) {
                        return TrackingAffiliation.STAFF;
                    }
                    return TrackingAffiliation.SOM;
                }
                if (d[2] == 83) {
                    if (((d[3] == 8) || (d[3] == 32)) || (d[3] == 37) || (d[3] == 45) || (d[3] == 73) || (d[3] == 84) || (d[3] == 86) || (d[3] == 96)
                            || (d[3] == 98) || (d[3] == 114) || (d[3] == 132) || (d[3] == 149) || (d[3] == 157) || (d[3] == 160) || (d[3] == 168)
                            || (d[3] == 172) || (d[3] == 174) || (d[3] == 203) || (d[3] == 204) || (d[3] == 205) || (d[3] == 206) || (d[3] == 208)
                            || (d[3] == 212) || (d[3] == 213) || (d[3] == 214) || (d[3] == 215) || (d[3] == 232)) {
                        return TrackingAffiliation.STAFF;
                    }
                    return TrackingAffiliation.SOM;
                }
                if ((d[2] >= 84) && (d[2] <= 111)) {
                    return TrackingAffiliation.SOM;
                }
                if (d[2] == 112) {
                    return TrackingAffiliation.LPCH;
                }
                if (d[2] == 113) {
                    return TrackingAffiliation.SOM;
                }
                if ((d[2] == 114) || (d[2] == 115)) {
                    return TrackingAffiliation.SHC;
                }
                if ((d[2] >= 116) && (d[2] <= 123)) {
                    return TrackingAffiliation.SOM;
                }
                if ((d[2] == 124) || (d[2] == 125)) {
                    return TrackingAffiliation.SHC;
                }
                if (d[2] == 126) {
                    return TrackingAffiliation.LPCH;
                }
                if (d[2] == 127) {
                    return TrackingAffiliation.SHC;
                }
                return TrackingAffiliation.SOM;
            }
            if ((d[1] == 66) && (d[2] == 222) && (d[3] == 37)) {
                return TrackingAffiliation.STAFF;// rzwies home ip
            }
            if ((d[1] == 66) || (d[1] == 67)) {
                return TrackingAffiliation.SU;
            }
            return TrackingAffiliation.OTHER;
        }
        if ((d[0] == 204) && (d[1] == 161) && (d[2] == 120)) {
            return TrackingAffiliation.SHC;
        }
        if ((d[0] == 209) && (d[1] == 11)) {
            if ((d[2] >= 184) && (d[2] <= 187)) {
                return TrackingAffiliation.SHC;
            }
            if ((d[2] >= 188) && (d[2] <= 191)) {
                return TrackingAffiliation.LPCH;
            }
        }
        return TrackingAffiliation.OTHER;
    }

    private String stringValue;

    private TrackingAffiliation(final String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
