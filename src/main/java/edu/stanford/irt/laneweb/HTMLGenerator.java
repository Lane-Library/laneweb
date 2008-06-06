package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The neko html generator reads HTML from a source, converts it to XHTML and
 * generates SAX Events. It uses the NekoHTML library to do this. stolen from
 * cocoon-2.1.10 source
 */
public class HTMLGenerator extends ServiceableGenerator implements
        CacheableProcessingComponent {

    /** The source, if coming from a file */
    private Source inputSource;

    /** The source, if coming from the request */
    private InputStream requestStream;

    /**
     * Recycle this component. All instance variables are set to
     * <code>null</code>.
     */
    @Override
    public void recycle() {
        if (this.inputSource != null) {
            this.resolver.release(this.inputSource);
            this.inputSource = null;
            this.requestStream = null;
        }
        super.recycle();
    }

    /**
     * Setup the html generator. Try to get the last modification date of the
     * source for caching.
     * 
     * @throws IOException
     * @throws SAXException
     * @throws ProcessingException
     */
    @Override
    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters par) throws ProcessingException,
            SAXException, IOException {
        super.setup(resolver, objectModel, src, par);

        if (super.source != null) {
            this.inputSource = resolver.resolveURI(super.source);
        }
    }

    /**
     * Generate the unique key. This key must be unique inside the space of this
     * component. This method must be invoked before the generateValidity()
     * method.
     * 
     * @return The generated key or <code>0</code> if the component is
     *         currently not cacheable.
     */
    public java.io.Serializable getKey() {
        if (this.inputSource == null) {
            return null;
        }

        return this.inputSource.getURI();
    }

    /**
     * Generate the validity object. Before this method can be invoked the
     * generateKey() method must be invoked.
     * 
     * @return The generated validity object or <code>null</code> if the
     *         component is currently not cacheable.
     */
    public SourceValidity getValidity() {
        if (this.inputSource == null) {
            return null;
        }
        return this.inputSource.getValidity();
    }

    /**
     * Generate XML data.
     * 
     * @throws IOException
     * @throws SourceNotFoundException
     * @throws SAXException
     * 
     * @throws SAXException
     */
    public void generate() throws SourceNotFoundException, IOException,
            SAXException {
        HtmlSaxParser parser = new HtmlSaxParser();

        if (this.inputSource != null) {
            this.requestStream = this.inputSource.getInputStream();
        }

        parser.setContentHandler(this.contentHandler);
        try {
            parser.parse(new InputSource(this.requestStream));
        } finally {
            if (null != this.requestStream) {
                this.requestStream.close();
            }
        }
    }

    public static class HtmlSaxParser extends AbstractSAXParser {

        public HtmlSaxParser() {
            super(getConfig());
        }

        private static HTMLConfiguration getConfig() {
            HTMLConfiguration config = new HTMLConfiguration();
            config
                    .setProperty(
                            "http://cyberneko.org/html/properties/names/elems",
                            "lower");
            return config;
        }
    }
}
