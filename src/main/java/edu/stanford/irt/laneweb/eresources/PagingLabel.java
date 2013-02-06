package edu.stanford.irt.laneweb.eresources;

public class PagingLabel {

    private String end;

    private int results;

    private String start;

    public PagingLabel(final String startTitle, final String endString, final int resultCount) {
        this.start = startTitle;
        this.end = endString;
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
        StringBuffer sb = new StringBuffer();
        sb.append(this.start);
        sb.append(" -- ");
        sb.append(this.end);
        sb.append(" (");
        sb.append(this.results);
        sb.append(")");
        return sb.toString();
    }
}
