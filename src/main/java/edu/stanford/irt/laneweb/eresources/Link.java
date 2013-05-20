package edu.stanford.irt.laneweb.eresources;

public class Link {

    private String instruction;

    private String label;

    private LinkType type;

    private String url;

    private Version version;

    public Link(final String instruction, final String label, final LinkType type, final String url) {
        this.instruction = instruction;
        this.label = label;
        this.type = type;
        this.url = url;
    }
    
    public String getLinkText() {
        StringBuilder sb = new StringBuilder();
        if (LinkType.IMPACTFACTOR.equals(this.type)) {
            sb.append("Impact Factor");
        } else {
            String summaryHoldings = this.version.getSummaryHoldings();
            if (summaryHoldings != null && this.version.getLinks().size() == 1) {
                sb.append(summaryHoldings);
                String dates = this.version.getDates();
                if (dates != null) {
                    sb.append(", ").append(dates);
                }
            } else {
                if (this.label != null) {
                    sb.append(this.label);
                }
            }
            if (sb.length() == 0) {
                sb.append(this.url);
            }
            String description = this.version.getDescription();
            if (description != null) {
                sb.append(" ").append(description);
            }
        }
        return sb.toString();
    }

    public String getAdditionalText() {
        StringBuilder sb = new StringBuilder();
        if (this.instruction != null) {
            sb.append(" ").append(this.instruction);
        }
        if (this.version.getPublisher() != null) {
            sb.append(" ").append(this.version.getPublisher());
        }
        return sb.toString();
    }

    public String getInstruction() {
        return this.instruction;
    }

    public String getLabel() {
        return this.label;
    }

    public String getPrimaryAdditionalText() {
        StringBuilder sb = new StringBuilder(this.version.getPrimaryAdditionalText());
        if (sb.length() == 1 && this.label != null) {
            sb.append(this.label);
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
        return sb.toString();
    }

    /**
     * get the LinkType of this Link
     * 
     * @return the LinkType
     */
    public LinkType getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public Version getVersion() {
        return this.version;
    }

    @Override
    public String toString() {
        return new StringBuilder("url:").append(this.url).toString();
    }

    void setVersion(final Version version) {
        this.version = version;
    }
}
