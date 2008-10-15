/**
 * 
 */
package edu.stanford.irt.laneweb;

import java.util.StringTokenizer;

/**
 * @author ceyates
 */
public class Affiliation {

    public static final Affiliation SHC = new Affiliation("SHC");

    public static final Affiliation LPCH = new Affiliation("LPCH");

    public static final Affiliation SU = new Affiliation("SU");

    public static final Affiliation SOM = new Affiliation("SOM");

    public static final Affiliation PAVA = new Affiliation("PAVA");

    public static final Affiliation OTHER = new Affiliation("OTHER");

    public static final Affiliation ERR = new Affiliation("ERR");

    public static final Affiliation STAFF = new Affiliation("STAFF");

    public static final Affiliation LANE = new Affiliation("LANE");

    private String stringValue;

    private Affiliation(final String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    public static Affiliation getAffiliationForIP(final String ip) {
        int[] d = new int[4];
        int index = 0;
        for (StringTokenizer tokenizer = new StringTokenizer(ip, "."); tokenizer
                .hasMoreTokens();) {
            try {
                d[index++] = Integer.parseInt(tokenizer.nextToken());
            } catch (NumberFormatException e) {
                return Affiliation.ERR;
            }
        }
        if (d[0] == 10) {
            if (d[1] == 50) {
                return Affiliation.LPCH;
            }
            if (d[1] == 248) {
                return Affiliation.SHC;
            }
            if ((d[1] == 250) || (d[1] == 251)) {
                if (d[2] < 128) {
                    return Affiliation.SHC;
                }
                return Affiliation.LPCH;
            }
            if (d[1] == 252) {
                if ((d[2] == 1) && ((d[3] == 1) || (d[3] == 2))) {
                    return Affiliation.LPCH;
                }
                if ((d[2] == 5) || (d[2] == 6) || (d[2] == 7) || (d[2] == 10)
                        || (d[2] == 11) || (d[2] == 18) || (d[2] == 19)
                        || (d[2] == 22) || (d[2] == 23) || (d[2] == 26)
                        || (d[2] == 27) || (d[2] == 30) || (d[2] == 31)
                        || (d[2] == 34) || (d[2] == 35) || (d[2] == 38)
                        || (d[2] == 39) || (d[2] == 42) || (d[2] == 43)
                        || (d[2] == 46) || (d[2] == 47) || (d[2] == 50)
                        || (d[2] == 51) || (d[2] == 54) || (d[2] == 55)
                        || (d[2] == 60) || (d[2] == 61) || (d[2] == 62)
                        || (d[2] == 63)) {
                    return Affiliation.LPCH;
                }
                if ((d[2] == 64)
                        && ((d[3] == 12) || (d[3] == 23) || (d[3] == 46)
                                || (d[3] == 60) || (d[3] == 64) || (d[3] == 71))) {
                    return Affiliation.LPCH;
                }
                if ((d[2] == 65)
                        && ((d[3] == 5) || (d[3] == 6) || (d[3] == 9)
                                || (d[3] == 13) || (d[3] == 14) || (d[3] == 17)
                                || (d[3] == 18) || (d[3] == 41) || (d[3] == 42)
                                || (d[3] == 141) || (d[3] == 142)
                                || (d[3] == 145) || (d[3] == 146))) {
                    return Affiliation.LPCH;
                }
                if ((d[2] == 66)
                        && ((d[3] == 5) || (d[3] == 6) || (d[3] == 49)
                                || (d[3] == 50) || (d[3] == 233)
                                || (d[3] == 234) || (d[3] == 249)
                                || (d[3] == 250) || (d[3] == 253) || (d[3] == 254))) {
                    return Affiliation.LPCH;
                }
                if ((d[2] == 67) || (d[2] == 70) || (d[2] == 71)
                        || (d[2] == 84) || (d[2] == 85) || (d[2] == 107)
                        || (d[2] == 108) || (d[2] == 109) || (d[2] == 122)
                        || (d[2] == 123)) {
                    return Affiliation.LPCH;
                }
                if ((d[2] >= 138) && (d[2] <= 149)) {
                    return Affiliation.LPCH;
                }
                if ((d[2] == 152) || (d[2] == 153) || (d[2] == 200)) {
                    return Affiliation.LPCH;
                }
                if (d[2] == 250) {
                    if ((d[3] >= 97) && (d[3] <= 126)) {
                        return Affiliation.LPCH;
                    }
                }
                return Affiliation.SHC;
            }
            if (d[1] == 253) {
                if ((d[2] == 220) || (d[2] == 254) || (d[2] == 255)) {
                    return Affiliation.LPCH;
                }
                return Affiliation.SHC;
            }
            if (d[1] > 253) {
                return Affiliation.SHC;
            }
            return Affiliation.OTHER;
        }
        if (d[0] == 128) {
            if (d[1] == 12) {
                return Affiliation.SU;
            }
            return Affiliation.OTHER;
        }
        if (d[0] == 134) {
            if (d[1] == 79) {
                return Affiliation.SU;
            }
            return Affiliation.OTHER;
        }
        if ((d[0] == 152) && (d[2] == 10) && (d[3] == 128) && (d[1] >= 130)
                && (d[1] <= 133)) {
            return Affiliation.PAVA;
        }
        if ((d[0] == 159) && (d[1] == 140) && (d[2] == 183)) {
            return Affiliation.LPCH;
        }
        if (d[0] == 171) {
            if (d[1] == 64) {
                return Affiliation.SU;
            }
            if (d[1] == 65) {
                if ((d[2] >= 0) && (d[2] <= 43)) {
                    return Affiliation.SOM;
                }
                if ((d[2] >= 44) && (d[2] <= 47)) {
                    return Affiliation.SHC;
                }
                if ((d[2] >= 48) && (d[2] <= 81)) {
                    return Affiliation.SOM;
                }
                if (d[2] == 82) {
                    if ((d[3] == 11) || (d[3] == 15) || (d[3] == 18)
                            || (d[3] == 20) || (d[3] == 24) || (d[3] == 25)
                            || (d[3] == 27) || (d[3] == 31) || (d[3] == 42)
                            || (d[3] == 43) || (d[3] == 46) || (d[3] == 47)
                            || (d[3] == 48) || (d[3] == 53) || (d[3] == 59)
                            || (d[3] == 60) || (d[3] == 65) || (d[3] == 66)
                            || (d[3] == 68) || (d[3] == 74) || (d[3] == 78)
                            || (d[3] == 81) || (d[3] == 86) || (d[3] == 95)
                            || (d[3] == 105) || (d[3] == 109) || (d[3] == 119)
                            || (d[3] == 129) || (d[3] == 130) || (d[3] == 135)
                            || (d[3] == 143) || (d[3] == 144) || (d[3] == 146)
                            || (d[3] == 166) || (d[3] == 168) || (d[3] == 179)
                            || (d[3] == 182) || (d[3] == 197) || (d[3] == 200)
                            || (d[3] == 204)) {
                        return Affiliation.STAFF;
                    }
                    return Affiliation.SOM;
                }
                if (d[2] == 83) {
                    if ((d[3] == 32) || (d[3] == 45) || (d[3] == 73)
                            || (d[3] == 84) || (d[3] == 86) || (d[3] == 96)
                            || (d[3] == 98) || (d[3] == 100) || (d[3] == 114)
                            || (d[3] == 118) || (d[3] == 149) || (d[3] == 168)
                            || (d[3] == 172) || (d[3] == 174) || (d[3] == 187)
                            || (d[3] == 203) || (d[3] == 204) || (d[3] == 205)
                            || (d[3] == 206) || (d[3] == 208) || (d[3] == 212)
                            || (d[3] == 213) || (d[3] == 214) || (d[3] == 215)
                            || (d[3] == 219) || (d[3] == 232)) {
                        return Affiliation.STAFF;
                    }
                    return Affiliation.SOM;
                }
                if ((d[2] >= 84) && (d[2] <= 111)) {
                    return Affiliation.SOM;
                }
                if (d[2] == 112) {
                    return Affiliation.LPCH;
                }
                if (d[2] == 113) {
                    return Affiliation.SOM;
                }
                if ((d[2] == 114) || (d[2] == 115)) {
                    return Affiliation.SHC;
                }
                if ((d[2] >= 116) && (d[2] <= 123)) {
                    return Affiliation.SOM;
                }
                if ((d[2] == 124) || (d[2] == 125)) {
                    return Affiliation.SHC;
                }
                if (d[2] == 126) {
                    return Affiliation.LPCH;
                }
                if (d[2] == 127) {
                    return Affiliation.SHC;
                }
                return Affiliation.SOM;
            }
            if ((d[1] == 66) && (d[2] == 222) && (d[3] == 37)) {
                return Affiliation.STAFF;// rzwies home ip
            }
            if ((d[1] == 66) || (d[1] == 67)) {
                return Affiliation.SU;
            }
            return Affiliation.OTHER;
        }
        if ((d[0] == 204) && (d[1] == 161) && (d[2] == 120)) {
            return Affiliation.SHC;
        }
        if ((d[0] == 209) && (d[1] == 11)) {
            if ((d[2] >= 184) && (d[2] <= 187)) {
                return Affiliation.SHC;
            }
            if ((d[2] >= 188) && (d[2] <= 191)) {
                return Affiliation.LPCH;
            }
        }
        return Affiliation.OTHER;
    }

}
