package edu.stanford.irt.laneweb.search;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.irt.search.impl.ContentResult;

public class ScoreStrategy {

    private static final int DESC_ONLY = 10;

    private static final Pattern DOUBLE_WEIGHT_PATTERN = Pattern.compile("pubmed_cochrane_reviews|dare|acpjc");

    private static final Pattern ENGINEID_PATTERN = Pattern.compile("_content_\\d+");

    private static final int EXACT_TITLE = 100;

    private static final Pattern HALF_WEIGHT_PATTERN = Pattern.compile(
            "^bmj_clinical_evidence|pubmed_(clinicaltrial|recent_reviews|treatment_focused|diagnosis_focused|prognosis_focused|harm_focused|etiology_focused|epidemiology_focused)");

    private static final int MAX_SCORE = 10;

    private static final int MIN_SCORE = -10;

    private static final int NO_MATCH = 1;

    private static final Pattern QUARTER_WEIGHT_PATTERN = Pattern.compile("aafp_patients|medlineplus_0");

    private static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private static final int TITLE_AND_DESC = 30;

    private static final int TITLE_AND_MULTI_DESC = 40;

    private static final int TITLE_BEGINS = 65;

    private static final int TITLE_BEGINS_AND_MULTI_TITLE = 70;

    private static final int TITLE_BEGINS_AND_MULTI_TITLE_AND_DESC = 80;

    private static final int TITLE_BEGINS_AND_MULTI_TITLE_AND_MULTI_DESC = 90;

    private static final int TITLE_CONTAINS_AND_MULTI_TITLE = 50;

    private static final int TITLE_CONTAINS_AND_MULTI_TITLE_AND_MULTI_DESC = 60;

    private static final int TITLE_ONLY = 20;

    /**
     * <pre>
     *  100 exact title match
     *  90 title begins with AND title contains more than one match AND desc contains more than one match
     *  80 title begins with AND title contains more than one match AND desc match
     *  70 title begins with AND title contains more than one match
     *  65 title begins with
     *  60 title contains more than one match AND desc contains more than one match
     *  50 title contains more than one match
     *  40 title match AND desc contains more than one match
     *  30 title match AND desc match
     *  20 title match
     *  10 desc match
     *  1
     * </pre>
     *
     * @param searchResult
     *            the ContentResult
     * @param queryTermPattern
     *            a Pattern
     * @return the resulting score
     */
    public int computeScore(final ContentResult searchResult, final Pattern queryTermPattern) {
        int score;
        String title = searchResult.getTitle();
        int titleHits = getTitleHits(queryTermPattern, title);
        int descriptionHits = getDescriptionHits(searchResult, queryTermPattern);
        if (titleHits > 0) {
            score = getScoreWhenTitleHits(queryTermPattern, title, titleHits, descriptionHits);
        } else if (descriptionHits > 0) {
            score = DESC_ONLY;
        } else {
            score = NO_MATCH;
        }
        double weight = computeWeight(ENGINEID_PATTERN.matcher(searchResult.getId()).replaceFirst(""));
        score = (int) ((score + computeDateAdjustment(searchResult.getYear())) * weight);
        return score < 0 ? 0 : score;
    }

    // return -10 to 10, based on pub date's proximity to THIS_YEAR
    private int computeDateAdjustment(final int year) {
        return year == 0 ? 0 : Math.max(MIN_SCORE, MAX_SCORE - (THIS_YEAR - year));
    }

    private double computeWeight(final String engineId) {
        if (DOUBLE_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 2;
        } else if (HALF_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 0.5;
        } else if (QUARTER_WEIGHT_PATTERN.matcher(engineId).matches()) {
            return 0.25;
        }
        return 1;
    }

    private int getDescriptionHits(final ContentResult searchResult, final Pattern queryTermPattern) {
        int descriptionHits = 0;
        String description = searchResult.getDescription();
        if (null != description) {
            Matcher ndMatcher = queryTermPattern.matcher(description);
            while (ndMatcher.find()) {
                descriptionHits++;
            }
        }
        return descriptionHits;
    }

    private int getScoreWhenMultipleTitleHits(final int descriptionHits) {
        int score;
        if (descriptionHits > 1) {
            score = TITLE_CONTAINS_AND_MULTI_TITLE_AND_MULTI_DESC;
        } else {
            score = TITLE_CONTAINS_AND_MULTI_TITLE;
        }
        return score;
    }

    private int getScoreWhenStartsWith(final int titleHits, final int descriptionHits, final Pattern queryTermPattern,
            final String title) {
        int score;
        if (titleHits > 1) {
            if (descriptionHits > 1) {
                score = TITLE_BEGINS_AND_MULTI_TITLE_AND_MULTI_DESC;
            } else if (descriptionHits == 1) {
                score = TITLE_BEGINS_AND_MULTI_TITLE_AND_DESC;
            } else {
                score = TITLE_BEGINS_AND_MULTI_TITLE;
            }
        } else if (isExactTitle(queryTermPattern, title)) {
            score = EXACT_TITLE;
        } else {
            score = TITLE_BEGINS;
        }
        return score;
    }

    private int getScoreWhenTitleHits(final Pattern queryTermPattern, final String title, final int titleHits,
            final int descriptionHits) {
        int score;
        if (titleBeginsWithTerm(queryTermPattern, title)) {
            score = getScoreWhenStartsWith(titleHits, descriptionHits, queryTermPattern, title);
        } else if (titleHits > 1) {
            score = getScoreWhenMultipleTitleHits(descriptionHits);
        } else if (descriptionHits > 1) {
            score = TITLE_AND_MULTI_DESC;
        } else if (descriptionHits == 1) {
            score = TITLE_AND_DESC;
        } else {
            score = TITLE_ONLY;
        }
        return score;
    }

    private int getTitleHits(final Pattern queryTermPattern, final String title) {
        int titleHits = 0;
        if (title != null) {
            Matcher ntMatcher = queryTermPattern.matcher(title);
            while (ntMatcher.find()) {
                titleHits++;
            }
        }
        return titleHits;
    }

    private boolean isExactTitle(final Pattern queryTermPattern, final String title) {
        Pattern exactTitlePattern = Pattern.compile("^(" + queryTermPattern + "\\W?)$",
                Pattern.CASE_INSENSITIVE);
        return exactTitlePattern.matcher(title).matches();
    }

    private boolean titleBeginsWithTerm(final Pattern queryTermPattern, final String title) {
        Pattern titleBeginsWithPattern = Pattern.compile("^(" + queryTermPattern + ").*",
                Pattern.CASE_INSENSITIVE);
        return titleBeginsWithPattern.matcher(title).matches();
    }
}
