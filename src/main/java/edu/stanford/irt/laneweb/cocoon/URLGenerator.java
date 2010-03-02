package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.Serializable;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.source.util.SourceUtil;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.SAXException;


public class URLGenerator extends AbstractGenerator implements CacheableProcessingComponent {
    
    private SAXParser saxParser;
    
    public void setParser(SAXParser saxParser) {
        this.saxParser = saxParser;
    }

    public Serializable getKey() {
        return this.source.getURI();
    }

    public SourceValidity getValidity() {
        return this.source.getValidity();
    }

    public void generate() throws IOException, SAXException, ProcessingException {
        SourceUtil.parse(this.saxParser, this.source, this.xmlConsumer);
    }
}
