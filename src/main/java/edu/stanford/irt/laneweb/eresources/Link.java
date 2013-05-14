/**
 * 
 */
package edu.stanford.irt.laneweb.eresources;


/**
 * @author ceyates
 */
public class Link {

    private String instruction;

    private String label;

    private String url;

    private LinkType type;

    private String additionalText;

    private String text;

    // private int seqnum;
    //	
    // public int getSeqnum() {
    // return this.seqnum;
    // }
    //	
    // public void setSeqnum(int seqnum) {
    // this.seqnum = seqnum;
    // }
    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Link#getInstruction()
     */
    public String getInstruction() {
        return this.instruction;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Link#getLabel()
     */
    public String getLabel() {
        return this.label;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Link#getUrl()
     */
    public String getUrl() {
        return this.url;
    }

    /*
     * (non-Javadoc)
     * @see
     * edu.stanford.irt.eresources.impl.Link#setInstruction(java.lang.String)
     */
    public void setInstruction(final String instruction) {
        this.instruction = instruction;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Link#setLabel(java.lang.String)
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /*
     * (non-Javadoc)
     * @see edu.stanford.irt.eresources.impl.Link#setUrl(java.lang.String)
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new StringBuilder("url:").append(this.url).toString();
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

    /**
     * get the LinkType of this Link
     * 
     * @return the LinkType
     */
    public LinkType getType() {
        return this.type;
    }

    public String getAdditionalText() {
        return this.additionalText;
    }

    public void setAdditionalText(String text) {
        this.additionalText = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
