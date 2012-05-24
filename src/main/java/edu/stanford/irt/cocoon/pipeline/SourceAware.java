package edu.stanford.irt.cocoon.pipeline;

import org.apache.excalibur.source.Source;

/**
 * Source aware components get set with a Source object
 */
public interface SourceAware {

    /**
     * Set the Source object
     * 
     * @param source
     *            the Source object
     */
    void setSource(Source source);
}
