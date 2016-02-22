package edu.stanford.irt.laneweb.solr;

/**
 * An enum of the different types of Links
 */
public enum LinkType {
    GETPASSWORD("getPassword"), IMPACTFACTOR("impactFactor"), NORMAL("normal");

    private String type;

    private LinkType(final String type) {
        this.type = type;
    }

    /**
     * provides the String representation of the instance.
     */
    @Override
    public String toString() {
        return this.type;
    }
}
