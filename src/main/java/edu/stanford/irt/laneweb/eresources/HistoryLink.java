package edu.stanford.irt.laneweb.eresources;

/**
 * A temporary Link subclass to be used until the LINK_TEXT column is added to the history H_LINK table. And until the
 * ADDITIONAL_TEXT is added to that table.
 */
public class HistoryLink extends Link {

    private String additionalText;

    private Boolean isFirstLink;

    private String linkText;

    public HistoryLink(final String instruction, final String label, final LinkType type, final String url) {
        super(instruction, label, type, url, null, null);
    }

    @Override
    public String getAdditionalText() {
        if (this.additionalText == null) {
            StringBuilder sb = new StringBuilder();
            if (isFirstLink()) {
                sb.append(getVersion().getPrimaryAdditionalText());
                if (sb.length() == 1 && getLabel() != null) {
                    sb.append(getLabel());
                }
                if (getInstruction() != null) {
                    if (sb.length() > 1) {
                        sb.append(", ");
                    }
                    sb.append(getInstruction());
                }
                if (sb.length() > 1) {
                    sb.append(" ");
                }
            } else {
                if (getInstruction() != null) {
                    sb.append(" ").append(getInstruction());
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
            if (isFirstLink()) {
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

    private boolean isFirstLink() {
        if (this.isFirstLink == null) {
            Version version = getVersion();
            if (equals(version.getLinks().iterator().next())
                    && version.equals(version.getEresource().getVersions().iterator().next())) {
                this.isFirstLink = Boolean.TRUE;
            } else {
                this.isFirstLink = Boolean.FALSE;
            }
        }
        return Boolean.valueOf(this.isFirstLink);
    }
}
