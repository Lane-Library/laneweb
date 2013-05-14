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
}
