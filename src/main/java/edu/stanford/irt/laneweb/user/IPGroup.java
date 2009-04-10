/**
 * 
 */
package edu.stanford.irt.laneweb.user;

import java.util.StringTokenizer;

/**
 * @author ceyates
 */
public class IPGroup {

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
    
    public static final IPGroup SOM_OTHER = new IPGroup("SOM_OTHER");
    
    public static final IPGroup STAFF = new IPGroup("STAFF");

    public static final IPGroup SU = new IPGroup("SU");

    public static IPGroup getGroupForIP(final String ip) {
        int[] d = new int[4];
        int index = 0;
        for (StringTokenizer tokenizer = new StringTokenizer(ip, "."); tokenizer.hasMoreTokens();) {
            try {
                d[index++] = Integer.parseInt(tokenizer.nextToken());
            } catch (NumberFormatException e) {
                return IPGroup.ERR;
            }
        }
        if (d[0] == 10) {
            if (d[1] == 50) {
                return IPGroup.LPCH;
            }
            if ((d[1] == 247) || (d[1] == 248)) {
                return IPGroup.SHC;
            }
            if ((d[1] == 250) || (d[1] == 251)) {
                if (d[2] < 128) {
                    return IPGroup.SHC;
                }
                return IPGroup.LPCH;
            }
            if (d[1] == 252) {
                if ((d[2] == 1) && ((d[3] == 1) || (d[3] == 2))) {
                    return IPGroup.LPCH;
                }
                if ((d[2] == 5) || (d[2] == 6) || (d[2] == 7) || (d[2] == 10) || (d[2] == 11) || (d[2] == 18) || (d[2] == 19) || (d[2] == 22) || (d[2] == 23)
                        || (d[2] == 26) || (d[2] == 27) || (d[2] == 30) || (d[2] == 31) || (d[2] == 34) || (d[2] == 35) || (d[2] == 38) || (d[2] == 39)
                        || (d[2] == 42) || (d[2] == 43) || (d[2] == 46) || (d[2] == 47) || (d[2] == 50) || (d[2] == 51) || (d[2] == 54) || (d[2] == 55)
                        || (d[2] == 60) || (d[2] == 61) || (d[2] == 62) || (d[2] == 63)) {
                    return IPGroup.LPCH;
                }
                if ((d[2] == 64) && ((d[3] == 12) || (d[3] == 23) || (d[3] == 46) || (d[3] == 60) || (d[3] == 64) || (d[3] == 71))) {
                    return IPGroup.LPCH;
                }
                if ((d[2] == 65)
                        && ((d[3] == 5) || (d[3] == 6) || (d[3] == 9) || (d[3] == 13) || (d[3] == 14) || (d[3] == 17) || (d[3] == 18) || (d[3] == 41)
                                || (d[3] == 42) || (d[3] == 141) || (d[3] == 142) || (d[3] == 145) || (d[3] == 146))) {
                    return IPGroup.LPCH;
                }
                if ((d[2] == 66)
                        && ((d[3] == 5) || (d[3] == 6) || (d[3] == 49) || (d[3] == 50) || (d[3] == 233) || (d[3] == 234) || (d[3] == 249) || (d[3] == 250)
                                || (d[3] == 253) || (d[3] == 254))) {
                    return IPGroup.LPCH;
                }
                if ((d[2] == 67) || (d[2] == 70) || (d[2] == 71) || (d[2] == 84) || (d[2] == 85) || (d[2] == 107) || (d[2] == 108) || (d[2] == 109)
                        || (d[2] == 122) || (d[2] == 123)) {
                    return IPGroup.LPCH;
                }
                if ((d[2] >= 138) && (d[2] <= 149)) {
                    return IPGroup.LPCH;
                }
                if ((d[2] == 152) || (d[2] == 153) || (d[2] == 200)) {
                    return IPGroup.LPCH;
                }
                if (d[2] == 250) {
                    if ((d[3] >= 97) && (d[3] <= 126)) {
                        return IPGroup.LPCH;
                    }
                }
                return IPGroup.SHC;
            }
            if (d[1] == 253) {
                if ((d[2] == 220) || (d[2] == 254) || (d[2] == 255)) {
                    return IPGroup.LPCH;
                }
                return IPGroup.SHC;
            }
            if (d[1] > 253) {
                return IPGroup.SHC;
            }
            return IPGroup.OTHER;
        }
        if (d[0] == 128) {
            if (d[1] == 12) {
                return IPGroup.SU;
            }
            return IPGroup.OTHER;
        }
        if (d[0] == 134) {
            if (d[1] == 79) {
                return IPGroup.SU;
            }
            return IPGroup.OTHER;
        }
        if ((d[0] == 152) && (d[2] == 10) && (d[3] == 128) && (d[1] >= 130) && (d[1] <= 133)) {
            return IPGroup.PAVA;
        }
        if ((d[0] == 159) && (d[1] == 140) && (d[2] == 183)) {
            return IPGroup.LPCH;
        }
        if (d[0] == 171) {
            if (d[1] == 64) {
                return IPGroup.SU;
            }
            if (d[1] == 65) {
                if ((d[2] == 1) && (d[3] == 213)) {
                    return IPGroup.STAFF;
                }
                if ((d[2] >= 0) && (d[2] <= 43)) {
                  if ((d[2] >= 4) && (d[2] <= 15)) {
                    return IPGroup.SOM_CCSR;
                  }
                  if ((d[2] >= 20) && (d[2] <= 27)) {
                    return IPGroup.SOM_BECKMAN;
                  }
                  if ((d[2] == 40) && ((d[3] >= 64) && (d[3] <= 79))) {
                    return IPGroup.SOM_GRANT;
                  }
                  return IPGroup.SOM_OTHER;
                }
                if ((d[2] == 44)) {
                    return IPGroup.PAVA;
                }
                if ((d[2] == 45)) {
                  return IPGroup.SOM_OTHER;
                }
                if ((d[2] >= 46) && (d[2] <= 47)) {
                  return IPGroup.SHC;
                }
                if ((d[2] >= 48) && (d[2] <= 81)) {
                  if ((d[2] == 71) && (d[3] <= 127)) {
                    return IPGroup.SOM_CLARK;
                  }
                  return IPGroup.SOM_OTHER;
                }
                if (d[2] == 82) {
                    if ((d[3] == 11) || (d[3] == 15) || (d[3] == 16) || (d[3] == 20) || (d[3] == 24) || (d[3] == 25) || (d[3] == 27) || (d[3] == 31)
                            || (d[3] == 42) || (d[3] == 43) || (d[3] == 46) || (d[3] == 47) || (d[3] == 48) || (d[3] == 59) || (d[3] == 60) || (d[3] == 68)
                            || (d[3] == 74) || (d[3] == 78) || (d[3] == 95) || (d[3] == 105) || (d[3] == 119) || (d[3] == 129) || (d[3] == 135)
                            || (d[3] == 143) || (d[3] == 144) || (d[3] == 166) || (d[3] == 168) || (d[3] == 179) || (d[3] == 204) || (d[3] == 232)) {
                        return IPGroup.STAFF;
                    }
                    return IPGroup.SOM_LANE;
                }
                if (d[2] == 83) {
                    if (((d[3] == 8) || (d[3] == 32)) || (d[3] == 37) || (d[3] == 45) || (d[3] == 73) || (d[3] == 84) || (d[3] == 86) || (d[3] == 96)
                            || (d[3] == 98) || (d[3] == 114) || (d[3] == 132) || (d[3] == 149) || (d[3] == 157) || (d[3] == 160) || (d[3] == 168)
                            || (d[3] == 172) || (d[3] == 174) || (d[3] == 203) || (d[3] == 204) || (d[3] == 205) || (d[3] == 206) || (d[3] == 208)
                            || (d[3] == 212) || (d[3] == 213) || (d[3] == 214) || (d[3] == 215) || (d[3] == 232)) {
                        return IPGroup.STAFF;
                    }
                    return IPGroup.SOM_LANE;
                }
                if ((d[2] >= 84) && (d[2] <= 111)) {
                  if ((d[2] >= 88) && (d[2] <= 91)) {
                    return IPGroup.SOM_GRANT;
                  }
                  if ((d[2] >= 92) && (d[2] <= 95)) {
                    return IPGroup.SOM_CLARK;
                  }
                  if ((d[2] >= 102) && (d[2] <= 103)) {
                    return IPGroup.SOM_CLARK;
                  }
                  return IPGroup.SOM_OTHER;
                }
                if (d[2] == 112) {
                    return IPGroup.LPCH;
                }
                if (d[2] == 113) {
                    return IPGroup.SOM_OTHER;
                }
                if ((d[2] == 114) || (d[2] == 115)) {
                    return IPGroup.SHC;
                }
                if ((d[2] >= 116) && (d[2] <= 123)) {
                    return IPGroup.SOM_OTHER;
                }
                if ((d[2] == 124)) {
                    return IPGroup.SOM_OTHER;
                }
                if ((d[2] == 125)) {
                  return IPGroup.SHC;
                }
                if (d[2] == 126) {
                    return IPGroup.LPCH;
                }
                if (d[2] == 127) {
                    return IPGroup.SHC;
                }
                if ((d[2] >= 128) && (d[2] <= 255)) {
                  return IPGroup.SHC;
                }
                return IPGroup.SOM_OTHER;
            }
            if ((d[1] == 66) && (d[2] == 222) && (d[3] == 37)) {
                return IPGroup.STAFF;// rzwies home ip
            }
            if ((d[1] == 66) || (d[1] == 67)) {
                return IPGroup.SU;
            }
            return IPGroup.OTHER;
        }
        if ((d[0] == 204) && (d[1] == 161) && (d[2] == 120)) {
            return IPGroup.SHC;
        }
        if ((d[0] == 209) && (d[1] == 11)) {
            if ((d[2] >= 184) && (d[2] <= 187)) {
                return IPGroup.SHC;
            }
            if ((d[2] >= 188) && (d[2] <= 191)) {
                return IPGroup.LPCH;
            }
        }
        return IPGroup.OTHER;
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
