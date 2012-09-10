package edu.stanford.irt.cocoon.xml;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;
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

import edu.stanford.irt.cocoon.CocoonException;

public class TransformerHandlerFactory {

    /** The error handler for the transformer */
    private ErrorListener errorHandler;

    /** The trax TransformerFactory this component uses */
    private SAXTransformerFactory factory;

    /** The store service instance */
    private Map<String, Object[]> store;

    private URIResolver uriResolver;

    public TransformerHandlerFactory(final SAXTransformerFactory factory, final URIResolver uriResolver,
            final ErrorListener errorListener) {
        this.factory = factory;
        this.uriResolver = uriResolver;
        this.errorHandler = errorListener;
        this.factory.setErrorListener(this.errorHandler);
        this.store = Collections.synchronizedMap(new HashMap<String, Object[]>());
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
            throw new CocoonException(e);
        } catch (TransformerException e) {
            throw new CocoonException(e);
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
        Object[] templateAndValidity = this.store.get(key);
        if (templateAndValidity == null) {
            // Templates not found in cache
            return null;
        }
        SourceValidity storedValidity = (SourceValidity) templateAndValidity[1];
        if (storedValidity.isValid() != SourceValidity.VALID) {
            this.store.remove(key);
            return null;
        }
        return prepareHandlerFromTemplates((Templates) templateAndValidity[0]);
    }

    private TransformerHandler prepareHandlerFromTemplates(final Templates templates) throws TransformerConfigurationException {
        TransformerHandler handler = this.factory.newTransformerHandler(templates);
        Transformer transformer = handler.getTransformer();
        transformer.setErrorListener(this.errorHandler);
        transformer.setURIResolver(this.uriResolver);
        return handler;
    }

    private void putTemplates(final Templates templates, final Source stylesheet, final String id) throws IOException {
        SourceValidity validity = stylesheet.getValidity();
        if (null != validity) {
            Object[] templateAndValidity = new Object[2];
            templateAndValidity[0] = templates;
            templateAndValidity[1] = validity;
            this.store.put(id, templateAndValidity);
        }
    }
}
