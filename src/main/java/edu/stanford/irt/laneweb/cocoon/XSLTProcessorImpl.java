package edu.stanford.irt.laneweb.cocoon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceResolver;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.AggregatedValidity;
import org.apache.excalibur.store.Store;
import org.apache.excalibur.xml.sax.XMLizable;
import org.apache.excalibur.xml.xslt.XSLTProcessor;
import org.apache.excalibur.xml.xslt.XSLTProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;

public class XSLTProcessorImpl implements XSLTProcessor, URIResolver {

    private static class TransformerHandlerAndValidity extends XSLTProcessor.TransformerHandlerAndValidity {

        protected TransformerHandlerAndValidity(final TransformerHandler transformerHandler,
                final SourceValidity transformerValidity) {
            super(transformerHandler, transformerValidity);
        }
    }

    private static class TraxErrorHandler implements ErrorListener {

        public void error(final TransformerException te) throws TransformerException {
            final String message = getMessage(te);
            if (null != LOGGER) {
                LOGGER.error(message, te);
            } else {
                System.out.println("ERROR: " + message);
            }
        }

        public void fatalError(final TransformerException te) throws TransformerException {
            final String message = getMessage(te);
            if (null != LOGGER) {
                LOGGER.error(message, te);
            } else {
                System.out.println("FATAL-ERROR: " + message);
            }
            throw te;
        }

        public void warning(final TransformerException te) throws TransformerException {
            final String message = getMessage(te);
            if (null != LOGGER) {
                LOGGER.warn(message, te);
            } else {
                System.out.println("WARNING: " + message);
            }
        }

        private String getMessage(final TransformerException te) {
            final SourceLocator locator = te.getLocator();
            if (null != locator) {
                // System.out.println("Parser fatal error: "+exception.getMessage());
                final String id = (locator.getPublicId() != null) ? locator.getPublicId()
                        : (null != locator.getSystemId()) ? locator.getSystemId() : "SystemId Unknown";
                return new StringBuffer("Error in TraxTransformer: ").append(id).append("; Line ").append(locator.getLineNumber())
                        .append("; Column ").append(locator.getColumnNumber()).append("; ").toString();
            }
            return "Error in TraxTransformer: " + te;
        }
    }

    private static Logger LOGGER = LoggerFactory.getLogger(XSLTProcessor.class);

    /**
     * Return a new <code>InputSource</code> object that uses the
     * <code>InputStream</code> and the system ID of the <code>Source</code>
     * object.
     * 
     * @throws IOException
     *             if I/O error occured.
     */
    private static InputSource getInputSource(final Source source) throws IOException, SourceException {
        final InputSource newObject = new InputSource(source.getInputStream());
        newObject.setSystemId(source.getURI());
        return newObject;
    }

    /** Hold the System ID of the main/base stylesheet */
    private String id;

    private SAXParser saxParser;

    /** Check included stylesheets */
    protected boolean checkIncludes;

    /** The default TransformerFactory used by this component */
    protected SAXTransformerFactory defaultFactory;

    /** The error handler for the transformer */
    protected TraxErrorHandler errorHandler;

    /** The trax TransformerFactory this component uses */
    protected SAXTransformerFactory factory;

    /** Map of pairs of System ID's / validities of the included stylesheets */
    protected Map<String, List<Object>> includesMap = new HashMap<String, List<Object>>();

    /** Resolver used to resolve XSLT document() calls, imports and includes */
    protected SourceResolver resolver;

    /** The store service instance */
    protected Store store;

    /** The configured transformer factory to use */
    protected String transformerFactory;

    public XSLTProcessorImpl(final SAXParser saxParser, final Store store, final SourceResolver sourceResolver) {
        this.saxParser = saxParser;
        this.store = store;
        this.resolver = sourceResolver;
    }

    /**
     * @see org.apache.excalibur.xml.xslt.XSLTProcessor#getTransformerHandler(org.apache.excalibur.source.Source)
     */
    public TransformerHandler getTransformerHandler(final Source stylesheet) throws XSLTProcessorException {
        return getTransformerHandler(stylesheet, null);
    }

    /**
     * @see org.apache.excalibur.xml.xslt.XSLTProcessor#getTransformerHandler(org.apache.excalibur.source.Source,
     *      org.xml.sax.XMLFilter)
     */
    public TransformerHandler getTransformerHandler(final Source stylesheet, final XMLFilter filter) throws XSLTProcessorException {
        final XSLTProcessor.TransformerHandlerAndValidity validity = getTransformerHandlerAndValidity(stylesheet, filter);
        return validity.getTransfomerHandler();
    }

    public TransformerHandlerAndValidity getTransformerHandlerAndValidity(final Source stylesheet) throws XSLTProcessorException {
        return getTransformerHandlerAndValidity(stylesheet, null);
    }

    public TransformerHandlerAndValidity getTransformerHandlerAndValidity(final Source stylesheet, final XMLFilter filter)
            throws XSLTProcessorException {
        if (filter != null) {
            throw new UnsupportedOperationException();
        }
        try {
            this.id = stylesheet.getURI();
            TransformerHandlerAndValidity handlerAndValidity = getTemplates(stylesheet, this.id);
            if (null == handlerAndValidity) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Creating new Templates for " + this.id);
                }
                // Create a Templates ContentHandler to handle parsing of the
                // stylesheet.
                TemplatesHandler templatesHandler = this.factory.newTemplatesHandler();
                // Set the system ID for the template handler since some
                // TrAX implementations (XSLTC) rely on this in order to obtain
                // a meaningful identifier for the Templates instances.
                templatesHandler.setSystemId(this.id);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Source = " + stylesheet + ", templatesHandler = " + templatesHandler);
                }
                // Initialize List for included validities
                SourceValidity validity = stylesheet.getValidity();
                if (validity != null && this.checkIncludes) {
                    this.includesMap.put(this.id, new ArrayList<Object>());
                }
                // from here must go recursive!!
                try {
                    // Process the stylesheet.
                    sourceToSAX(stylesheet, templatesHandler);
                    // Get the Templates object (generated during the parsing of
                    // the stylesheet) from the TemplatesHandler.
                    final Templates template = templatesHandler.getTemplates();
                    if (null == template) {
                        throw new XSLTProcessorException("Unable to create templates for stylesheet: " + stylesheet.getURI());
                    }
                    putTemplates(template, stylesheet, this.id);
                    // Create transformer handler
                    final TransformerHandler handler = this.factory.newTransformerHandler(template);
                    handler.getTransformer().setErrorListener(this.errorHandler);
                    handler.getTransformer().setURIResolver(this);
                    // Create aggregated validity
                    AggregatedValidity aggregated = null;
                    if (validity != null && this.checkIncludes) {
                        List<Object> includes = this.includesMap.get(this.id);
                        if (includes != null) {
                            aggregated = new AggregatedValidity();
                            aggregated.add(validity);
                            for (int i = includes.size() - 1; i >= 0; i--) {
                                aggregated.add((SourceValidity) ((Object[]) includes.get(i))[1]);
                            }
                            validity = aggregated;
                        }
                    }
                    // Create result
                    handlerAndValidity = new TransformerHandlerAndValidity(handler, validity);
                } finally {
                    if (this.checkIncludes) {
                        this.includesMap.remove(this.id);
                    }
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Reusing Templates for " + this.id);
                }
            }
            return handlerAndValidity;
        } catch (SAXException e) {
            // Unwrapping the exception will "remove" the real cause with
            // never Xalan versions and makes the exception message unusable
            throw new XSLTProcessorException("Exception in creating Transform Handler", e);
            /*
             * if( e.getException() == null ) { throw new
             * XSLTProcessorException(
             * "Exception in creating Transform Handler", e ); } else {
             * LOGGER.debug( "Got SAXException. Rethrowing cause exception.", e
             * ); throw new XSLTProcessorException(
             * "Exception in creating Transform Handler", e.getException() ); }
             */
        } catch (Exception e) {
            throw new XSLTProcessorException("Exception in creating Transform Handler", e);
        }
    }

    /**
     * Initialize
     */
    public void initialize() throws Exception {
        this.errorHandler = new TraxErrorHandler();
        if (this.factory == null) {
            this.factory = getTransformerFactory(this.transformerFactory);
        }
        this.factory.setErrorListener(this.errorHandler);
        this.defaultFactory = this.factory;
    }

    /**
     * Called by the processor when it encounters an xsl:include, xsl:import, or
     * document() function.
     * 
     * @param href
     *            An href attribute, which may be relative or absolute.
     * @param base
     *            The base URI in effect when the href attribute was
     *            encountered.
     * @return A Source object, or null if the href cannot be resolved, and the
     *         processor should try to resolve the URI itself.
     * @throws TransformerException
     *             if an error occurs when trying to resolve the URI.
     */
    public javax.xml.transform.Source resolve(final String href, final String base) throws TransformerException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("resolve(href = " + href + ", base = " + base + "); resolver = " + this.resolver);
        }
        Source xslSource = null;
        try {
            if (base == null || href.indexOf(":") > 1) {
                // Null base - href must be an absolute URL
                xslSource = this.resolver.resolveURI(href);
            } else if (href.length() == 0) {
                // Empty href resolves to base
                xslSource = this.resolver.resolveURI(base);
            } else {
                // is the base a file or a real url
                if (!base.startsWith("file:")) {
                    int lastPathElementPos = base.lastIndexOf('/');
                    if (lastPathElementPos == -1) {
                        // this should never occur as the base should
                        // always be protocol:/....
                        return null; // we can't resolve this
                    } else {
                        xslSource = this.resolver.resolveURI(base.substring(0, lastPathElementPos) + "/" + href);
                    }
                } else {
                    File parent = new File(base.substring(5));
                    File parent2 = new File(parent.getParentFile(), href);
                    xslSource = this.resolver.resolveURI(parent2.toURI().toURL().toExternalForm());
                }
            }
            InputSource is = getInputSource(xslSource);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("xslSource = " + xslSource + ", system id = " + xslSource.getURI());
            }
            if (this.checkIncludes) {
                // Populate included validities
                List<Object> includes = this.includesMap.get(this.id);
                if (includes != null) {
                    SourceValidity included = xslSource.getValidity();
                    if (included != null) {
                        includes.add(new Object[] { xslSource.getURI(), xslSource.getValidity() });
                    } else {
                        // One of the included stylesheets is not cacheable
                        this.includesMap.remove(this.id);
                    }
                }
            }
            return new StreamSource(is.getByteStream(), is.getSystemId());
        } catch (SourceException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Failed to resolve " + href + "(base = " + base + "), return null", e);
            }
            // CZ: To obtain the same behaviour as when the resource is
            // transformed by the XSLT Transformer we should return null here.
            return null;
        } catch (java.net.MalformedURLException mue) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Failed to resolve " + href + "(base = " + base + "), return null", mue);
            }
            return null;
        } catch (IOException ioe) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Failed to resolve " + href + "(base = " + base + "), return null", ioe);
            }
            return null;
        } finally {
            this.resolver.release(xslSource);
        }
    }

    /**
     * Set the transformer factory used by this component
     */
    public void setTransformerFactory(final String classname) {
        this.factory = getTransformerFactory(classname);
    }

    public void transform(final Source source, final Source stylesheet, final Parameters params, final Result result)
            throws XSLTProcessorException {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transform source = " + source + ", stylesheet = " + stylesheet + ", parameters = " + params
                        + ", result = " + result);
            }
            final TransformerHandler handler = getTransformerHandler(stylesheet);
            if (params != null) {
                final Transformer transformer = handler.getTransformer();
                transformer.clearParameters();
                String[] names = params.getNames();
                for (int i = names.length - 1; i >= 0; i--) {
                    transformer.setParameter(names[i], params.getParameter(names[i]));
                }
            }
            handler.setResult(result);
            sourceToSAX(source, handler);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transform done");
            }
        } catch (SAXException e) {
            // Unwrapping the exception will "remove" the real cause with
            // never Xalan versions and makes the exception message unusable
            final String message = "Error in running Transformation";
            throw new XSLTProcessorException(message, e);
            /*
             * if( e.getException() == null ) { final String message =
             * "Error in running Transformation"; throw new
             * XSLTProcessorException( message, e ); } else { final String
             * message = "Got SAXException. Rethrowing cause exception.";
             * LOGGER.debug( message, e ); throw new XSLTProcessorException(
             * "Error in running Transformation", e.getException() ); }
             */
        } catch (Exception e) {
            final String message = "Error in running Transformation";
            throw new XSLTProcessorException(message, e);
        }
    }

    @SuppressWarnings("unchecked")
    private TransformerHandlerAndValidity getTemplates(final Source stylesheet, final String id) throws IOException,
            SourceException, TransformerException {
        // we must augment the template ID with the factory classname since one
        // transformer implementation cannot handle the instances of a
        // template created by another one.
        String key = "XSLTTemplate: " + id + '(' + this.factory.getClass().getName() + ')';
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getTemplates: stylesheet " + id);
        }
        SourceValidity newValidity = stylesheet.getValidity();
        // Only stylesheets with validity are stored
        if (newValidity == null) {
            // Remove an old template
            this.store.remove(key);
            return null;
        }
        // Stored is an array of the templates and the caching time and list of
        // includes
        Object[] templateAndValidityAndIncludes = (Object[]) this.store.get(key);
        if (templateAndValidityAndIncludes == null) {
            // Templates not found in cache
            return null;
        }
        // Check template modification time
        SourceValidity storedValidity = (SourceValidity) templateAndValidityAndIncludes[1];
        int valid = storedValidity.isValid();
        boolean isValid;
        if (valid == 0) {
            valid = storedValidity.isValid(newValidity);
            isValid = (valid == 1);
        } else {
            isValid = (valid == 1);
        }
        if (!isValid) {
            this.store.remove(key);
            return null;
        }
        // Check includes
        if (this.checkIncludes) {
            AggregatedValidity aggregated = null;
            List<Object> includes = (List<Object>) templateAndValidityAndIncludes[2];
            if (includes != null) {
                aggregated = new AggregatedValidity();
                aggregated.add(storedValidity);
                for (int i = includes.size() - 1; i >= 0; i--) {
                    // Every include stored as pair of source ID and validity
                    Object[] pair = (Object[]) includes.get(i);
                    storedValidity = (SourceValidity) pair[1];
                    aggregated.add(storedValidity);
                    valid = storedValidity.isValid();
                    isValid = false;
                    if (valid == 0) {
                        Source includedSource = null;
                        try {
                            includedSource = this.resolver.resolveURI((String) pair[0]);
                            SourceValidity included = includedSource.getValidity();
                            if (included != null) {
                                valid = storedValidity.isValid(included);
                                isValid = (valid == 1);
                            }
                        } finally {
                            this.resolver.release(includedSource);
                        }
                    } else {
                        isValid = (valid == 1);
                    }
                    if (!isValid) {
                        this.store.remove(key);
                        return null;
                    }
                }
                storedValidity = aggregated;
            }
        }
        TransformerHandler handler = this.factory.newTransformerHandler((Templates) templateAndValidityAndIncludes[0]);
        handler.getTransformer().setErrorListener(this.errorHandler);
        handler.getTransformer().setURIResolver(this);
        return new TransformerHandlerAndValidity(handler, storedValidity);
    }

    /**
     * Get the TransformerFactory associated with the given classname. If the
     * class can't be found or the given class doesn't implement the required
     * interface, the default factory is returned.
     */
    private SAXTransformerFactory getTransformerFactory(final String factoryName) {
        SAXTransformerFactory _factory;
        if (null == factoryName) {
            _factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        } else {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                if (loader == null) {
                    loader = getClass().getClassLoader();
                }
                _factory = (SAXTransformerFactory) loader.loadClass(factoryName).newInstance();
            } catch (ClassNotFoundException cnfe) {
                LOGGER.error("Cannot find the requested TrAX factory '" + factoryName
                        + "'. Using default TrAX Transformer Factory instead.");
                if (this.factory != null) {
                    return this.factory;
                }
                _factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            } catch (ClassCastException cce) {
                LOGGER.error("The indicated class '" + factoryName
                        + "' is not a TrAX Transformer Factory. Using default TrAX Transformer Factory instead.");
                if (this.factory != null) {
                    return this.factory;
                }
                _factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            } catch (Exception e) {
                LOGGER.error("Error found loading the requested TrAX Transformer Factory '" + factoryName
                        + "'. Using default TrAX Transformer Factory instead.");
                if (this.factory != null) {
                    return this.factory;
                }
                _factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            }
        }
        // _factory.setErrorListener(this.errorHandler);
        _factory.setURIResolver(this);
        return _factory;
    }

    private void putTemplates(final Templates templates, final Source stylesheet, final String id) throws IOException {
        // we must augment the template ID with the factory classname since one
        // transformer implementation cannot handle the instances of a
        // template created by another one.
        String key = "XSLTTemplate: " + id + '(' + this.factory.getClass().getName() + ')';
        // only stylesheets with a last modification date are stored
        SourceValidity validity = stylesheet.getValidity();
        if (null != validity) {
            // Stored is an array of the template and the current time
            Object[] templateAndValidityAndIncludes = new Object[3];
            templateAndValidityAndIncludes[0] = templates;
            templateAndValidityAndIncludes[1] = validity;
            if (this.checkIncludes) {
                templateAndValidityAndIncludes[2] = this.includesMap.get(id);
            }
            this.store.store(key, templateAndValidityAndIncludes);
        }
    }

    private void sourceToSAX(final Source source, final ContentHandler handler) throws SAXException, IOException, SourceException {
        if (source instanceof XMLizable) {
            ((XMLizable) source).toSAX(handler);
        } else {
            final InputStream inputStream = source.getInputStream();
            InputSource input = new InputSource(inputStream);
            try {
                this.saxParser.parse(input, handler);
            } finally {
                inputStream.close();
            }
        }
    }
}
