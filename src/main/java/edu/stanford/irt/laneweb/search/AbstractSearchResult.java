package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

public abstract class AbstractSearchResult implements SearchResult {

    private static final Pattern NON_FILING_PATTERN = Pattern.compile("^(a|an|the) ", Pattern.CASE_INSENSITIVE);

    private static final Pattern WHITESPACE = Pattern.compile("\\W");

    private int score;

    private String sortTitle;

    private String title;

    public AbstractSearchResult(final int score, final String title) {
        this.score = score < 0 ? 0 : score;
        this.title = title;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public String getSortTitle() {
        if (this.sortTitle == null) {
            String temp = NON_FILING_PATTERN.matcher(this.title).replaceFirst("");
            this.sortTitle = WHITESPACE.matcher(temp).replaceAll("").toLowerCase();
        }
        return this.sortTitle;
    }
}
