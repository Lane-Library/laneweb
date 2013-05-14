package edu.stanford.irt.laneweb.eresources;


/**
 * A Link that has a particular LinkType
 */
public class TypedLink extends Link {

    private LinkType type;

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
}
