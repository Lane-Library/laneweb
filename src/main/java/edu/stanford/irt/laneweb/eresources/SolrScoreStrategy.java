package edu.stanford.irt.laneweb.eresources;

import edu.stanford.irt.laneweb.resource.AbstractScoreStrategy;

public class SolrScoreStrategy extends AbstractScoreStrategy {

    private static final int MAX_INT_MINUS_100 = Integer.MAX_VALUE - 100;

    // TODO: can probably achieve all of this in solr itself
    public int computeScore(final String query, final String title, final int year, final Float solrScore, final Float solrMaxScore) {
        int score = (int) ((solrScore.floatValue() / solrMaxScore.floatValue()) * 100);
        if (query.equalsIgnoreCase(title)) {
            score = MAX_INT_MINUS_100;
        } else if (title.indexOf('(') > -1 && query.equalsIgnoreCase(title.replaceFirst(" \\(.*", ""))) {
            score = MAX_INT_MINUS_100;
        }
        score = score + computeDateAdjustment(year);
        return score;
    }
}