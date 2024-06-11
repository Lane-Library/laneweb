package edu.stanford.irt.laneweb.eresources.model;

public class Link {

    private String additionalText;

    private String label;

    private String linkText;

    private LinkType type;

    private String url;

    private Version version;

    public Link() {
        // empty for serialization
    }

    public String getAdditionalText() {
        return this.additionalText;
    }

    public String getLabel() {
        return this.label;
    }

    public String getLinkText() {
        return this.linkText;
    }

    public LinkType getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public Version getVersion() {
        return this.version;
    }

    public void setAdditionalText(final String additionalText) {
        this.additionalText = additionalText;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setLinkText(final String linkText) {
        this.linkText = linkText;
    }

    public void setType(final LinkType type) {
        this.type = type;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setVersion(final Version version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new StringBuilder("url:").append(this.url).toString();
    }
}
