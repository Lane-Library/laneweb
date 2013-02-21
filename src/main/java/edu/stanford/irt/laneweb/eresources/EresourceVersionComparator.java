package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;

/**
 * @author ryanmax
 */
public class EresourceVersionComparator implements Comparator<Version>, Serializable {

    private static final Pattern CLOSED_DATE_PATTERN = Pattern.compile("(\\d{4})\\-(\\d{4})\\.");

    private static final List<String> FAVORED_PUBLISHERS = Arrays.asList("sciencedirect", "wiley", "springer",
            "highwire", "ovid", "nature", "liebert", "informaworld", "karger", "pubmed central");

    private static final Pattern OPEN_DATE_PATTERN = Pattern.compile(".*(\\d{4})\\-");

    private static final long serialVersionUID = 1L;

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    public int compare(final Version v1, final Version v2) {
        int score1 = calculateHoldingsScore(v1);
        int score2 = calculateHoldingsScore(v2);
        int yearsCovered1 = getYearsCovered(v1);
        int yearsCovered2 = getYearsCovered(v2);
        // factor in years covered only if available in both versions
        if (yearsCovered1 != -1 && yearsCovered2 != -1) {
            score1 = score1 + yearsCovered1;
            score2 = score2 + yearsCovered2;
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
        Collection<Link> links = v.getLinks();
        if (links.size() > 0 && "Impact Factor".equals(links.iterator().next().getLabel())) {
            return -99;
        }
        String summaryHoldings = v.getSummaryHoldings();
        if (summaryHoldings != null) {
            if (summaryHoldings.endsWith("-") || summaryHoldings.startsWith("v. 1-")) {
                score++;
            } else if (summaryHoldings.endsWith(".")) {
                score--;
            }
        }
        String dates = v.getDates();
        if (dates != null) {
            if (dates.endsWith("-")) {
                score++;
            } else if (dates.endsWith(".")) {
                score--;
            }
        }
        String description = v.getDescription();
        if (description != null && description.contains("delayed")) {
            score--;
        }
        //make sure installed software product description is first:
        if (score == 0) {
            if ("Product Description".equals(links.iterator().next().getLabel())) {
                score = 1;
            }
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
        String publisher = v.getPublisher();
        if (publisher != null) {
            publisher = publisher.toLowerCase();
            if (FAVORED_PUBLISHERS.contains(publisher)) {
                score = score + (10 - FAVORED_PUBLISHERS.indexOf(publisher));
            }
        }
        return score;
    }

    private int getYearsCovered(final Version v) {
        String dates = v.getDates();
        if (dates != null) {
            Matcher closedMatcher = CLOSED_DATE_PATTERN.matcher(dates);
            Matcher openMatcher = OPEN_DATE_PATTERN.matcher(dates);
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
