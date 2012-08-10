package edu.stanford.irt.cocoon.pipeline.generate;

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

import edu.stanford.irt.cocoon.CocoonException;
import edu.stanford.irt.cocoon.pipeline.SourceAware;

public class URLGenerator extends AbstractGenerator implements CacheableProcessingComponent, SourceAware {

    private SAXParser saxParser;

    private Source source;

    private String type;

    public URLGenerator(final String type) {
        this.type = type;
    }

    public Serializable getKey() {
        return this.source.getURI();
    }

    public String getType() {
        return this.type;
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
            throw new CocoonException(e);
        } catch (IOException e) {
            throw new CocoonException(e);
        }
    }
}
