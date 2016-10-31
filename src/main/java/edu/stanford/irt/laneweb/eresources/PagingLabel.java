package edu.stanford.irt.laneweb.eresources;

import java.io.Serializable;

public class PagingLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String end;

    private int results;

    private String start;

    public PagingLabel(final String startTitle, final String endTitle, final int resultCount) {
        this.start = startTitle;
        this.end = endTitle;
        this.results = resultCount;
    }

    public String getEnd() {
        return this.end;
    }

    public int getResults() {
        return this.results;
    }

    public String getStart() {
        return this.start;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.start);
        sb.append(" -- ");
        sb.append(this.end);
        sb.append(" (");
        sb.append(this.results);
        sb.append(')');
        return sb.toString();
    }
}
