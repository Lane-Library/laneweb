package edu.stanford.irt.laneweb.eresources.model;

/**
 * An enum of the different types of Links
 */
public enum LinkType {

    LANE_DIGITAL("lane-digital"),  LANE_PRINT("lane-print"), NORMAL( "normal"), SUL_PRINT("sul-print");

    private String type;

    LinkType(final String type) {
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
