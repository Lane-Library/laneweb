package edu.stanford.irt.laneweb.cocoon;

import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.xml.AbstractXMLPipe;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

import edu.stanford.irt.laneweb.LanewebException;

public class TransformerSerializer extends AbstractXMLPipe implements Serializer, CacheableProcessingComponent {

    /**
     * The caching key
     */
    private String cachingKey;

    /**
     * The <code>Properties</code> used by this serializer.
     */
    private Properties properties = new Properties();

    /**
     * The trax <code>TransformerFactory</code> used by this serializer.
     */
    private SAXTransformerFactory transformerFactory;

    public TransformerSerializer(final SAXTransformerFactory factory, final Properties properties) {
        this.transformerFactory = factory;
        this.properties = properties;
        StringBuilder sb = new StringBuilder();
        //use a TreeSet to get an ordered set:
        Set<Object> keys = new TreeSet<Object>(properties.keySet());
        for (Object key : keys) {
            sb.append(';').append(key).append('=').append(properties.get(key));
        }
        this.cachingKey = sb.toString();
    }

    /**
     * Generate the unique key. This key must be unique inside the space of this
     * component. This method must be invoked before the generateValidity()
     * method.
     * 
     * @return The generated key or <code>0</code> if the component is currently
     *         not cacheable.
     */
    public String getKey() {
        return this.cachingKey;
    }

    public String getMimeType() {
        throw new UnsupportedOperationException();
    }

    /**
     * Generate the validity object. Before this method can be invoked the
     * generateKey() method must be invoked.
     * 
     * @return The generated validity object or <code>null</code> if the
     *         component is currently not cacheable.
     */
    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }

    @Override
    public void setOutputStream(final OutputStream out) {
        try {
            TransformerHandler handler = this.transformerFactory.newTransformerHandler();
            handler.getTransformer().setOutputProperties(this.properties);
            handler.setResult(new StreamResult(out));
            this.setContentHandler(handler);
            this.setLexicalHandler(handler);
        } catch (TransformerConfigurationException e) {
            throw new LanewebException(e);
        }
    }

    public boolean shouldSetContentLength() {
        throw new UnsupportedOperationException();
    }
}
