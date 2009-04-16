package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.SourceValidity;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The neko html generator reads HTML from a source, converts it to XHTML and
 * generates SAX Events. It uses the NekoHTML library to do this.
 */
public class HTMLGenerator implements Generator, CacheableProcessingComponent {

    /** The source, if coming from a file */
    private Source source;

    private XMLConsumer xmlConsumer;

    /**
     * Generate XML data.
     * 
     * @throws IOException
     * @throws SourceNotFoundException
     * @throws SAXException
     */
    public void generate() throws SourceNotFoundException, IOException, SAXException {
        HTMLConfiguration conf = new HTMLConfiguration();
        conf.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
        conf.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
        AbstractSAXParser parser = new HtmlSAXParser(conf); 
        parser.setContentHandler(this.xmlConsumer);
        parser.parse(new InputSource(this.source.getInputStream()));
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

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    /**
     * Setup the html generator. Try to get the last modification date of the
     * source for caching.
     * 
     * @throws IOException
     * @throws SAXException
     * @throws ProcessingException
     */
    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) throws ProcessingException, SAXException,
            IOException {
        this.source = resolver.resolveURI(src);
    }
    
    private static class HtmlSAXParser extends AbstractSAXParser{

        protected HtmlSAXParser(XMLParserConfiguration conf) {
            super(conf);
        }
    }
}
