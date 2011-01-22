package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.irt.eresources.Version;

/**
 * @author ryanmax
 */
public class EresourceVersionComparator implements Comparator<Version>, Serializable {

    private static final Pattern CLOSED_DATE_PATTERN = Pattern.compile("(\\d{4})\\-(\\d{4})\\.");

    private static final List<String> favoredPublishers = Arrays.asList("sciencedirect", "wiley", "springer", "highwire", "ovid",
            "nature", "liebert", "informaworld", "karger", "pubmed central");

    private static final Pattern OPEN_DATE_PATTERN = Pattern.compile(".*(\\d{4})\\-");

    private static final long serialVersionUID = 1L;

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    public int compare(final Version v1, final Version v2) {
        int score1 = calculateHoldingsScore(v1);
        int score2 = calculateHoldingsScore(v2);
        // factor in years covered only if available in both versions
        if (getYearsCovered(v1) != -1 && getYearsCovered(v2) != -1) {
            score1 = score1 + getYearsCovered(v1);
            score2 = score2 + getYearsCovered(v2);
        }
        if (score1 != score2) {
            return score2 - score1;
        }
        // only factor in publisher score if holding scores are equal
        score1 = calculatePublisherScore(v1);
        score2 = calculatePublisherScore(v2);
        if (score1 != score2) {
            return score2 - score1;
        }
        return 1;
        // return (v1.getId() < v2.getId() ? -1 : (v1.getId() == v2.getId() ? 0
        // : 1));
    }

    /**
     * Calculate sorting score for version based on:
     * 
     * <pre>
     * ++ dates or summaryHoldings end in "-"
     * -- description has "delayed" in it
     * -- first link label is "Impact Factor"
     * -- has period at end of dates or summaryHoldings
     * </pre>
     * 
     * @param v
     * @return score
     */
    private int calculateHoldingsScore(final Version v) {
        int score = 0;
        if (v.getLinks().size() > 0 && "Impact Factor".equals(v.getLinks().iterator().next().getLabel())) {
            return -99;
        }
        if (null != v.getSummaryHoldings()) {
            if (v.getSummaryHoldings().endsWith("-") || v.getSummaryHoldings().startsWith("v. 1-")) {
                score++;
            } else if (v.getSummaryHoldings().endsWith(".")) {
                score--;
            }
        }
        if (null != v.getDates()) {
            if (v.getDates().endsWith("-")) {
                score++;
            } else if (v.getDates().endsWith(".")) {
                score--;
            }
        }
        if (null != v.getDescription() && v.getDescription().contains("delayed")) {
            score--;
        }
        return score;
    }

    /**
     * Calculate score for select list of publishers
     * 
     * @param v
     * @return score
     */
    private int calculatePublisherScore(final Version v) {
        int score = 1;
        if (null != v.getPublisher() && favoredPublishers.contains(v.getPublisher().toLowerCase())) {
            score = score + (10 - favoredPublishers.indexOf(v.getPublisher().toLowerCase()));
        }
        return score;
    }

    private int getYearsCovered(final Version v) {
        if (null != v.getDates()) {
            Matcher closedMatcher = CLOSED_DATE_PATTERN.matcher(v.getDates());
            Matcher openMatcher = OPEN_DATE_PATTERN.matcher(v.getDates());
            if (closedMatcher.matches()) {
                int date1 = Integer.parseInt(closedMatcher.group(1));
                int date2 = Integer.parseInt(closedMatcher.group(2));
                return date2 - date1;
            } else if (openMatcher.matches()) {
                return THIS_YEAR - Integer.parseInt(openMatcher.group(1));
            }
        }
        return -1;
    }
}
