package edu.stanford.irt.laneweb.eresources;

public class Link {

    private String instruction;

    private String label;

    private LinkType type;

    private String url;

    private Version version;
    
    private String linkText;

    private String additionalText;

    public Link(final String instruction, final String label, final LinkType type, final String url, final String linkText, final String additionalText) {
        this.instruction = instruction;
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

    public String getInstruction() {
        return this.instruction;
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
