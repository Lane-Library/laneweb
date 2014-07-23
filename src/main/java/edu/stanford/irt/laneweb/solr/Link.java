package edu.stanford.irt.laneweb.solr;

public class Link {

    private String additionalText = null;

    private String label = null;

    private String text = null;

    private String type = null;

    private String url = null;

    public String getAdditionalText() {
        return this.additionalText;
    }

    public String getLabel() {
        return this.label;
    }

    public String getText() {
        return this.text;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setAdditionalText(final String additionalText) {
        this.additionalText = additionalText;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
