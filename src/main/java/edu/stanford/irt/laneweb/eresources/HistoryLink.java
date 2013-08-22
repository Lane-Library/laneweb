package edu.stanford.irt.laneweb.eresources;

/**
 * A temporary Link subclass to be used until the LINK_TEXT column is added to the history H_LINK table.
 */
public class HistoryLink extends Link {

    private String linkText;

    public HistoryLink(final String instruction, final String label, final LinkType type, final String url) {
        super(instruction, label, type, url, null, null);
    }

    @Override
    public String getLinkText() {
        if (this.linkText == null) {
            StringBuilder sb = new StringBuilder();
            Version version = getVersion();
            String summaryHoldings = version.getSummaryHoldings();
            if (summaryHoldings != null && version.getLinks().size() == 1) {
                sb.append(summaryHoldings);
                String dates = version.getDates();
                if (dates != null) {
                    sb.append(", ").append(dates);
                }
            } else {
                String label = getLabel();
                if (label != null) {
                    sb.append(label);
                }
            }
            if (sb.length() == 0) {
                sb.append(getUrl());
            }
            String description = version.getDescription();
            if (description != null) {
                sb.append(" ").append(description);
            }
            this.linkText = sb.toString();
        }
        return this.linkText;
    }
}
