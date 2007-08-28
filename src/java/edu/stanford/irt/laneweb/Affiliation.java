/**
 * 
 */
package edu.stanford.irt.laneweb;

import java.util.StringTokenizer;

/**
 * @author ceyates
 *
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
	
	public String toString() {
		return this.stringValue;
	}
	
	public static Affiliation getAffiliationForIP(final String ip) {
		int[] d = new int[4];
		int index = 0;
			for (StringTokenizer tokenizer = new StringTokenizer(ip, "."); tokenizer
					.hasMoreTokens();) {
				d[index++] = Integer.parseInt(tokenizer.nextToken());
			}
		if (d[0] == 10) {
			if (d[1] == 252) {
				if (d[2] == 6 || d[2] == 7 || d[2] == 10 || d[2] == 11
						|| d[2] == 14 || d[2] == 15 || d[2] == 18 || d[2] == 19
						|| d[2] == 22 || d[2] == 23 || d[2] == 26 || d[2] == 27
						|| d[2] == 30 || d[2] == 31 || d[2] == 34 || d[2] == 35
						|| d[2] == 38 || d[2] == 39 || d[2] == 43 || d[2] == 46
						|| d[2] == 47 || d[2] == 50 || d[2] == 51 || d[2] == 54
						|| d[2] == 55 || d[2] == 67 || d[2] == 71 || d[2] == 97
						|| d[2] == 108 || d[2] == 109 || d[2] == 122
						|| d[2] == 123) {
					return Affiliation.LPCH;
				}
				if (d[2] >= 138 && d[2] <= 146) {
					return Affiliation.LPCH;
				}
				if (d[2] == 250) {
					if (d[3] >= 96 && d[3] <= 127) {
						return Affiliation.LPCH;
					}
					return Affiliation.SHC;
				}
				return Affiliation.SHC;
			}
			if (d[1] >= 253 && d[1] <= 255) {
				return Affiliation.SHC;
			}
			if (d[1] == 50) {
				return Affiliation.LPCH;
			}
			return Affiliation.OTHER;
		}
		if (d[0] == 128) {
			if (d[1] == 12) {
				return Affiliation.SU;
			}
			return Affiliation.OTHER;
		}
		if (d[0] == 134) {//10.50.0.1 - 10.50.255.255
			if (d[1] == 79) {
				return Affiliation.SU;
			}
			return Affiliation.OTHER;
		}
		if (d[0] == 152) {
			if (d[1] >= 130 && d[1] <= 133) {
				if (d[2] == 10 && d[3] == 128) {
					return Affiliation.PAVA;
				}
			}
			return Affiliation.OTHER;
		}
		if (d[0] == 171) {
			if (d[1] == 64) {
				return Affiliation.SU;
			}
			if (d[1] == 65) {
				if (d[2] >= 1 && d[2] <= 43) {
					return Affiliation.SOM;
				}
				if (d[2] >= 44 && d[2] <= 45) {
					return Affiliation.SHC;
				}
				if (d[2] >= 46 && d[2] <= 55) {
					return Affiliation.SOM;
				}
				if (d[2] >= 56 && d[2] <= 59) {
					return Affiliation.LPCH;
				}
				if (d[2] >= 60 && d[2] <= 81) {
					return Affiliation.SOM;
				}
				if (d[2] == 82) {
					if (d[3] == 11 || d[3] == 12 || d[3] == 15 || d[3] == 18
							|| d[3] == 20 || d[3] == 24 || d[3] == 25
							|| d[3] == 27 || d[3] == 29 || d[3] == 31
							|| d[3] == 32 || d[3] == 37 || d[3] == 42
							|| d[3] == 43 || d[3] == 46 || d[3] == 47
							|| d[3] == 48 || d[3] == 59 || d[3] == 60
							|| d[3] == 61 || d[3] == 65 || d[3] == 66
							|| d[3] == 74 || d[3] == 81 || d[3] == 82
							|| d[3] == 85 || d[3] == 86 || d[3] == 94
							|| d[3] == 96 || d[3] == 109 || d[3] == 110
							|| d[3] == 118 || d[3] == 119 || d[3] == 129
							|| d[3] == 130 || d[3] == 133 || d[3] == 135
							|| d[3] == 143 || d[3] == 146 || d[3] == 166
							|| d[3] == 179 || d[3] == 182 || d[3] == 197
							|| d[3] == 200 || d[3] == 204 || d[3] == 213) {
						return Affiliation.STAFF;
					}
					return Affiliation.SOM;
				}
				if (d[2] == 83) {
					if (d[3] == 3 || d[3] == 8 || d[3] == 32 || d[3] == 34
							|| d[3] == 45 || d[3] == 84 || d[3] == 96
							|| d[3] == 98 || d[3] == 100 || d[3] == 114
							|| d[3] == 127 || d[3] == 137 || d[3] == 149
							|| d[3] == 152 || d[3] == 168 || d[3] == 172
							|| d[3] == 174 || d[3] == 183 || d[3] == 186
							|| d[3] == 187 || d[3] == 189 || d[3] == 203
							|| d[3] == 204 || d[3] == 205 || d[3] == 206
							|| d[3] == 208 || d[3] == 212 || d[3] == 213
							|| d[3] == 214 || d[3] == 215 || d[3] == 219) {
						return Affiliation.STAFF;
					}
					return Affiliation.SOM;
				}
				if (d[2] >= 84 && d[2] <= 111) {
					return Affiliation.SOM;
				}
				if (d[2] >= 112 && d[2] <= 126) {
					return Affiliation.SHC;
				}
				if (d[2] == 127) {
					return Affiliation.LPCH;
				}
				if (d[2] >= 128 && d[2] <= 255) {
					return Affiliation.SHC;
				}
				return Affiliation.SU;
			}
			if (d[1] == 66) {
				if (d[2] >= 97 && d[2] <= 99) {
					return Affiliation.SHC;
				}
				return Affiliation.SU;
			}
			if (d[1] == 67) {
				return Affiliation.SU;
			}
			return Affiliation.OTHER;
		}
		return Affiliation.OTHER;
	}

}
