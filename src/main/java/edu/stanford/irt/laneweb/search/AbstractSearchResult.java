package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

public class AbstractSearchResult implements SearchResult {

    private static final Pattern NON_FILING_PATTERN = Pattern.compile("^(a|an|the) ", Pattern.CASE_INSENSITIVE);

    private static final Pattern WHITESPACE = Pattern.compile("\\W");

    private int hashCode;

    private int score;

    private String sortTitle;

    public AbstractSearchResult(final int score, final String title) {
        this.score = score < 0 ? 0 : score;
        this.sortTitle = AbstractSearchResult.WHITESPACE
                .matcher(AbstractSearchResult.NON_FILING_PATTERN.matcher(title).replaceFirst("")).replaceAll("")
                .toLowerCase();
        this.hashCode = this.sortTitle.hashCode();
    }

    public int compareTo(final SearchResult o) {
        int value = o.getScore() - this.score;
        if (value == 0) {
            value = this.sortTitle.compareTo(o.getSortTitle());
        }
        return value;
    }

    // explicitly call Object.equals() to satisfy sonar
    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    public int getScore() {
        return this.score;
    }

    public String getSortTitle() {
        return this.sortTitle;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
