package edu.stanford.irt.laneweb.eresources;

public class Link {

    private String additionalText;

    private String holdingsAndDates;

    private String label;

    private String linkText;

    private LinkType type;

    private String url;

    public Link(final String label, final LinkType type, final String url, final String linkText,
            final String additionalText, final String holdingsAndDates) {
        this.label = label;
        this.type = type;
        this.url = url;
        this.linkText = linkText;
        this.additionalText = additionalText;
        this.holdingsAndDates = holdingsAndDates;
    }

    public String getAdditionalText() {
        return this.additionalText;
    }

    public String getHoldingsAndDates() {
        return this.holdingsAndDates;
    }

    public String getLabel() {
        return this.label;
    }

    public String getLinkText() {
        return this.linkText;
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

    @Override
    public String toString() {
        return new StringBuilder("url:").append(this.url).toString();
    }
}
