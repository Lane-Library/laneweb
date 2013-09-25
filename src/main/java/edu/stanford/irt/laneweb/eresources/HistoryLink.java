package edu.stanford.irt.laneweb.eresources;

/**
 * A temporary Link subclass to be used until the LINK_TEXT column is added to the history H_LINK table. And until the
 * ADDITIONAL_TEXT is added to that table.
 */
public class HistoryLink extends Link {

    private String additionalText;

    private Boolean isFirstLink;

    private String linkText;

    private String instruction;

    public HistoryLink(final String instruction, final String label, final LinkType type, final String url, final boolean isFirstLink) {
        super(label, type, url, null, null);
        this.instruction = instruction;
        this.isFirstLink = isFirstLink;
    }

    @Override
    public String getAdditionalText() {
        if (this.additionalText == null) {
            StringBuilder sb = new StringBuilder();
            if (this.isFirstLink) {
                Version version = getVersion();
                sb.append(" ");
                String summaryHoldings = version.getSummaryHoldings();
                if (summaryHoldings != null) {
                    sb.append(summaryHoldings);
                }
                maybeAppend(sb, version.getDates());
                maybeAppend(sb, version.getPublisher());
                maybeAppend(sb, version.getDescription());
                if (sb.length() == 1 && getLabel() != null) {
                    sb.append(getLabel());
                }
                if (this.instruction != null) {
                    if (sb.length() > 1) {
                        sb.append(", ");
                    }
                    sb.append(this.instruction);
                }
                if (sb.length() > 1) {
                    sb.append(" ");
                }
            } else {
                if (this.instruction != null) {
                    sb.append(" ").append(this.instruction);
                }
                if (getVersion().getPublisher() != null) {
                    sb.append(" ").append(getVersion().getPublisher());
                }
            }
            this.additionalText = sb.toString();
        }
        return this.additionalText;
    }

    @Override
    public String getLinkText() {
        if (this.linkText == null) {
            if (this.isFirstLink) {
                this.linkText = getVersion().getEresource().getTitle();
            } else {
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
        }
        return this.linkText;
    }

    private void maybeAppend(final StringBuilder sb, final String string) {
        if (string != null && string.length() > 0) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(string);
        }
    }
}
