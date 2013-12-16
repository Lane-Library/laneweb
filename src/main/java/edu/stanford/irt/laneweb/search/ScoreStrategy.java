package edu.stanford.irt.laneweb.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.resource.AbstractScoreStrategy;
import edu.stanford.irt.search.impl.DefaultContentResult;

public class ScoreStrategy extends AbstractScoreStrategy {

    private static final Pattern DOUBLE_WEIGHT_PATTERN = Pattern
            .compile("pubmed_cochrane_reviews|dare|acpjc|bmj_clinical_evidence");

    private static final Pattern ENGINEID_PATTERN = Pattern.compile("_content_\\d+");

    private static final Pattern HALF_WEIGHT_PATTERN = Pattern
            .compile("^pubmed_(clinicaltrial|recent_reviews|treatment_focused|diagnosis_focused|prognosis_focused|harm_focused|etiology_focused|epidemiology_focused)");

    private static final Pattern QUARTER_WEIGHT_PATTERN = Pattern.compile("aafp_patients|medlineplus_0");

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
     */
    public int computeScore(final DefaultContentResult searchResult, final Pattern queryTermPattern) {
        int score;
        double weight = computeWeight(ENGINEID_PATTERN.matcher(searchResult.getId()).replaceFirst(""));
        Pattern titleBeginsWithPattern = Pattern.compile("^(" + queryTermPattern.toString() + ").*",
                Pattern.CASE_INSENSITIVE);
        String title = searchResult.getTitle();
        boolean titleBeginsWithQueryTerms = titleBeginsWithPattern.matcher(title).matches();
        Pattern exactTitlePattern = Pattern.compile("^(" + queryTermPattern.toString() + "\\W?)$",
                Pattern.CASE_INSENSITIVE);
        boolean exactTitle = exactTitlePattern.matcher(title).matches();
        int titleHits = 0;
        int descriptionHits = 0;
        Matcher ntMatcher = queryTermPattern.matcher(title);
        while (ntMatcher.find()) {
            titleHits++;
        }
        String description = searchResult.getDescription();
        if (null != description) {
            Matcher ndMatcher = queryTermPattern.matcher(description);
            while (ndMatcher.find()) {
                descriptionHits++;
            }
        }
        if (exactTitle) {
            score = 100;
        } else if (titleBeginsWithQueryTerms && titleHits > 1 && descriptionHits > 1) {
            score = 90;
        } else if (titleBeginsWithQueryTerms && titleHits > 1 && descriptionHits == 1) {
            score = 80;
        } else if (titleBeginsWithQueryTerms && titleHits > 1) {
            score = 70;
        } else if (titleBeginsWithQueryTerms) {
            score = 65;
        } else if (titleHits > 1 && descriptionHits > 1) {
            score = 60;
        } else if (titleHits > 1) {
            score = 50;
        } else if (titleHits > 0 && descriptionHits > 1) {
            score = 40;
        } else if (titleHits > 0 && descriptionHits > 0) {
            score = 30;
        } else if (titleHits > 0) {
            score = 20;
        } else if (descriptionHits > 0) {
            score = 10;
        } else {
            score = 1;
        }
        score = (int) ((score + computeDateAdjustment(searchResult.getYear())) * weight);
        return score < 0 ? 0 : score;
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
}
