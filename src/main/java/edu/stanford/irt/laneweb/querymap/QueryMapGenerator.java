package edu.stanford.irt.laneweb.querymap;

import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

public class QueryMapGenerator extends AbstractQueryMapComponent implements Generator {

    private ThreadLocal<XMLConsumer> consumer = new ThreadLocal<XMLConsumer>();
    
    public void generate() throws SAXException {
        XMLConsumer consumer = this.consumer.get();
        if (null == consumer) {
            super.reset();
            throw new IllegalStateException("null consumer");
        }
        XMLizable queryMap = new XMLizableQueryMap(super.getQueryMap());
        try {
            consumer.startDocument();
            queryMap.toSAX(consumer);
            consumer.endDocument();
        } finally {
            this.consumer.set(null);
            super.reset();
        }
    }

    public void setConsumer(final XMLConsumer consumer) {
        if (null == consumer) {
            throw new IllegalArgumentException("null consumer");
        }
        this.consumer.set(consumer);
    }

}
