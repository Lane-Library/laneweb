package edu.stanford.irt.laneweb.eresources;

import edu.stanford.irt.eresources.impl.LinkImpl;

/**
 * A Link that has a particular LinkType
 */
public class TypedLink extends LinkImpl {

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
