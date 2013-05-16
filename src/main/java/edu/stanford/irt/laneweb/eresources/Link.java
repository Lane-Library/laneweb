package edu.stanford.irt.laneweb.eresources;

public class Link {

    private String additionalText;

    private String instruction;

    private String label;

    private String text;

    private LinkType type;

    private String url;

    private Version version;

    public Link(final String instruction, final String label, final LinkType type, final String url,
            final Version version) {
        this.instruction = instruction;
        this.label = label;
        this.type = type;
        this.url = url;
        this.version = version;
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

    public Version getVersion() {
        return this.version;
    }

    @Override
    public String toString() {
        return new StringBuilder("url:").append(this.url).toString();
    }
}
