package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.Serializable;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class URLGenerator extends AbstractGenerator implements CacheableProcessingComponent {

    private SAXParser saxParser;

    public void generate() throws IOException, SAXException {
    	Source source = getSource();
    	if (source instanceof XMLizable) {
    		((XMLizable)source).toSAX(this.xmlConsumer);
    	} else {
    		InputSource inputSource = new InputSource(source.getInputStream());
    		inputSource.setSystemId(source.getURI());
    		this.saxParser.parse(inputSource, this.xmlConsumer);
    	}
    }

    public Serializable getKey() {
        return getSource().getURI();
    }

    public SourceValidity getValidity() {
        return getSource().getValidity();
    }

    public void setParser(final SAXParser saxParser) {
        this.saxParser = saxParser;
    }
}
