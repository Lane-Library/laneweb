package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
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
public class HTMLGenerator extends ServiceableGenerator implements Configurable, CacheableProcessingComponent {

    /** The source, if coming from a file */
    private Source inputSource;

    /** The source, if coming from the request */
    private InputStream requestStream;

    /** Neko properties */
    private Properties properties;

    public void configure(final Configuration config) throws ConfigurationException {

        String configUrl = config.getChild("neko-config").getValue(null);

        if (configUrl != null) {
            org.apache.excalibur.source.SourceResolver resolver = null;
            Source configSource = null;
            try {
                resolver =
                        (org.apache.excalibur.source.SourceResolver) this.manager
                                                                                 .lookup(org.apache.excalibur.source.SourceResolver.ROLE);
                configSource = resolver.resolveURI(configUrl);
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug("Loading configuration from " + configSource.getURI());
                }

                this.properties = new Properties();
                this.properties.load(configSource.getInputStream());

            } catch (Exception e) {
                getLogger().warn("Cannot load configuration from " + configUrl);
                throw new ConfigurationException("Cannot load configuration from " + configUrl, e);
            } finally {
                if (null != resolver) {
                    this.manager.release(resolver);
                    resolver.release(configSource);
                }
            }
        }
    }

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
     */
    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        super.setup(resolver, objectModel, src, par);

        Request request = ObjectModelHelper.getRequest(objectModel);

        // append the request parameter to the URL if necessary
        if (par.getParameterAsBoolean("copy-parameters", false) && request.getQueryString() != null) {
            StringBuffer query = new StringBuffer(super.source);
            query.append(super.source.indexOf("?") == -1 ? '?' : '&');
            query.append(request.getQueryString());
            super.source = query.toString();
        }

        try {
            if (this.source != null) {
                this.inputSource = resolver.resolveURI(super.source);
            }
        } catch (SourceException se) {
            throw SourceUtil.handle("Unable to resolve " + super.source, se);
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
     * @throws SAXException
     */
    public void generate() throws IOException, SAXException {
        HtmlSaxParser parser = new HtmlSaxParser(this.properties);

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

        public HtmlSaxParser(final Properties properties) {
            super(getConfig(properties));
        }

        private static HTMLConfiguration getConfig(final Properties properties) {
            HTMLConfiguration config = new HTMLConfiguration();
            config.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
            if (properties != null) {
                for (Object element : properties.keySet()) {
                    String name = (String) element;
                    if (name.indexOf("/features/") > -1) {
                        config.setFeature(name, Boolean.getBoolean(properties.getProperty(name)));
                    } else if (name.indexOf("/properties/") > -1) {
                        config.setProperty(name, properties.getProperty(name));
                    }
                }
            }
            return config;
        }
    }
}
