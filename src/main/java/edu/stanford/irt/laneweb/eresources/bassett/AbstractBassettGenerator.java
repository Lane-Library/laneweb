package edu.stanford.irt.laneweb.eresources.bassett;

import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;

import edu.stanford.irt.laneweb.model.AbstractObjectModelAware;

// $Id$
public abstract class AbstractBassettGenerator extends AbstractObjectModelAware implements Generator {

    protected BassettCollectionManager collectionManager;
    protected String query;
    protected XMLConsumer xmlConsumer;

    public void setCollectionManager(final BassettCollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer = xmlConsumer;
    }
}