package edu.stanford.irt.laneweb.eresources.bassett;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;

// $Id$
public abstract class AbstractBassettGenerator extends AbstractGenerator {

    protected BassettCollectionManager collectionManager;

    protected String query;

    public void setCollectionManager(final BassettCollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }
}