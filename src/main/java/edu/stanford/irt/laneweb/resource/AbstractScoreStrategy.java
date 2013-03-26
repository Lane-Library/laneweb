package edu.stanford.irt.laneweb.resource;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractScoreStrategy {

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private static final Pattern YEAR_PATTERN = Pattern.compile(".*(\\d{4}).*");

    // return -10 to 10, based on pub date's proximity to THIS_YEAR
    protected int computeDateAdjustment(final int year) {
        return year == 0 ? 0 : Math.max(-10, 10 - (THIS_YEAR - year));
    }

    // return -10 to 10, based on pub date's proximity to THIS_YEAR
    protected int computeDateAdjustment(final String publicationDate) {
        if (null == publicationDate) {
            return 0;
        }
        Matcher yearMatcher = YEAR_PATTERN.matcher(publicationDate);
        if (yearMatcher.matches()) {
            return computeDateAdjustment(Integer.parseInt(yearMatcher.group(1)));
        }
        return 0;
    }
}
