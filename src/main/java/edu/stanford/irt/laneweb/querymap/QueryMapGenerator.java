package edu.stanford.irt.laneweb.querymap;

import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.LanewebGenerator;

public class QueryMapGenerator extends AbstractQueryMapComponent implements LanewebGenerator {
    
    private XMLConsumer consumer;

    public void generate() throws SAXException {
        if (null == this.consumer) {
            throw new IllegalStateException("null consumer");
        }
        XMLizable queryMap = new XMLizableQueryMap(super.getQueryMap());
        this.consumer.startDocument();
        queryMap.toSAX(this.consumer);
        this.consumer.endDocument();
    }

    public void setConsumer(final XMLConsumer consumer) {
        if (null == consumer) {
            throw new IllegalArgumentException("null consumer");
        }
        this.consumer = consumer;
    }
}
