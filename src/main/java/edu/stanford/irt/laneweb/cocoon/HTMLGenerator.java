package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.SourceAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.laneweb.LanewebException;

/**
 * The neko html generator reads HTML from a source, converts it to XHTML and
 * generates SAX Events. It uses the NekoHTML library to do this.
 */
public class HTMLGenerator extends AbstractGenerator implements CacheableProcessingComponent, SourceAware {

    private static class HtmlSAXParser extends AbstractSAXParser {

        protected HtmlSAXParser(final HTMLConfiguration conf) {
            super(conf);
        }
    }

    private static final String NAMESPACE = "http://www.w3.org/1999/xhtml";

    private Source source;

    private String type;

    public HTMLGenerator(final String type) {
        this.type = type;
    }

    /**
     * Generate the unique key. This key must be unique inside the space of this
     * component. This method must be invoked before the generateValidity()
     * method.
     * 
     * @return The generated key or <code>0</code> if the component is currently
     *         not cacheable.
     */
    public java.io.Serializable getKey() {
        return this.source.getURI();
    }

    public String getType() {
        return this.type;
    }

    /**
     * Generate the validity object. Before this method can be invoked the
     * generateKey() method must be invoked.
     * 
     * @return The generated validity object or <code>null</code> if the
     *         component is currently not cacheable.
     */
    public SourceValidity getValidity() {
        return this.source.getValidity();
    }

    public void setSource(final Source source) {
        this.source = source;
    }

    /**
     * Generate XML data.
     * 
     * @throws IOException
     * @throws SAXException
     */
    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        HTMLConfiguration conf = new HTMLConfiguration();
        // TODO: review properties
        conf.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
        conf.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        conf.setFeature("http://cyberneko.org/html/features/insert-namespaces", true);
        conf.setProperty("http://cyberneko.org/html/properties/namespaces-uri", NAMESPACE);
        AbstractSAXParser parser = new HtmlSAXParser(conf);
        parser.setContentHandler(xmlConsumer);
        InputSource inputSource = new InputSource();
        try {
            inputSource.setByteStream(this.source.getInputStream());
            inputSource.setSystemId(this.source.getURI());
            parser.parse(inputSource);
        } catch (IOException e) {
            throw new LanewebException(e);
        } catch (SAXException e) {
            throw new LanewebException(e);
        } finally {
            if (inputSource.getByteStream() != null) {
                try {
                    inputSource.getByteStream().close();
                } catch (IOException e) {
                    throw new LanewebException(e);
                }
            }
        }
    }
}
