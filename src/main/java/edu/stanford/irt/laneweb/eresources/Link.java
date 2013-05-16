package edu.stanford.irt.laneweb.eresources;

public class Link {

    private String additionalText;

    private String instruction;

    private String label;

    private String text;

    private LinkType type;

    private String url;

    public String getAdditionalText() {
        return this.additionalText;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public String getLabel() {
        return this.label;
    }

    public String getText() {
        return this.text;
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

    public void setAdditionalText(final String text) {
        this.additionalText = text;
    }

    public void setInstruction(final String instruction) {
        this.instruction = instruction;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setText(final String text) {
        this.text = text;
    }

    /**
     * set the LinkType
     * 
     * @param type
     *            the LinkType
     */
    public void setType(final LinkType type) {
        this.type = type;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new StringBuilder("url:").append(this.url).toString();
    }
}
