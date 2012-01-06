package edu.stanford.irt.laneweb.cocoon;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.store.Store;
import org.apache.excalibur.xml.xslt.XSLTProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;

import edu.stanford.irt.laneweb.LanewebException;

public class XSLTProcessorImpl implements XSLTProcessor, URIResolver {

    private static class TransformerHandlerAndValidity extends XSLTProcessor.TransformerHandlerAndValidity {

        protected TransformerHandlerAndValidity(final TransformerHandler transformerHandler,
                final SourceValidity transformerValidity) {
            super(transformerHandler, transformerValidity);
        }
    }

    private static class TraxErrorHandler implements ErrorListener {

        private Logger log = LoggerFactory.getLogger(XSLTProcessor.class);

        public void error(final TransformerException te) {
            this.log.error(getMessage(te));
        }

        public void fatalError(final TransformerException te) throws TransformerException {
            this.log.error(getMessage(te));
            throw te;
        }

        public void warning(final TransformerException te) {
            this.log.warn(getMessage(te));
        }

        private String getMessage(final TransformerException te) {
            StringBuilder sb = new StringBuilder(te.getMessage());
            SourceLocator locator = te.getLocator();
            if (locator != null) {
                sb.append(" publicId: ").append(locator.getPublicId()).append(" systemId: ").append(locator.getSystemId())
                        .append(" line: ").append(locator.getLineNumber()).append(" column: ").append(locator.getColumnNumber());
            }
            return sb.toString();
        }
    }

    /** The error handler for the transformer */
    private TraxErrorHandler errorHandler;

    /** The trax TransformerFactory this component uses */
    private SAXTransformerFactory factory;

    /** Hold the System ID of the main/base stylesheet */
    private String id;

    /** Resolver used to resolve XSLT document() calls, imports and includes */
    private SourceResolver resolver;

    /** The store service instance */
    private Store store;

    public XSLTProcessorImpl(final Store store, final SourceResolver sourceResolver,
            final SAXTransformerFactory factory) {
        this.store = store;
        this.resolver = sourceResolver;
        this.factory = factory;
        this.errorHandler = new TraxErrorHandler();
        this.factory.setErrorListener(this.errorHandler);
    }

    /**
     * @see org.apache.excalibur.xml.xslt.XSLTProcessor#getTransformerHandler(org.apache.excalibur.source.Source)
     */
    public TransformerHandler getTransformerHandler(final Source stylesheet) {
        XSLTProcessor.TransformerHandlerAndValidity validity = getTransformerHandlerAndValidity(stylesheet);
        return validity.getTransfomerHandler();
    }

    /**
     * @see org.apache.excalibur.xml.xslt.XSLTProcessor#getTransformerHandler(org.apache.excalibur.source.Source,
     *      org.xml.sax.XMLFilter)
     */
    public TransformerHandler getTransformerHandler(final Source stylesheet, final XMLFilter filter) {
        throw new UnsupportedOperationException();
    }

    public TransformerHandlerAndValidity getTransformerHandlerAndValidity(final Source stylesheet) {
        try {
            this.id = stylesheet.getURI();
            TransformerHandlerAndValidity handlerAndValidity = getTemplates(stylesheet, this.id);
            if (null == handlerAndValidity) {
                SourceValidity validity = stylesheet.getValidity();
                Templates template = this.factory.newTemplates(new StreamSource(stylesheet.getInputStream(), this.id));
                putTemplates(template, stylesheet, this.id);
                TransformerHandler handler = this.factory.newTransformerHandler(template);
                Transformer transformer = handler.getTransformer();
                transformer.setErrorListener(this.errorHandler);
                transformer.setURIResolver(this);
                handlerAndValidity = new TransformerHandlerAndValidity(handler, validity);
            }
            return handlerAndValidity;
        } catch (IOException e) {
            throw new LanewebException(e);
        } catch (TransformerException e) {
            throw new LanewebException(e);
        }
    }

    public TransformerHandlerAndValidity getTransformerHandlerAndValidity(final Source stylesheet, final XMLFilter filter) {
        throw new UnsupportedOperationException();
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
            return new StreamSource(is.getByteStream(), is.getSystemId());
        } catch (IOException ioe) {
            return null;
        }
    }

    public void setTransformerFactory(final String classname) {
        throw new UnsupportedOperationException();
    }

    public void transform(final Source source, final Source stylesheet, final Parameters params, final Result result) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a new <code>InputSource</code> object that uses the
     * <code>InputStream</code> and the system ID of the <code>Source</code>
     * object.
     * 
     * @throws SourceNotFoundException
     * @throws IOException
     *             if I/O error occured.
     */
    private InputSource getInputSource(final Source source) throws IOException {
        InputSource inputSource = new InputSource(source.getInputStream());
        inputSource.setSystemId(source.getURI());
        return inputSource;
    }

    private TransformerHandlerAndValidity getTemplates(final Source stylesheet, final String id) throws IOException, TransformerException {
        // we must augment the template ID with the factory classname since one
        // transformer implementation cannot handle the instances of a
        // template created by another one.
        String key = "XSLTTemplate: " + id + '(' + this.factory.getClass().getName() + ')';
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
        SourceValidity storedValidity = (SourceValidity) templateAndValidityAndIncludes[1];
        int valid = storedValidity.isValid();
        boolean isValid;
        if (valid == SourceValidity.UNKNOWN) {
            valid = storedValidity.isValid(newValidity);
            isValid = (valid == SourceValidity.VALID);
        } else {
            isValid = (valid == SourceValidity.VALID);
        }
        if (!isValid) {
            this.store.remove(key);
            return null;
        }
        TransformerHandler handler = this.factory.newTransformerHandler((Templates) templateAndValidityAndIncludes[0]);
        Transformer transformer = handler.getTransformer();
        transformer.setErrorListener(this.errorHandler);
        transformer.setURIResolver(this);
        return new TransformerHandlerAndValidity(handler, storedValidity);
    }

    private void putTemplates(final Templates templates, final Source stylesheet, final String id) throws IOException {
        String key = "XSLTTemplate: " + id + '(' + this.factory.getClass().getName() + ')';
        SourceValidity validity = stylesheet.getValidity();
        if (null != validity) {
            Object[] templateAndValidityAndIncludes = new Object[2];
            templateAndValidityAndIncludes[0] = templates;
            templateAndValidityAndIncludes[1] = validity;
            this.store.store(key, templateAndValidityAndIncludes);
        }
    }
}
