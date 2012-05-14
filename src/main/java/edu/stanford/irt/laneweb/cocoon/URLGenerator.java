package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.Serializable;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class URLGenerator extends AbstractGenerator implements CacheableProcessingComponent, SourceAware {

    private SAXParser saxParser;

    private Source source;

    public Serializable getKey() {
        return this.source.getURI();
    }

    public SourceValidity getValidity() {
        return this.source.getValidity();
    }

    public void setParser(final SAXParser saxParser) {
        this.saxParser = saxParser;
    }

    public void setSource(final Source source) {
        this.source = source;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try {
            if (this.source instanceof XMLizable) {
                ((XMLizable) this.source).toSAX(xmlConsumer);
            } else {
                InputSource inputSource = new InputSource(this.source.getInputStream());
                inputSource.setSystemId(this.source.getURI());
                this.saxParser.parse(inputSource, xmlConsumer);
            }
        } catch (SAXException e) {
            throw new LanewebException(e);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
