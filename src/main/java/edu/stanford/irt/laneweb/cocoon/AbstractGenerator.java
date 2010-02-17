package edu.stanford.irt.laneweb.cocoon;

import org.apache.cocoon.xml.XMLConsumer;


public abstract class AbstractGenerator extends AbstractSitemapModelComponent implements LanewebGenerator {
    
    protected XMLConsumer xmlConsumer;

    public final void setConsumer(XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer = xmlConsumer;
    }
}
