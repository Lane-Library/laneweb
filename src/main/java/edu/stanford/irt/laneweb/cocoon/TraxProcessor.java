package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;

import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.store.Store;
import org.apache.excalibur.xml.xslt.XSLTProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;

public class TraxProcessor {

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

    /** The store service instance */
    private Store store;
    
    private URIResolver uriResolver;

    public TraxProcessor(final SAXTransformerFactory factory, final URIResolver uriResolver, final Store store) {
        this.factory = factory;
        this.uriResolver = uriResolver;
        this.store = store;
        this.errorHandler = new TraxErrorHandler();
        this.factory.setErrorListener(this.errorHandler);
    }

    public TransformerHandler getTransformerHandler(final Source stylesheet) {
        try {
            String uri = stylesheet.getURI();
            TransformerHandler handler = getValidHandlerFromStore(stylesheet, uri);
            if (handler == null) {
                Templates templates = this.factory.newTemplates(new StreamSource(stylesheet.getInputStream(), uri));
                putTemplates(templates, stylesheet, uri);
                handler = prepareHandlerFromTemplates(templates);
            }
            return handler;
        } catch (IOException e) {
            throw new LanewebException(e);
        } catch (TransformerException e) {
            throw new LanewebException(e);
        }
    }
    
    private TransformerHandler getValidHandlerFromStore(final Source stylesheet, final String uri) throws IOException,
            TransformerException {
        // we must augment the template ID with the factory classname since one
        // transformer implementation cannot handle the instances of a
        // template created by another one.
        String key = "XSLTTemplate: " + uri + '(' + this.factory.getClass().getName() + ')';
        SourceValidity newValidity = stylesheet.getValidity();
        // Only stylesheets with validity are stored
        if (newValidity == null) {
            // Remove an old template
            this.store.remove(key);
            return null;
        }
        // Stored is an array of the templates and the caching time and list of
        // includes
        Object[] templateAndValidity = (Object[]) this.store.get(key);
        if (templateAndValidity == null) {
            // Templates not found in cache
            return null;
        }
        SourceValidity storedValidity = (SourceValidity) templateAndValidity[1];
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
        TransformerHandler handler = prepareHandlerFromTemplates((Templates) templateAndValidity[0]);
        return handler;
    }

    private TransformerHandler prepareHandlerFromTemplates(Templates templates) throws TransformerConfigurationException {
        TransformerHandler handler = this.factory.newTransformerHandler(templates);
        Transformer transformer = handler.getTransformer();
        transformer.setErrorListener(this.errorHandler);
        transformer.setURIResolver(this.uriResolver);
        return handler;
    }

    private void putTemplates(final Templates templates, final Source stylesheet, final String id) throws IOException {
        String key = "XSLTTemplate: " + id + '(' + this.factory.getClass().getName() + ')';
        SourceValidity validity = stylesheet.getValidity();
        if (null != validity) {
            Object[] templateAndValidity = new Object[2];
            templateAndValidity[0] = templates;
            templateAndValidity[1] = validity;
            this.store.store(key, templateAndValidity);
        }
    }
}
