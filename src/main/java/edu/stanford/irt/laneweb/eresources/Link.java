package edu.stanford.irt.laneweb.eresources;

public class Link {

    private String label;

    private LinkType type;

    private String url;
    
    private String linkText;

    private String additionalText;

    public Link(final String label, final LinkType type, final String url, final String linkText, final String additionalText) {
        this.label = label;
        this.type = type;
        this.url = url;
        this.linkText = linkText;
        this.additionalText = additionalText;
    }
    
    public String getLinkText() {
        return this.linkText;
    }

    public String getAdditionalText() {
        return this.additionalText;
    }

    public String getLabel() {
        return this.label;
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
