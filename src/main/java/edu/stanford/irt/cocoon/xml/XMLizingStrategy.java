package edu.stanford.irt.cocoon.xml;

import org.apache.cocoon.xml.XMLConsumer;

/**
 * A strategy to allow refactoring of XMLizable classes to permit easier testing
 * as well as alternate strategies for the same class.
 * 
 * @param <T>
 *            any class
 */
public interface XMLizingStrategy<T> {

    /**
     * Send SAX event representation of an object to an xmlConsumer.
     * 
     * @param object
     * @param xmlConsumer
     */
    void toSAX(T object, XMLConsumer xmlConsumer);
}
